package com.xd.algorithm;

import com.xd.data.GraphProcess;
import com.xd.graph.Edge;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/11.
 */
public class GA {


    //时间
    long start = System.currentTimeMillis();
    long end ;

    // 种群的规模
    private int pop_size;

    //变异概率
    public double pm0 = 0.005,pm1 = 0.01, pm2 = 0.05;

    public double pro_init_server = 0.6;

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

    public Population[] oldPop, newPop;

    private Graph graph;

    private GraphProcess graphProcess;

    private List<List> pathList, bestList;

    public HashMap<Integer, Boolean> bestServer = new HashMap<>();

    private int bestCost = Integer.MAX_VALUE;

    private int bestId;

    private int maxServerNum;

    private FastPQDijkstra pqDijkstra;


    public GA(int pop_size, int chrom_size, int generation_num, GraphProcess graphProcess){
        this.pop_size = pop_size;
        this.chrom_size = chrom_size;
        this.generation_num = generation_num;
        this.graphProcess = graphProcess;
        this.graph = graphProcess.getGraph();
        this.maxServerNum = graphProcess.getGraph().getUserVertexs().size();
        this.pqDijkstra = new FastPQDijkstra(graph);
    }


    public void addSuperSource(HashMap<Integer, Boolean> hashMap){
        for (Integer id:hashMap.keySet()){
            graph.add(new Edge(graph.networkVertexnum+1, id, Integer.MAX_VALUE, 0));
        }
    }


    public int calFit(Population pop, int gen){


        graph.serverIds.clear();
        NetworkVertex networkVertex = null;
        for (int i=0; i < pop.chrom.length; ++i){
            if (pop.chrom[i] == 1) {
                networkVertex = graph.getNetworkVertices().get(i);
                graph.serverIds.put(networkVertex.id, true);
            }

        }

        int startServerSize = graph.serverIds.size() + 1;

        //使用dijkstra算法求得最小路径
        graphProcess.addEdgesOfVertex();
        int sum = pqDijkstra.searchGraphPaths(graph.userAdjVertices, graph.table);

        //使用最小费用最大流
//        graphProcess.addEdges();
//        addSuperSource(graph.serverIds);
//        PathCost pqDijkstra = new PathCost(graph);
//        int sum = pqDijkstra.minPathCost(graph.table);
//        System.out.println(sum);

        int endServerSize = graph.serverIds.size() + 1;

        usageRate = endServerSize * 1.0 / startServerSize;

        pathList = pqDijkstra.getAllPathList();

        pop.cost = sum;

        pop.fitness = pop.cost * usageRate;
//        pop.fitness = pop.cost * 1.0;

        compareCost(sum, pathList, gen);

        return sum;
    }

    public boolean compareCost(int cost, List pathList, int gen){
        if (cost < bestCost){
            bestCost = cost;
            bestList = pathList;
            bestId = gen;

            bestServer.clear();
            for (Integer id:graph.serverIds.keySet()){
                bestServer.put(id, true);
            }
            return true;
        }
        return false;
    }

    public double getUsageRate(){
        return  usageRate;
    }


    public void statistics(Population[] pop, int gen){
        double tmpFitness;
        double sumFitness1=0;

        sumFitness = 0;
        maxFitness = 0;
        minFitness = Double.MAX_VALUE;

        for (int i=0; i < pop_size; ++i){
            tmpFitness = pop[i].fitness;

            if (tmpFitness > maxFitness){
                maxFitness = tmpFitness;
                maxPop = i;
            }


            if (tmpFitness < minFitness){
                minFitness = tmpFitness;
                minPop = i;
            }

//            System.out.println(oldPop[i]);
        }

//        System.out.println("======");

        for (int i=0; i < pop_size; ++i){
//            sumFitness += 1- pop[i].fitness /(maxFitness + minFitness);
            sumFitness += (maxFitness - pop[i].fitness) / maxFitness - minFitness;
            sumFitness1 += pop[i].fitness;

        }

        avgFitness = sumFitness1 / pop_size;


        double pc0 = 0.4, pc1 = 0.6, pc2 = 0.9, para1 = 0.78, para2 = 0.0005;
        // 自适应交叉率
        if (minFitness/avgFitness > para1 && minFitness / maxFitness >= pc1 / pc2){
            pro_cross = pc0;
        }
        else if (minFitness / avgFitness > para1 && minFitness / maxFitness > para2 && minFitness / maxFitness < pc1 / pc2){
            pro_cross = pc2 - (pc2 - pc1) / (1 - minFitness/maxFitness);
        }
        else {
            pro_cross = pc2;
        }

        //自适应变异率

        if (minFitness/avgFitness > para1 && minFitness / maxFitness >= pm1 / pm2){
            pro_mutate = pm2;
//            System.out.println("2");
        }
        else if (minFitness / avgFitness > para1 && minFitness / maxFitness > para2 && minFitness / maxFitness < pm1 / pm2){
            pro_mutate = pm2 - (pm2 - pm1) / (1 - minFitness/maxFitness);
//            System.out.println("1");
        }
        else {
            pro_mutate = pm0;
//            System.out.println("0");
        }



    }


    void initPop(){
        int i,j=0;
        oldPop = new Population[pop_size];
        newPop = new Population[pop_size];

        Population population1,population2 ;
        for (i=0; i < pop_size; ++i){
            population1 = new Population(chrom_size);
            population2 = new Population(chrom_size);
            oldPop[i] = population1;
            newPop[i] = population2;

            for (j=0; j < chrom_size; ++j) {
                if (excise() <= pro_init_server) {
                    oldPop[i].chrom[j] = 1;
                }
            }

            oldPop[i].maxServerNum++;

            calFit(oldPop[i], 0);


        }
    }

    double excise(){
        double pp;
        pp = random.nextDouble();
        return pp;
    }

    int selection(){
        double wheelPos, randNumber, partsum = 0;
        int i = 0;

        randNumber = random.nextDouble();

        wheelPos = randNumber*sumFitness;
        do {

//            partsum += ( 1 - oldPop[i].fitness / (minFitness + maxFitness));
            partsum += (maxFitness - oldPop[i].fitness) / (maxFitness - minFitness);
            ++i;

        }while((partsum < wheelPos) && i < pop_size);
        return i-1;
    }



    boolean crossOver(int[] parent1, int[] parent2, int chr1, int chr2){
        int j;
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

            for (j = 0; j <= crossPos1; ++j){
                newPop[chr1].chrom[j] = parent1[j];
                newPop[chr2].chrom[j] = parent2[j];
            }

            for (; j < crossPos2; ++j){
                newPop[chr1].chrom[j] = parent2[j];
                newPop[chr2].chrom[j] = parent1[j];
            }
            for (; j < chrom_size; ++j){
                newPop[chr1].chrom[j] = parent1[j];
                newPop[chr2].chrom[j] = parent2[j];
            }
            return true;
        }
        return false;

    }

    boolean oneCrossOver(int[] parent1, int[] parent2, int chr1, int chr2){
        int j;
        int crossPos1, crossPos2;
        if (excise() <= pro_cross){
            crossPos1 = random.nextInt(chrom_size);


            for (j = 0; j <= crossPos1; ++j){
                newPop[chr1].chrom[j] = parent1[j];
                newPop[chr2].chrom[j] = parent2[j];
            }

            for (; j < chrom_size; ++j){
                newPop[chr1].chrom[j] = parent2[j];
                newPop[chr2].chrom[j] = parent1[j];
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
                newPop.chrom[i] = 1 - oldPop[random.nextInt(pop_size)].chrom[i];
            }
        }

    }

    void annealing(double delta, Population pop, int loc){


        double pro = Math.pow(Math.E, -delta / T);

        if (excise() > pro){

            for (int i=0; i < chrom_size; ++i){
                pop.chrom[i] = oldPop[loc].chrom[i];
            }
            pop.cost = oldPop[loc].cost;
            pop.fitness = oldPop[loc].fitness;


        }


    }



    void generation(int gen){
        int mate1,mate2;


        for (int i=1; i < pop_size; i = i+2){

            mate1 = selection();
            mate2 = selection();

            crossOver(oldPop[mate1].chrom, oldPop[mate2].chrom, i, i+1);

//            for (int j=0; j < chrom_size; ++j){
//                mutation(newPop[i].chrom[j]);
//                mutation(newPop[i+1].chrom[j]);
//            }

            twoBinaryMutation(newPop[i]);
            twoBinaryMutation(newPop[i+1]);

            calFit(newPop[i], gen);
            calFit(newPop[i+1], gen);

//            if (newPop[i].fitness > oldPop[mate1].fitness){
//                annealing(newPop[i].fitness - oldPop[mate1].fitness, newPop[i], mate1);
//            }
//            if (newPop[i+1].fitness > oldPop[mate2].fitness){
//                annealing(newPop[i+1].fitness - oldPop[mate2].fitness, newPop[i+1], mate2);
//            }

        }


        for (int j=0; j < chrom_size; ++j){
            newPop[0].chrom[j] = oldPop[minPop].chrom[j];
        }
        newPop[0].cost = oldPop[minPop].cost;
        newPop[0].fitness = oldPop[minPop].fitness;

    }

    void initParamter(){
        if (graph.networkVertexnum < 250){
            this.pm0 = 0.005;
            this.pm1 = 0.007;
            this.pm2 = 0.01;

            this.T = chrom_size * pop_size ;

            this.pro_init_server = 0.4;
            System.out.println(this.pro_init_server);
        }
        else if (graph.networkVertexnum < 600){
            this.pm0 = 0.003;
            this.pm1 = 0.005;
            this.pm2 = 0.005;

            this.T = chrom_size * pop_size ;

            this.pro_init_server = 0.6;
            System.out.println(this.pro_init_server);
        }
        else {
            this.pm0 = 0.004;
            this.pm1 = 0.005;
            this.pm2 = 0.007;

            this.pro_init_server = (graph.userVertexnums * 1.1 / 2) / graph.aliveNetVerticesNum;
            System.out.println(this.pro_init_server);
        }
    }

    public void startGA(){


        long end;
        random = new Random(System.currentTimeMillis());
        int gen = 0;


        initParamter();
        initPop();
        statistics(oldPop, gen);
        double d = maxFitness - minFitness;

        T = d * T;

        while (gen < generation_num){
            T = T * 0.97;


            ++gen;

            end = System.currentTimeMillis();



//            System.out.println(end - start);
            if (end - start > 84*1000){
                System.out.println(end - start);
                System.out.println(gen);
                break;
            }

            if (gen % 3 == 0){
                graph.shuffleUseradjVertice(graph.userAdjVertices);
            }


            generation(gen);
            statistics(newPop, gen);

            for (int i=0; i < pop_size; ++i){
                for (int j=0; j < chrom_size; ++j){
                    oldPop[i].chrom[j] = newPop[i].chrom[j];
                }
                oldPop[i].cost = newPop[i].cost;
                oldPop[i].fitness = newPop[i].fitness;
            }

        }


    }

    public class Population{
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
