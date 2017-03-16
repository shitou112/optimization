package com.xd.algorithm;

import com.xd.data.GraphProcess;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;

import java.util.List;
import java.util.Random;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/11.
 */
public class GA {
    // 种群的规模
    private int pop_size;

    // 交叉概率
    private double pro_cross;

    // 变异概率
    private double pro_mutate;

    private double pro_better_mutate;

    //同或概率
    private double pro_xnor;

    // 染色体长度
    private int chrom_size;

    // 繁殖代数
    private int generation_num;


    private double sumFitness, usageRate, minFitness = Double.MAX_VALUE, maxFitness = Double.MAX_VALUE;

    private int[] Pi;

    private double pro_init_server;

    private int minPop, maxPop;

    private Random random;

    private Population[] oldPop, newPop;

    private Graph graph;

    private GraphProcess graphProcess;

    private List<List> pathList, bestList;

    private int bestCost = Integer.MAX_VALUE;

    private int bestId;

    private int serverNum;



    public GA(int pop_size, int chrom_size, int generation_num, double pro_cross, double pro_mutate, double pro_better_mutate,double pro_init_server, double pro_xnor,
               GraphProcess graphProcess){
        this.pop_size = pop_size;
        this.pro_cross = pro_cross;
        this.pro_mutate = pro_mutate;
        this.pro_better_mutate = pro_better_mutate;
        this.pro_xnor = pro_xnor;
        this.pro_init_server = pro_init_server;
        this.chrom_size = chrom_size;
        this.generation_num = generation_num;
        this.oldPop = new Population[pop_size];
        this.newPop = new Population[pop_size];
        this.Pi = new int[pop_size];
        this.graphProcess = graphProcess;
        this.graph = graphProcess.getGraph();
        this.serverNum = graphProcess.getGraph().getUserVertexs().size();
    }



    public int calFit(int[] chr){
        graphProcess.addEdgesOfVertex();
        graph.serverIds.clear();
        NetworkVertex networkVertex = null;
        for (int i=0; i < chr.length; ++i){
            if (chr[i] == 1) {
                networkVertex = graph.getNetworkVertices().get(i);
                graph.serverIds.put(networkVertex.id, true);
            }
        }

        int startServerNum = graph.serverIds.size();

        //使用dijkstra算法求得最小路径
        FastPQDijkstra pqDijkstra = new FastPQDijkstra(graph);

        int sum = pqDijkstra.searchGraphPaths(graph.userAdjVertices, graph.table);
        pathList = pqDijkstra.getAllPathList();

        int endServerNum = graph.serverIds.size();

        usageRate = startServerNum *1.0/ endServerNum;

        return sum;
    }

    public double getUsageRate(){
        return  usageRate;
    }


    public void statistics(Population[] pop, int gen){
        double tmpFitness;
        int i=0;

        for (; i < pop_size; ++i){
            tmpFitness = pop[i].fitness;

            if (tmpFitness > maxFitness){
                maxFitness = pop[i].fitness;
                maxPop = i;
            }

            if (tmpFitness < minFitness){
                minFitness = pop[i].fitness;
                minPop = i;
            }


            sumFitness += 1000.0 / tmpFitness;
        }

    }


    void initPop(){
        int i,j;
        oldPop = new Population[pop_size];
        newPop = new Population[pop_size];
        Population population1,population2 ;
        for (i=0; i < pop_size; ++i){
            population1 = new Population(chrom_size);
            population2 = new Population(chrom_size);
            oldPop[i] = population1;
            newPop[i] = population2;
            for (j=0; j < Math.round(serverNum*1.2) && j < chrom_size; j++){
                if (excise() < pro_init_server) {
                    oldPop[i].chrom[j] = 1;
                }
            }
            oldPop[i].cost = calFit(oldPop[i].chrom);
            oldPop[i].fitness = oldPop[i].cost * getUsageRate();
            if (oldPop[i].cost < bestCost) {
                bestCost = oldPop[i].cost;
                bestList = pathList;
            }
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
            partsum += ( 1000.0 / oldPop[i].fitness);
            i++;
        }while((partsum < wheelPos) && i < pop_size);
        return i-1;
    }

    boolean crossOver(int[] parent1, int[] parent2, int chr1, int chr2){
        int j;
        int crossPos1 = chrom_size-1, crossPos2 = chrom_size -1;
        if (excise() <= pro_cross){
            crossPos1 = random.nextInt(chrom_size);
            crossPos2 = random.nextInt(chrom_size);
            if (crossPos1 > crossPos2){
                int temp = crossPos1;
                crossPos1 = crossPos2;
                crossPos2 = crossPos1;
            }

            for (j = 0; j <= crossPos1; j++){
                newPop[chr1].chrom[j] = parent1[j];
                newPop[chr2].chrom[j] = parent2[j];
            }

            for (; j < crossPos2; j++){
                newPop[chr1].chrom[j] = parent2[j];
                newPop[chr2].chrom[j] = parent1[j];
            }
            for (; j < chrom_size; j++){
                newPop[chr1].chrom[j] = parent1[j];
                newPop[chr2].chrom[j] = parent2[j];
            }
            return true;
        }
        return false;

    }

    int mutation(int alleles){
        double pp = excise();
        if (pp <= pro_mutate){
            if (alleles==1)
                alleles = 0;
            else {
                if (pp <= pro_better_mutate){
                    if (alleles == 0)
                        alleles =1;
                }
            }
        }
        return alleles;

    }

    void xnor(int[] parent1, int[] parent2, int i){
        int j;
        int xnor = 0;
        if (excise() <= pro_xnor){
            xnor = random.nextInt(chrom_size);
        }

        for (j = 0; j < xnor; ++j) {
            newPop[i].chrom[j] = parent1[j];
        }
        for (; j < chrom_size; ++j) {
            newPop[i].chrom[j] = parent1[j] == parent2[j] ? parent1[j] : 0;
        }

    }

    void generation(){
        int mate1,mate2;
        for (int i=1; i < pop_size; i = i+2){

            mate1 = selection();
            mate2 = selection();
            crossOver(oldPop[mate1].chrom, oldPop[mate2].chrom, i, i+1);
            xnor(oldPop[mate1].chrom, oldPop[mate2].chrom, i);
            for (int j = 0; j < chrom_size; ++j) {
                newPop[i].chrom[j] = mutation(newPop[i].chrom[j]);
                newPop[i+1].chrom[j] = mutation(newPop[i+1].chrom[j]);
            }
            newPop[i].cost = calFit(newPop[i].chrom);
            newPop[i].fitness = newPop[i].cost * getUsageRate();

            newPop[i+1].cost = calFit(newPop[i+1].chrom);
            newPop[i+1].fitness = newPop[i+1].cost * getUsageRate();
            if (newPop[i].cost < bestCost) {
                bestCost = oldPop[i].cost;
                bestList = pathList;
            }
            if (newPop[i+1].cost < bestCost){
                bestCost = oldPop[i+1].cost;
                bestList = pathList;
            }
        }

        for (int j=0; j < chrom_size; ++j){
            newPop[0].chrom[j] = oldPop[minPop].chrom[j];
        }
        newPop[0].cost = oldPop[minPop].cost;
        newPop[0].fitness = oldPop[minPop].fitness;


    }

    public void startGA(){
        random = new Random(System.currentTimeMillis());
        int gen = 0;
        initPop();

        statistics(oldPop, gen);
        while (gen < generation_num){
            ++gen;
            if (gen % 3 ==0 ) {
                graph.shuffleUseradjVertice(graph.userAdjVertices);
            }
            if (gen % 100 == 0)
                random = new Random(System.currentTimeMillis());


            generation();


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
        int chrom[];

        // 花费
        int cost;

        boolean isMutation;

        double fitness;


        public Population(int size){
            chrom = new int[size];
        }

        @Override
        public String toString() {
            String str = "";
            for (int i =0; i<chrom_size;++i){
                str+=chrom[i]+" ";
            }
            str += cost;
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
