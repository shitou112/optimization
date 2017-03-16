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

    private int sumFitness, minFitness = Integer.MAX_VALUE, maxFitness = Integer.MAX_VALUE;

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
                //原先代码
//                graph.serverIds.add(i);

                //改
                networkVertex = graph.getNetworkVertices().get(i);
                graph.serverIds.put(networkVertex.id, true);
            }
        }

        //使用dijkstra算法求得最小路径
        FastPQDijkstra pqDijkstra = new FastPQDijkstra(graph);

        int sum = pqDijkstra.searchGraphPaths(graph.userAdjVertices, graph.table);
        pathList = pqDijkstra.getAllPathList();
        return sum;
    }


    public void statistics(Population[] pop){
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

            if (oldPop[i].fitness < bestCost) {
                bestCost = oldPop[i].fitness;
                bestList = pathList;
            }

            sumFitness += 1000.0/tmpFitness;
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


            for (j=0; j < Math.round(serverNum*1.2) && i < chrom_size; j++){
                if (excise() <= pro_init_server) {
                    oldPop[i].chrom[j] = 1;
                }
            }
            oldPop[i].fitness = calFit(oldPop[i].chrom);
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
            partsum += (1000.0 / oldPop[i].fitness);
            i++;
        }while((partsum < wheelPos) && i < pop_size);
        return i-1;
    }

    boolean crossOver(int[] parent1, int[] parent2, int chr1){
        int j;
        int crossPos;
        if (excise() <= pro_cross){
            crossPos = random.nextInt(chrom_size);
        }
        else {
            crossPos = chrom_size - 1;
        }

        for (j = 0; j <= crossPos; j++){
            newPop[chr1].chrom[j] = parent1[j];
//            newPop[chr2].chrom[j] = parent2[j];
        }

        for (; j < chrom_size; j++){
            newPop[chr1].chrom[j] = parent2[j];
//            newPop[chr2].chrom[j] = parent1[j];
        }

        return true;
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

        if (excise() <= 0.5) {
            for (j = 0; j < xnor; ++j) {
                newPop[i].chrom[j] = parent1[j] == parent2[j] ? parent1[j] : 0;
            }
            for (; j < chrom_size; ++j) {
                newPop[i].chrom[j] = parent1[j];
            }
        }else {
            for (j = 0; j < xnor; ++j) {
                newPop[i].chrom[j] = parent1[j];
            }
            for (; j < chrom_size; ++j) {
                newPop[i].chrom[j] = parent1[j] == parent2[j] ? parent1[j] : 0;
            }
        }

    }

    void generation(){
        int mate1,mate2,mate3;

        for (int i=0; i < pop_size; ++i){

            if (i == minPop){
                for (int j=0; j < chrom_size; ++j) {
                    newPop[i].chrom[j] = oldPop[i].chrom[j];
                    newPop[i].fitness = oldPop[i].fitness;
                }
            }
            else {
                mate1 = selection();
                mate2 = selection();
                mate3 = selection();
                crossOver(oldPop[mate1].chrom, oldPop[mate2].chrom, i);
                xnor(oldPop[mate1].chrom, oldPop[mate3].chrom, i);
                for (int j = 0; j < chrom_size; ++j) {
                    newPop[i].chrom[j] = mutation(newPop[i].chrom[j]);
                }
                newPop[i].fitness = calFit(newPop[i].chrom);
            }


        }
    }

    public void startGA(){
        int gen = 0;
        random = new Random(System.currentTimeMillis());
        initPop();
        statistics(oldPop);
        while (gen < generation_num){
            ++gen;
            if (gen % 3 ==0 ) {
                graph.shuffleUseradjVertice(graph.userAdjVertices);
            }
            if (gen % 100 == 0)
                random = new Random(System.currentTimeMillis());


            generation();
            statistics(newPop);

            for (int i=0; i < pop_size; ++i){
                for (int j=0; j < chrom_size; ++j){
                    oldPop[i].chrom[j] = newPop[i].chrom[j];
                }
                oldPop[i].fitness = newPop[i].fitness;
            }
        }


    }

    public class Population{
        // 基因组
        int chrom[];

        // 适应度
        int fitness;



        public Population(int size){
            chrom = new int[size];
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
