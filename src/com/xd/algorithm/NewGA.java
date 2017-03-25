//package com.xd.algorithm;
//
//import com.xd.data.GraphProcess;
//import com.xd.graph.Graph;
//import com.xd.graph.NetworkVertex;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Random;
//
///**
// * @author Qian Shaofeng
// * @created on 2017/3/11.
// */
//public class NewGA {
//    // 种群的规模
//    private int pop_size;
//
//    // 交叉概率
//    private double pro_cross;
//
//    // 变异概率
//    private double pro_mutate;
//
//    private double pro_better_mutate;
//
//    //同或概率
//    private double pro_xnor;
//
//    // 染色体长度
//    private int chrom_size;
//
//    // 繁殖代数
//    private int generation_num;
//
//
//    private double sumFitness, avgFitness, usageRate, minFitness = Double.MAX_VALUE, maxFitness = Double.MAX_VALUE;
//
//
//    private double pro_init_server;
//
//    private int minPop, maxPop;
//
//    private Random random;
//
//    public Population[] oldPop, newPop;
//
//    private Graph graph;
//
//    private GraphProcess graphProcess;
//
//    private List<List> pathList, bestList;
//
//    public HashMap<Integer, Boolean> bestServer = new HashMap<>();
//
//    private int bestCost = Integer.MAX_VALUE;
//
//    private int bestId;
//
//    private int serverNum;
//
//    PQDijkstra pqDijkstra;
//
//
//
//    public NewGA(int pop_size, int chrom_size, int generation_num, GraphProcess graphProcess){
//        this.pop_size = pop_size;
//        this.chrom_size = chrom_size;
//        this.generation_num = generation_num;
//        this.oldPop = new Population[pop_size];
//        this.newPop = new Population[pop_size];
//        this.graphProcess = graphProcess;
//        this.graph = graphProcess.getGraph();
//        this.serverNum = graphProcess.getGraph().getUserVertexs().size();
//        pqDijkstra = new PQDijkstra(graph);
//        pqDijkstra.initPath(graph.userAdjVertices, graph.table);
//    }
//
//
//    public int fixCalFit(int[] chr){
//        graphProcess.addEdgesOfVertex();
//        graph.serverIds.clear();
//        NetworkVertex networkVertex = null;
//        for (int i=0; i < chr.length; ++i){
//            if (chr[i] == 1) {
//                networkVertex = graph.getNetworkVertices().get(i);
//                graph.serverIds.put(networkVertex.id, true);
//            }
//        }
//
//        int startServerNum = graph.serverIds.size()+1;
//
//        //使用dijkstra算法求得最小路径
//        FastPQDijkstra pqDijkstra = new FastPQDijkstra(graph);
//
//        int sum = pqDijkstra.searchGraphPaths(graph.userAdjVertices, graph.table);
//        pathList = pqDijkstra.getAllPathList();
//
//        int endServerNum = graph.serverIds.size()+1;
//
////        usageRate = endServerNum  *1.0/ startServerNum;
//
//        usageRate = 1.0;
//        return sum;
//    }
//
//    public int calFit(int[] chr){
//        graph.serverIds.clear();
//        NetworkVertex networkVertex = null;
//        for (int i=0; i < chr.length; ++i){
//            if (chr[i] == 1) {
//                networkVertex = graph.getNetworkVertices().get(i);
//                graph.serverIds.put(networkVertex.id, true);
//            }
//        }
//
//        int startServerNum = graph.serverIds.size()+1;
//
//
//        int sum = pqDijkstra.startPQDijkstra(graph.userAdjVertices, graph.serverIds);
////        pathList = pqDijkstra.getAllPathList();
//
//        int endServerNum = graph.serverIds.size()+1;
//
//        usageRate = endServerNum  *1.0/ startServerNum;
////        System.out.println(sum);
//        return sum;
//    }
//
//    public double getUsageRate(){
//        return  usageRate;
//    }
//
//
//    public void statistics(Population[] pop, int gen){
//        double tmpFitness;
//        double sumFitness1=0;
//        sumFitness = 0;
//        maxFitness = 0;
//        minFitness = Double.MAX_VALUE;
//
//        for (int i=0; i < pop_size; ++i){
//            tmpFitness = pop[i].fitness;
//
//            if (tmpFitness > maxFitness){
//                maxFitness = tmpFitness;
//                maxPop = i;
//            }
//
//
//            if (tmpFitness < minFitness){
//                minFitness = tmpFitness;
//                minPop = i;
//            }
//
//        }
//
//        pop[minPop].cost = fixCalFit(pop[minPop].chrom);
//        pop[minPop].fitness = pop[minPop].cost * getUsageRate();
//
////        System.out.println(pop[minPop]);
//
//        for (int i=0; i < pop_size; ++i){
//            sumFitness += 1- pop[i].fitness /(maxFitness + minFitness);
//            sumFitness1 += pop[i].fitness;
//        }
//
//        avgFitness = sumFitness1 / pop_size;
//
//
//        double pc0 = 0.3, pc1 = 0.6, pc2 = 0.9, para1 = 0.78, para2 = 0.0005;
//        // 自适应交叉率
//        if (minFitness/avgFitness > para1 && minFitness / maxFitness >= pc1 / pc2){
//            pro_cross = pc0;
//
//        }
//        else if (minFitness / avgFitness > para1 && minFitness / maxFitness > para2 && minFitness / maxFitness < pc1 / pc2){
//            pro_cross = pc2 - (pc1 - pc2) / (1 - minFitness/maxFitness);
//        }
//        else {
//            pro_cross = pc2;
//        }
//
//        //自适应变异率
//        double pm0 = 0.0001,pm1 = 0.001, pm2 = 0.1;
//        if (minFitness/avgFitness > para1 && minFitness / maxFitness >= pm1 / pm2){
//            pro_mutate = pm2;
//        }
//        else if (minFitness / avgFitness > para1 && minFitness / maxFitness > para2 && minFitness / maxFitness < pm1 / pm2){
//            pro_mutate = pm2 - (pm2 - pm1) / (1 - minFitness/maxFitness);
//        }
//        else {
//            pro_mutate = pm0;
//        }
//
//    }
//
//
//    void initPop(){
//        int i,j=0;
//        oldPop = new Population[pop_size];
//        newPop = new Population[pop_size];
//        Population population1,population2 ;
//        for (i=0; i < pop_size; ++i){
//            population1 = new Population(chrom_size);
//            population2 = new Population(chrom_size);
//            oldPop[i] = population1;
//            newPop[i] = population2;
//
//            for (j=0; j < chrom_size; ++j) {
//                if (excise() < graph.userVertexnums*0.6/graph.aliveNetVerticesNum) {
//                    oldPop[i].chrom[j] = 1;
//                }
//            }
////            j = random.nextInt(chrom_size);
////            oldPop[i].chrom[j] = 1;
//
//            oldPop[i].maxServerNum++;
//
//            oldPop[i].cost = calFit(oldPop[i].chrom);
//            if (oldPop[i].cost < bestCost) {
//                bestCost = oldPop[i].cost;
//                bestList = pathList;
//
//                bestServer.clear();
//                for (Integer id:graph.serverIds.keySet()){
//                    bestServer.put(id, true);
//                }
//            }
//            oldPop[i].fitness = oldPop[i].cost * getUsageRate();
//
//        }
//    }
//
//    double excise(){
//        double pp;
//        pp = random.nextDouble();
//        return pp;
//    }
//
//    int selection(){
//        double wheelPos, randNumber, partsum = 0;
//        int i = 0;
//
//        randNumber = random.nextDouble();
//
//        wheelPos = randNumber*sumFitness;
//        do {
//            partsum += ( 1 - oldPop[i].fitness / (minFitness + maxFitness));
//            i++;
//        }while((partsum < wheelPos) && i < pop_size);
//        return i-1;
//    }
//
//    boolean crossOver(int[] parent1, int[] parent2, int chr1, int chr2){
//        int j;
//        int crossPos1, crossPos2;
//        if (excise() <= pro_cross){
//            crossPos1 = random.nextInt(chrom_size);
//            while ((crossPos2 = random.nextInt(chrom_size))==crossPos1){
//
//            }
//            if (crossPos1 > crossPos2){
//                int temp = crossPos1;
//                crossPos1 = crossPos2;
//                crossPos2 = temp;
//            }
//
//            for (j = 0; j <= crossPos1; j++){
//                newPop[chr1].chrom[j] = parent1[j];
//                newPop[chr2].chrom[j] = parent2[j];
//            }
//
//            for (; j < crossPos2; j++){
//                newPop[chr1].chrom[j] = parent2[j];
//                newPop[chr2].chrom[j] = parent1[j];
//            }
//            for (; j < chrom_size; j++){
//                newPop[chr1].chrom[j] = parent1[j];
//                newPop[chr2].chrom[j] = parent2[j];
//            }
//            return true;
//        }
//        return false;
//
//    }
//
//    int mutation( int alleles){
//        double pp = excise();
//
//        if (pp <= pro_mutate) {
//            return 1 - alleles;
//        }
//        return alleles;
//
//    }
//
//
//    void generation(int gen){
//        int mate1,mate2;
//
//
//
//        for (int i=1; i < pop_size; i = i+2){
//
//            mate1 = selection();
//            mate2 = selection();
//            crossOver(oldPop[mate1].chrom, oldPop[mate2].chrom, i, i+1);
//            for (int j = 0; j < chrom_size; ++j) {
//                newPop[i].chrom[j] = mutation( newPop[i].chrom[j]);
//                newPop[i+1].chrom[j] = mutation( newPop[i+1].chrom[j]);
//            }
//            newPop[i].cost = calFit(newPop[i].chrom);
//            newPop[i].fitness = newPop[i].cost * getUsageRate();
//            if (newPop[i].cost < bestCost) {
//                bestCost = newPop[i].cost;
//                bestList = getBestList();
//                bestId = gen;
//
//                bestServer.clear();
//                for (Integer id:graph.serverIds.keySet()){
//                    bestServer.put(id, true);
//                }
//            }
//
//            newPop[i+1].cost = calFit(newPop[i+1].chrom);
//            newPop[i+1].fitness = newPop[i+1].cost * getUsageRate();
//
//            if (newPop[i+1].cost < bestCost){
//                bestCost = newPop[i+1].cost;
//                bestList = pathList;
//                bestId = gen;
//
//                bestServer.clear();
//                for (Integer id:graph.serverIds.keySet()){
//                    bestServer.put(id, true);
//                }
//            }
//        }
//
//
//        for (int j=0; j < chrom_size; ++j){
//            newPop[0].chrom[j] = oldPop[minPop].chrom[j];
//        }
//        newPop[0].cost = oldPop[minPop].cost;
//        newPop[0].fitness = oldPop[minPop].fitness;
//
//    }
//
//    public void startGA(){
//        random = new Random(System.currentTimeMillis());
//        int gen = 0;
//        initPop();
//
//        statistics(oldPop, gen);
//        while (gen < generation_num){
//            ++gen;
//
////            if (gen % 3 ==0 ) {
////                graph.shuffleUseradjVertice(graph.userAdjVertices);
////            }
//
//            generation(gen);
//            statistics(newPop, gen);
//
//            for (int i=0; i < pop_size; ++i){
//                for (int j=0; j < chrom_size; ++j){
//                    oldPop[i].chrom[j] = newPop[i].chrom[j];
//                }
//                oldPop[i].cost = newPop[i].cost;
//                oldPop[i].fitness = newPop[i].fitness;
//            }
//        }
//
//
//    }
//
//    public class Population{
//        // 基因组
//        public int chrom[];
//
//        // 花费
//        int cost;
//
//        int maxServerNum;
//
//        double fitness;
//
//        public int[] mutateStates;
//
//
//        public Population(int size){
//            chrom = new int[size];
//            mutateStates = new int[size];
//        }
//
//        @Override
//        public String toString() {
//            String str = "";
//            for (int i =0; i<chrom_size;++i){
//                str+=chrom[i]+" ";
//            }
//            str += cost+" fit "+ fitness;
//            return str;
//        }
//    }
//
//    public int getBestCost() {
//        return bestCost;
//    }
//
//    public List<List> getBestList() {
//        return bestList;
//    }
//
//    public int getBestId() {
//        return bestId;
//    }
//}
