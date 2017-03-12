package com.xd.algorithm;

import com.xd.data.GraphProcess;
import com.xd.graph.Graph;

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

    // 染色体长度
    private int chrom_size;

    // 繁殖代数
    private int generation_num;

    private int sumFitness, minFitness, maxFitness, avgFitness;

    private double alpha;

    private int minPop, maxPop;

    private Random random;

    private Population[] oldPop, newPop;

    private Graph graph;

    private GraphProcess graphProcess;

    private List<List> pathList, bestList;

    private int bestCost = Integer.MAX_VALUE;

    private int bestId;

    private int serverNum;



    public GA(int pop_size, double pro_cross, double pro_mutate, int chrom_size,
              int generation_num, GraphProcess graphProcess){
        this.pop_size = pop_size;
        this.pro_cross = pro_cross;
        this.pro_mutate = pro_mutate;
        this.chrom_size = chrom_size;
        this.generation_num = generation_num;
        this.oldPop = new Population[pop_size];
        this.newPop = new Population[pop_size];
        this.random = new Random();
        this.graphProcess = graphProcess;
        this.serverNum = graphProcess.getGraph().getUserVertexs().size();
    }


    public int calServerNum(int[] chr){
        int num=0;
        for (int i=0; i < chr.length; ++i){
            if (chr[i] == 1){
                ++num;
            }
        }
        return num;
    }

    public int calFit(int[] chr){
        graphProcess.addEdgesOfVertex();
        graph = graphProcess.getGraph();
        graph.serverIds.clear();
        for (int i=0; i < chr.length; ++i){
            if (chr[i] == 1)
                graph.serverIds.add(i);
        }

        //使用dijkstra算法求得最小路径
        PQDijkstraImprove pqDijkstra = new PQDijkstraImprove(graph, 1000);

        int sum = pqDijkstra.searchGraphPaths(graph.userAdjVertices, graph.table);
        pathList = pqDijkstra.getAllPathList();
        return sum;
    }


    public void statistics(Population[] pop){
        double tmpFitness;
        int i=0;

        for (; i < pop_size; ++i){
            sumFitness += pop[i].fitness;
            tmpFitness = pop[i].fitness;

            if (tmpFitness > maxFitness){
                maxFitness = pop[i].fitness;
                maxPop = i;
            }

            if (tmpFitness < minFitness){
                minFitness = pop[i].fitness;
                minPop = i;
            }
        }
//        for (i=0; i < pop_size && pop[i].fitness != Integer.MAX_VALUE; ++i){
//            sumFitness += (maxFitness - pop[i].fitness);
//
//        }
//        avgFitness = sumFitness / pop_size;
    }


    void initPop(){
        int i,j,k;
        oldPop = new Population[pop_size];
        newPop = new Population[pop_size];
        Population population1,population2 ;
        for (i=0; i < pop_size; ++i){
            population1 = new Population(chrom_size);
            population2 = new Population(chrom_size);
            oldPop[i] = population1;
            newPop[i] = population2;


            for (j=0; j < serverNum; j++){
                k = random.nextInt(chrom_size);
                oldPop[i].chrom[k] = 1;
            }
            oldPop[i].parent1 = 0;
            oldPop[i].parent2 = 0;
            oldPop[i].cross = 0;
        }
    }

    boolean excise(double probability){
        double pp;
        pp = random.nextDouble();
        if (pp < probability)
            return true;
        return false;
    }

    int selection(int pop){
//        double wheelPos, randNumber, partsum = 0;
//        int i = 0;
//
//        randNumber = random.nextDouble();
//
//        wheelPos = randNumber*sumFitness;
//        do {
//            partsum += (maxFitness - oldPop[i].fitness);
//            i++;
//        }while((partsum < wheelPos) && i < pop_size && oldPop[i].fitness!=Integer.MAX_VALUE);
//        return i-1;
        return minPop;
    }

    boolean crossOver(int[] parent1, int[] parent2, int i){
        int j;
        int crossPos;
        if (excise(pro_cross)){
            crossPos = random.nextInt(chrom_size);
        }
        else {
            crossPos = chrom_size - 1;
        }

        for (j = 0; j <= crossPos; j++){
            newPop[i].chrom[j] = parent1[j];
        }

        for (; j < chrom_size; j++){
            newPop[i].chrom[j] = parent2[j];
        }

        newPop[i].cross = crossPos;
        return true;
    }

    int mutation(int alleles){
        if (excise(pro_mutate)){
            alleles = alleles == 0?1:0;
        }
        return alleles;
    }

    void generation(int genId){
        int i,j,mate1,mate2;
        int tmpFit, tmpServerNum;
        boolean notGen;
        for (i=0; i < pop_size; i++){
            notGen = false;
            while (!notGen){
                mate1 = selection(i);
                mate2 = selection(i+1);
                crossOver(oldPop[mate1].chrom, oldPop[mate2].chrom, i);
                for (j = 0; j < chrom_size; ++j){
                    newPop[i].chrom[j] = mutation(newPop[i].chrom[j]);
                }

                tmpServerNum = calServerNum(newPop[i].chrom);
                if (tmpServerNum <= serverNum) {
                    int changeNum = 3;
                    while ((tmpFit = calFit(newPop[i].chrom)) == Integer.MAX_VALUE && changeNum>=0){
                        graph.shuffleUseradjVertice(graph.userAdjVertices);
                        --changeNum;
                    }

                    if (tmpFit != Integer.MAX_VALUE) {
                        newPop[i].fitness = calFit(newPop[i].chrom);
                        newPop[i].parent1 = mate1;
                        newPop[i].parent2 = mate2;

                        notGen = true;

                        if (newPop[i].fitness < bestCost) {
                            bestCost = newPop[i].fitness;
                            bestList = pathList;
                            bestId = genId;
                        }
                    }
                }



            }
        }
    }

    public void startGA(){
        int gen = 0, oldMaxPop, oldMax;
        random = new Random(System.currentTimeMillis());
        initPop();
        statistics(newPop);
        while (gen < generation_num){
            ++gen;
            if (gen % 100 == 0)
                random = new Random(System.currentTimeMillis());
            oldMaxPop = maxPop;
            oldMax = maxFitness;
            generation(gen);
            statistics(newPop);

            if (maxFitness < oldMax){
                for (int k =0 ; k < chrom_size; ++k){
                    newPop[minPop].chrom[k] = oldPop[oldMaxPop].chrom[k];
                }
                newPop[minPop].fitness = oldPop[oldMaxPop].fitness;
                newPop[minPop].parent1 = oldPop[oldMaxPop].parent1;
                newPop[minPop].parent2 = oldPop[oldMaxPop].parent2;
                statistics(newPop);
            }
        }


    }

    public class Population{
        // 基因组
        int chrom[];

        // 适应度
        int fitness;

        //服务器个数
        int servernums;

        // 双亲，交叉节点
        int parent1, parent2, cross;
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
