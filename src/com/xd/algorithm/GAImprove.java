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
//public class GAImprove {
//    // 种群的规模
//    private int entity_size;
//
//    // 染色体长度
//    private int chrom_size;
//
//    // 繁殖代数
//    private int generation_num;
//
//
//
//    private Random random;
//
//    public Population firstPop, secondPop;
//
//    private Graph graph;
//
//    private GraphProcess graphProcess;
//
//    private List<List>  bestList;
//
//    public HashMap<Integer, Boolean> bestServer = new HashMap<>();
//
//    private int bestCost = Integer.MAX_VALUE;
//
//    private int bestId;
//
//    private int maxServerNum;
//
//    private int isMixed;
//
//
//
//
//    public GAImprove(int entity_size, int chrom_size, int generation_num, GraphProcess graphProcess){
//        this.entity_size = entity_size;
//        this.chrom_size = chrom_size;
//        this.generation_num = generation_num;
//
//        this.graphProcess = graphProcess;
//        this.graph = graphProcess.getGraph();
//        this.maxServerNum = graphProcess.getGraph().getUserVertexs().size();
//
//    }
//
//    void initPop(){
//
//        firstPop = new Population();
//        secondPop = new Population();
//
//        firstPop.pc0 = 0.3;
//        firstPop.pc1 = 0.6;
//        firstPop.pc2 = 0.9;
//        firstPop.pm0 = 0.005;
//        firstPop.pm1 = 0.02;
//        firstPop.pm2 = 0.1;
//
//        secondPop.pc0 = 0.2;
//        secondPop.pc1 = 0.4;
//        secondPop.pc2 = 0.6;
//        secondPop.pm0 = 0.01;
//        secondPop.pm1 = 0.05;
//        secondPop.pm2 = 0.08;
//
//        initEntity(firstPop, 0);
//        initEntity(secondPop, 0);
//
//    }
//
//    void initEntity(Population pop, int gen){
//
//
//
//        pop.oldEntity = new Entity[entity_size];
//        pop.newEntity = new Entity[entity_size];
//
//        Entity entity1, entity2;
//
//        for (int i=0; i < entity_size; ++i){
//            entity1 = new Entity(chrom_size);
//            entity2 = new Entity(chrom_size);
//
//            pop.oldEntity[i] = entity1;
//            pop.newEntity[i] = entity2;
//
//            for (int j=0; j < chrom_size; ++j) {
//                if (excise() < (0.3)) {
//                    pop.oldEntity[i].chrom[j] = 1;
//                }
//            }
//
//            pop.oldEntity[i].maxServerNum++;
//
//            calFit(pop.oldEntity[i], 0);
//        }
//    }
//
//
//
//    public void calFit(Entity entity, int gen){
//
//        double usageRate;
//
//        graphProcess.addEdgesOfVertex();
//        graph.serverIds.clear();
//
//        NetworkVertex networkVertex = null;
//        for (int i=0; i < entity.chrom.length; ++i){
//            if (entity.chrom[i] == 1) {
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
//        entity.cost = pqDijkstra.searchGraphPaths(graph.userAdjVertices, graph.table);
//        List pathList = pqDijkstra.getAllPathList();
//
//        int endServerNum = graph.serverIds.size()+1;
//
////        usageRate = endServerNum  *1.0/ startServerNum;
//        usageRate = 1.0;
//
//        entity.fitness = entity.cost * usageRate;
//        compareCost(entity.cost, pathList, gen);
//    }
//
//
//    public void crossoverTwoPopulation(Population firstPop, Population secondPop){
//        int j;
//        int crossPos1, crossPos2;
//
//        crossPos1 = random.nextInt(chrom_size);
//        while ((crossPos2 = random.nextInt(chrom_size))==crossPos1){
//
//        }
//        if (crossPos1 > crossPos2){
//            int temp = crossPos1;
//            crossPos1 = crossPos2;
//            crossPos2 = temp;
//        }
//
//        for (j=0; j < crossPos1; ++j){
//            firstPop.oldEntity[ firstPop.maxEntity ].chrom[j] = firstPop.oldEntity[ firstPop.minEntity ].chrom[j];
//            secondPop.oldEntity[ secondPop.maxEntity ].chrom[j] = secondPop.oldEntity[ secondPop.minEntity ].chrom[j];
//        }
//
//        for (j = crossPos1; j < crossPos2; ++j){
//            firstPop.oldEntity[ firstPop.maxEntity ].chrom[j] = secondPop.oldEntity[ secondPop.minEntity ].chrom[j];
//            secondPop.oldEntity[ secondPop.maxEntity ].chrom[j] = firstPop.oldEntity[ secondPop.minEntity ].chrom[j];
//        }
//
//        for (; j < chrom_size; ++j){
//            firstPop.oldEntity[ firstPop.maxEntity ].chrom[j] = firstPop.oldEntity[ firstPop.minEntity ].chrom[j];
//            secondPop.oldEntity[ secondPop.maxEntity ].chrom[j] = secondPop.oldEntity[ secondPop.minEntity ].chrom[j];
//        }
//
//        statistics(firstPop);
//        statistics(secondPop);
//
//    }
//
//
//    public void statistics(Population pop) {
//        double tmpFitness;
//        pop.maxFitness = 0;
//        pop.minFitness = Double.MAX_VALUE;
//
//
//        for (int i = 0; i < entity_size; ++i) {
//            tmpFitness = pop.oldEntity[i].fitness;
//
//
//            if (tmpFitness > pop.maxFitness) {
//                pop.maxFitness = tmpFitness;
//                pop.maxEntity = i;
//            }
//
//
//            if (tmpFitness < pop.minFitness) {
//                pop.minFitness = tmpFitness;
//                pop.minEntity = i;
//            }
//
////            System.out.println(pop.oldEntity[i]);
//
//        }
//        adapt(pop);
//    }
//
//    public void adapt(Population pop){
//        pop.sumFitness = 0;
//        double sumFitness1 = 0;
//
//        for (int i=0; i < entity_size; ++i){
//            pop.sumFitness += 1- pop.oldEntity[i].fitness /(pop.maxFitness + pop.minFitness);
//            sumFitness1 += pop.oldEntity[i].fitness;
//        }
//
//        pop.avgFitness = sumFitness1 / entity_size;
//
//
//        double pc0 = pop.pc0, pc1 = pop.pc1, pc2 = pop.pc2, para1 = 0.78, para2 = 0.0005;
//        // 自适应交叉率
//        if (pop.minFitness / pop.avgFitness > para1 && pop.minFitness / pop.maxFitness >= pc1 / pc2){
//            pop.pro_cross = pc0;
//
//        }
//        else if (pop.minFitness / pop.avgFitness > para1 && pop.minFitness / pop.maxFitness > para2 && pop.minFitness / pop.maxFitness < pc1 / pc2){
//            pop.pro_cross = pc2 - (pc1 - pc2) / (1 - pop.minFitness / pop.maxFitness);
//        }
//        else {
//            pop.pro_cross = pc2;
//        }
//
//        //自适应变异率
//        double pm0 = pop.pm0,pm1 = pop.pm1, pm2 = pop.pm2;
//        if (pop.minFitness / pop.avgFitness > para1 && pop.minFitness / pop.maxFitness >= pm1 / pm2){
//            pop.pro_mutate = pm2;
//        }
//        else if (pop.minFitness / pop.avgFitness > para1 && pop.minFitness / pop.maxFitness > para2 && pop.minFitness / pop.maxFitness < pm1 / pm2){
//            pop.pro_mutate = pm2 - (pm2 - pm1) / (1 - pop.minFitness / pop.maxFitness);
//        }
//        else {
//            pop.pro_mutate = pm0;
//        }
//
//    }
//
//
//    double excise(){
//        double pp;
//        pp = random.nextDouble();
//        return pp;
//    }
//
//
//    int selection(Population pop){
//        double wheelPos, randNumber, partsum = 0;
//        int i = 0;
//
//        randNumber = random.nextDouble();
//
//        wheelPos = randNumber*pop.sumFitness;
//        do {
//            partsum += ( 1 - pop.oldEntity[i].fitness / (pop.minFitness + pop.maxFitness));
//            i++;
//        }while((partsum < wheelPos) && i < entity_size);
//
//        return i-1;
//    }
//
//
//
//    boolean crossOver(Population pop, int parent1, int parent2, int chr1, int chr2){
//        int j;
//        int crossPos1, crossPos2;
//        if (excise() <= pop.pro_cross){
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
//                pop.newEntity[chr1].chrom[j] = pop.oldEntity[parent1].chrom[j];
//                pop.newEntity[chr2].chrom[j] = pop.oldEntity[parent2].chrom[j];
//            }
//
//            for (; j < crossPos2; j++){
//                pop.newEntity[chr1].chrom[j] = pop.oldEntity[parent2].chrom[j];
//                pop.newEntity[chr2].chrom[j] = pop.oldEntity[parent1].chrom[j];
//            }
//            for (; j < chrom_size; j++){
//                pop.newEntity[chr1].chrom[j] = pop.oldEntity[parent1].chrom[j];
//                pop.newEntity[chr2].chrom[j] = pop.oldEntity[parent2].chrom[j];
//            }
//
//            return true;
//        }
//
//        return false;
//
//    }
//
//    int mutation(Population pop, int alleles){
//        double pp = excise();
//
//        if (pp <= pop.pro_mutate) {
//            return 1 - alleles;
//        }
//        return alleles;
//
//    }
//
//
//    public void compareCost(int cost, List pathList,int gen){
//        if (cost < bestCost){
//            bestCost = cost;
//            bestList = pathList;
//            bestId = gen;
//
//            bestServer.clear();
//            for (Integer id:graph.serverIds.keySet()){
//                bestServer.put(id, true);
//            }
//        }
//    }
//
//    void generation(Population pop, int gen){
//        int mate1,mate2;
//
//
//        for (int i=1; i < entity_size; i = i+2){
//
//            mate1 = selection(pop);
//            mate2 = selection(pop);
//
//            crossOver(pop, mate1, mate2,i, i+1);
//            for (int j = 0; j < chrom_size; ++j) {
//                pop.newEntity[i].chrom[j] = mutation(pop, pop.newEntity[i].chrom[j]);
//                pop.newEntity[i+1].chrom[j] = mutation(pop, pop.newEntity[i+1].chrom[j]);
//            }
//            calFit(pop.newEntity[i], gen);
//
//            calFit(pop.newEntity[i+1], gen);
//        }
//
//
//        for (int j=0; j < chrom_size; ++j){
//            pop.newEntity[0].chrom[j] = pop.oldEntity[ pop.minEntity ].chrom[j];
//        }
//        pop.newEntity[0].cost = pop.oldEntity[ pop.minEntity ].cost;
//        pop.newEntity[0].fitness = pop.oldEntity[ pop.minEntity ].fitness;
//
//    }
//
//    public void copyToNewEntity(Population pop){
//        for (int i=0; i < entity_size; ++i){
//            for (int j=0; j < chrom_size; ++j){
//                pop.oldEntity[i].chrom[j] = pop.newEntity[i].chrom[j];
//            }
//            pop.oldEntity[i].cost = pop.newEntity[i].cost;
//            pop.oldEntity[i].fitness = pop.newEntity[i].fitness;
//        }
//    }
//
//    public void startGA(){
//
//        long start = System.currentTimeMillis();
//        long end;
//
//        random = new Random(System.currentTimeMillis());
//        int gen = 0;
//
//        initPop();
//
//        statistics(firstPop);
//        statistics(secondPop);
//
//
//        while (gen < generation_num){
//
//            end = System.currentTimeMillis();
//
//            if (end - start > 80*1000){
//                break;
//            }
//
//            ++gen;
//
//            if (gen % 3 == 0){
//                graph.shuffleUseradjVertice(graph.userAdjVertices);
//            }
//
//            if (gen % 150 == 0){
//                crossoverTwoPopulation(firstPop, secondPop);
//            }
//
//            generation(firstPop, gen);
//            copyToNewEntity(firstPop);
//            statistics(firstPop);
//
//
//            generation(secondPop, gen);
//            copyToNewEntity(secondPop);
//            statistics(secondPop);
//
//        }
//
//
//    }
//
//    public class Population{
//        public Entity[] oldEntity;
//        public Entity[] newEntity;
//
//        int minEntity;
//        int maxEntity;
//
//        double pc0;
//        double pc1;
//        double pc2;
//
//        double pm0;
//        double pm1;
//        double pm2;
//
//        double pro_cross;
//        double pro_mutate;
//
//        double minFitness;
//        double maxFitness;
//
//        double sumFitness;
//        double avgFitness;
//
//    }
//
//    public class Entity{
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
//        public Entity(int size){
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
