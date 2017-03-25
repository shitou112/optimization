package com.xd.algorithm;

import com.xd.data.GraphProcess;
import com.xd.graph.Edge;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;

import java.util.*;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/11.
 */
public class MyGA {


    //时间
    long start = System.currentTimeMillis();
    long end ;

    // 种群的规模
    private int pop_size;

    // 交叉概率
    private double pro_cross;

    // 变异概率
    private double pro_mutate;

    //退火参数d
    private double T;


    // 染色体长度
    private int chrom_size;

    // 繁殖代数
    private int generation_num;


    private double sumFitness, avgFitness, usageRate, minFitness = Double.MAX_VALUE, maxFitness = Double.MAX_VALUE;


    private int minPop, maxPop;

    private Random random;

    public List<Population> oldPop, newPop;

    private Graph graph;

    private GraphProcess graphProcess;

    private List<List> pathList, bestList;

    public HashMap<Integer, Boolean> bestServer = new HashMap<>();

    private int bestCost = Integer.MAX_VALUE;

    private int bestId;

    private int maxServerNum;


    public MyGA(int pop_size, int chrom_size, int generation_num, GraphProcess graphProcess){
        this.pop_size = pop_size;
        this.chrom_size = chrom_size;
        this.generation_num = generation_num;
        this.graphProcess = graphProcess;
        this.graph = graphProcess.getGraph();
        this.maxServerNum = graphProcess.getGraph().getUserVertexs().size();
    }



    public void addSuperSource(HashMap<Integer, Boolean> hashMap){
        for (Integer id:hashMap.keySet()){
            graph.add(new Edge(graph.networkVertexnum+1, id, Integer.MAX_VALUE, 0));
        }
    }

    public int calFit(Population pop, int gen){
        graphProcess.addEdgesOfVertex();
        graph.serverIds.clear();
        NetworkVertex networkVertex = null;
        for (int i=0; i < pop.chrom.length; ++i){
            if (pop.chrom[i] == 1) {
                networkVertex = graph.getNetworkVertices().get(i);
                graph.serverIds.put(networkVertex.id, true);
            }
        }


        //使用dijkstra算法求得最小路径
        FastPQDijkstra pqDijkstra = new FastPQDijkstra(graph);
        int sum = pqDijkstra.searchGraphPaths(graph.userAdjVertices, graph.table);

//        graphProcess.addEdges();
//        addSuperSource(graph.serverIds);
//        PathCost pqDijkstra = new PathCost(graph);
//        int sum = pqDijkstra.minPathCost(graph.table);

        pathList = pqDijkstra.getAllPathList();

        pop.cost = sum;

        pop.fitness = pop.cost * 1.0;

        compareCost(sum, pathList, gen);
        return sum;
    }

    public void compareCost(int cost, List pathList, int gen){
        if (cost < bestCost){
            bestCost = cost;
            bestList = pathList;
            bestId = gen;

            bestServer.clear();
            for (Integer id:graph.serverIds.keySet()){
                bestServer.put(id, true);
            }
        }
    }

    public double getUsageRate(){
        return  usageRate;
    }


    public void statistics(List<Population> pop, int gen){
        double tmpFitness;
        double sumFitness1=0;

        sumFitness = 0;
        maxFitness = 0;
        minFitness = Double.MAX_VALUE;

        for (int i=0; i < pop_size; ++i){
            tmpFitness = pop.get(i).fitness;

            if (tmpFitness > maxFitness){
                maxFitness = tmpFitness;
                maxPop = i;
            }


            if (tmpFitness < minFitness){
                minFitness = tmpFitness;
                minPop = i;
            }

        }


        for (int i=0; i < pop_size; ++i){
//            sumFitness += 1- pop[i].fitness /(maxFitness + minFitness);
            sumFitness += (maxFitness - pop.get(i).fitness) / maxFitness - minFitness;
            sumFitness1 += pop.get(i).fitness;

        }

        avgFitness = sumFitness1 / pop_size;


        double pc0 = 0.3, pc1 = 0.6, pc2 = 0.9, para1 = 0.78, para2 = 0.0005;
        // 自适应交叉率
        if (minFitness/avgFitness > para1 && minFitness / maxFitness >= pc1 / pc2){
            pro_cross = pc0;
//            System.out.println("1");
        }
        else if (minFitness / avgFitness > para1 && minFitness / maxFitness > para2 && minFitness / maxFitness < pc1 / pc2){
            pro_cross = pc2 - (pc2 - pc1) / (1 - minFitness/maxFitness);
//            System.out.println("2");
        }
        else {
            pro_cross = pc2;
//            System.out.println("3");
        }

        //自适应变异率
        double pm0 = 0.005,pm1 = 0.01, pm2 = 0.1;
        if (minFitness/avgFitness > para1 && minFitness / maxFitness >= pm1 / pm2){
            pro_mutate = pm2;
        }
        else if (minFitness / avgFitness > para1 && minFitness / maxFitness > para2 && minFitness / maxFitness < pm1 / pm2){
            pro_mutate = pm2 - (pm2 - pm1) / (1 - minFitness/maxFitness);
        }
        else {
            pro_mutate = pm0;
        }

    }


    void initPop(){
        int i,j=0;
        oldPop = new ArrayList<>(pop_size);
        newPop = new ArrayList<>(pop_size);

        Population population1,population2 ;
        for (i=0; i < pop_size; ++i){
            population1 = new Population(chrom_size);
            population2 = new Population(chrom_size);
            oldPop.add(population1);
            newPop.add(population2);

            for (j=0; j < chrom_size; ++j) {
                if (excise() <= 0.3) {
                    oldPop.get(i).chrom[j] = 1;
                }
            }

            oldPop.get(i).maxServerNum++;

            calFit(oldPop.get(i), 0);


        }
    }

    double excise(){
        double pp;
        pp = random.nextDouble();
        return pp;
    }

    void selection(){
        Collections.sort(oldPop);

        int i;
        for (i=0; i < (int)(pop_size*0.1); ++i){
            for (int j=0; j < chrom_size; ++j){
                newPop.get(i).chrom[j] = oldPop.get(i).chrom[j];
            }
            newPop.get(i).fitness = oldPop.get(i).fitness;
            newPop.get(i).cost = oldPop.get(i).cost;
        }
        int loc;
        for (; i < pop_size; ++i){
            loc = wheel();
            for (int j=0; j < chrom_size; ++j){
                newPop.get(i).chrom[j] = oldPop.get(loc).chrom[j];
            }
            newPop.get(i).fitness = oldPop.get(loc).fitness;
            newPop.get(i).cost = oldPop.get(loc).cost;
        }

        Collections.shuffle(newPop);

        for (int j=0; j < chrom_size; ++j){
            newPop.get(0).chrom[j] = oldPop.get(0).chrom[j];
        }
        newPop.get(0).cost = oldPop.get(0).cost;
        newPop.get(0).fitness = oldPop.get(0).fitness;


    }

    int wheel(){
        double wheelPos, randNumber, partsum = 0;
        int i = 0;

        randNumber = random.nextDouble();

        wheelPos = randNumber*sumFitness;
        do {

//            partsum += ( 1 - oldPop.get(i).fitness / (minFitness + maxFitness));
            partsum += (maxFitness - oldPop.get(i).fitness) / (maxFitness - minFitness);
            ++i;

        }while((partsum < wheelPos) && i < pop_size);
        return i-1;
    }



    boolean crossOver(Population first, Population second){
        int j, tmp;
        int crossPos1, crossPos2;
        if (excise() <= pro_cross){
            crossPos1 = random.nextInt(chrom_size);
            while ((crossPos2 = random.nextInt(chrom_size))==crossPos1){

            }
            if (crossPos1 > crossPos2){
                int temp = crossPos1;
                crossPos1 = crossPos2;
                crossPos2 = temp;
            }



            for (j=crossPos1; j < crossPos2; ++j){
                tmp = first.chrom[j];
                first.chrom[j] = second.chrom[j];
                second.chrom[j] = tmp;
            }

            return true;
        }
        return false;

    }



    int mutation( int alleles){

        if (excise() <= pro_mutate) {
            return 1 - alleles;
        }
        return alleles;

    }

    void twoBinaryMutation(Population newPop){


        for (int i=0; i < chrom_size; ++i) {
            if (excise() <= pro_mutate) {
                newPop.chrom[i] = 1 - oldPop.get(random.nextInt(pop_size)).chrom[i];
            }
        }

    }

    void annealing(double delta, Population pop, int loc){


        double pro = Math.pow(Math.E, -delta / T);


        if (excise() > pro){

            for (int i=0; i < chrom_size; ++i){
                pop.chrom[i] = oldPop.get(loc).chrom[i];
            }
            pop.cost = oldPop.get(loc).cost;
            pop.fitness = oldPop.get(loc).fitness;

        }

        T = T * 0.99;
    }



    void generation(int gen){
        int mate1,mate2;

        selection();


        for (int i=1; i < pop_size; i = i+2){

            crossOver(newPop.get(i), newPop.get(i+1));


            twoBinaryMutation(newPop.get(i));
            twoBinaryMutation(newPop.get(i+1));

            calFit(newPop.get(i), gen);
            calFit(newPop.get(i+1), gen);

//            if (newPop.get(i).fitness > oldPop[mate1].fitness){
//                annealing(newPop.get(i).fitness - oldPop[mate1].fitness, newPop.get(i), mate1);
//            }
//            if (newPop[i+1].fitness > oldPop[mate2].fitness){
//                annealing(newPop[i+1].fitness - oldPop[mate2].fitness, newPop[i+1], mate2);
//            }

        }




    }

    public void startGA(){


        long end;
        random = new Random(System.currentTimeMillis());
        int gen = 0;

        initPop();
        statistics(oldPop, gen);
        double d = maxFitness - minFitness;

        T = d * chrom_size;

        while (gen < generation_num){
            ++gen;

            end = System.currentTimeMillis();

//            System.out.println(end - start);
            if (end - start > 80*1000){
                System.out.println(end - start);
                System.out.println(gen);
                break;
            }

            if (gen %3 == 0){
                graph.shuffleUseradjVertice(graph.userAdjVertices);
            }



            generation(gen);
            statistics(newPop, gen);

            for (int i=0; i < pop_size; ++i){
                for (int j=0; j < chrom_size; ++j){
                    oldPop.get(i).chrom[j] = newPop.get(i).chrom[j];
                }
                oldPop.get(i).cost = newPop.get(i).cost;
                oldPop.get(i).fitness = newPop.get(i).fitness;
            }
        }


    }

    public class Population implements Comparable<Population>{
        // 基因组
        public int chrom[];

        // 花费
        int cost;

        int maxServerNum;

        double fitness;

        public int[] mutateStates;


        public Population(int size){
            chrom = new int[size];
            mutateStates = new int[size];
        }

        @Override
        public String toString() {
            String str = "";
            for (int i =0; i<chrom_size;++i){
                str+=chrom[i]+" ";
            }
            str += cost+" fit "+ fitness;
            return str;
        }

        @Override
        public int compareTo(Population o) {
            if (this.fitness < o.fitness)
                return -1;
            else if(this.fitness == o.fitness)
                return 0;
            else
                return 1;
        }
    }

    public int getBestCost() {
        return bestCost;
    }

    public List<List> getBestList() {
        return bestList;
    }

    public int getBestId() {
        return bestId;
    }
}
