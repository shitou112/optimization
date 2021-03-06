package test;

import com.filetool.util.FileUtil;
import com.xd.algorithm.*;
import com.xd.data.GraphProcess;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;
import com.xd.myutils.StringsUtils;
import org.junit.Test;

import java.util.List;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class App {
    String FILEPATH = "E:\\case_example\\mycase.txt";
    Graph graph = StringsUtils.readStrings(FileUtil.read(FILEPATH,null));

//    @Test
//    public void PQTest(){
//        GraphProcess graphProcess = new GraphProcess(graph);
//        graphProcess.updateGraph();
//
//        PQDijkstra pq = new PQDijkstra(graph);
//        graph.serverIds.put(0,true);
//        graph.serverIds.put(3,true);
//        graph.serverIds.put(22,true);
//
//        pq.initPath(graph.userAdjVertices, graph.table);
//        int cost = pq.startPQDijkstra(graph.userAdjVertices, graph.serverIds);
//        System.out.println(cost);
//    }
//
//    @Test
//    public void ABParameterTest(){
//
//        int bestcost = Integer.MAX_VALUE;
//
//        double bestA=0, bestB=0;
//
//        double[] A = {0.9,0.7,0.5,0.3,0.1};
//        double[] B = {0.1,0.3,0.5,0.7,0.9};
//        GA ga = null;
//        for (int i=0; i < A.length; ++i){
//            for (int j=0; j < B.length; ++j){
//                GraphProcess graphProcess = new GraphProcess(graph);
//
//                graphProcess.A = A[i];
//                graphProcess.B = B[j];
//                graphProcess.updateGraph();
////                ga = new GA(30, 0.8,  0.6,     0.1,      0.1,        0.7, graph.aliveNetVerticesNum, 200, graphProcess);
//                ga.startGA();
//                if (ga.getBestCost() < bestcost) {
//                    bestcost = ga.getBestCost();
//                    bestA = A[i];
//                    bestB = B[j];
//                    System.out.println(A[i]+"---"+B[j]+"----"+bestcost);
//                }
//
//            }
//        }
//        System.out.println(bestA);
//        System.out.println(bestB);
//    }
//
//    @Test
//    public void gaTest(){
//        int bestcost = Integer.MAX_VALUE;
//        double best_pro_corss =0,best_pro_mutution=0, best_pro_better=0,
//                best_xnor=0, best_init_server=0;
//
//        GraphProcess graphProcess = new GraphProcess(graph);
//        graphProcess.updateGraph();
//        double[] pro_cross = {0.9,0.8,0.7};
//        double[] pro_mutution = {0.6,0.5,0.4};
//        double[] pro_better_mutution = {0.1};
//        double[] pro_xnor = {0.5,0.6,0.7};
//        double[] pro_init_server = {0.2,0.1};
//        GA ga = null;
//        for (int i=0; i < pro_cross.length; ++i){
//            for (int j=0; j < pro_mutution.length; ++j){
//                for (int m=0; m < pro_better_mutution.length;++m){
//                    for (int n=0; n < pro_init_server.length; ++n){
//                        for (int k=0; k < pro_xnor.length; ++k){
////                            ga =new GA(30, pro_cross[i], pro_mutution[j], pro_better_mutution[m], pro_init_server[n], pro_xnor[k],graph.aliveNetVerticesNum, 200, graphProcess);
//                            ga.startGA();
//                            if (ga.getBestCost() < bestcost){
//                                bestcost = ga.getBestCost();
//                                best_pro_mutution = pro_mutution[j];
//                                best_pro_corss = pro_cross[i];
//                                best_pro_better = pro_better_mutution[m];
//                                best_init_server = pro_init_server[n];
//                                best_xnor = pro_xnor[k];
//
//                            }
//                        }
//
//                    }
//                }
//
//            }
//        }
//
//        System.out.println(best_pro_corss);
//        System.out.println(best_pro_mutution);
//        System.out.println(best_pro_better);
//        System.out.println(best_init_server);
//        System.out.println(best_xnor);
//        System.out.println("=====");
//        System.out.println(ga.getBestCost());
//        System.out.println(ga.getBestId());
//    }
//
//    @Test
//    public void graphCloneTest() throws CloneNotSupportedException {
//        GraphProcess graphProcess = new GraphProcess(graph);
//        graphProcess.updateGraph();
//
////
////        graph.serverIds.add(0);
////        graph.serverIds.add(3);
//
//        PQDijkstra pqDijkstra = new PQDijkstra(graph);
//        pqDijkstra.searchGraphPaths(graph.userAdjVertices, graph.table);
//
//
//        for (int i=0; i < graph.table.length; ++i){
//            if (graph.table[i]!=null) {
//                for(Integer id: graph.table[i].keySet()){
//                    System.out.print(i+"---id---"+id+"---"+graph.table[i].get(id)+":::");
//                }
//            }
//            System.out.println();
//        }
//
//        System.out.println("======================");
////        graphProcess.addEdgesOfVertex();
////        for (int i=0; i < graph.table.length; ++i){
////            if (graph.table[i]!=null) {
////                for(Integer id: graph.table[i].keySet()){
////                    System.out.print(i+"---"+id+"---"+graph.table[i].get(id)+":::");
////                }
////            }
////            System.out.println();
////        }
//
//    }
//
    /**
     * 注意此测试中的寻找路径为空需要后续处理，例如添加服务器节点
     */
    @Test
    public void searchPathTest(){
        long start = System.currentTimeMillis();

       GraphProcess graphProcess = new GraphProcess(graph);
        graphProcess.updateGraph();
        GA ga = new GA(31, graph.aliveNetVerticesNum, 500,graphProcess);
        for (int i=0; i < graph.table.length; ++i){
            System.out.println(i);
            if (graph.table[i] != null) {
                System.out.print(i+"::: ");
                for (Integer id : graph.table[i].keySet()) {
                    System.out.print("id号"+id+"--edge-"+graph.table[i].get(id) + "----");
                }
                System.out.println();
            }
        }

        System.out.println("=====");
        graph.serverIds.put(0, true);

        graph.serverIds.put(22, true);
        graph.serverIds.put(3, true);

//        ga.addSuperSource(graph.serverIds);
//        graph.shuffleUseradjVertice(graph.userAdjVertices);
        PathCost pqDijkstra = new PathCost(graph);

//        graph.shuffleUseradjVertice(graph.userAdjVertices);
//        for (NetworkVertex networkVertex:graph.userAdjVertices){
//            System.out.print(networkVertex.id+" ");
//        }
//        System.out.println();

//        pqDijkstra.initPath(graph.userAdjVertices, graph.table);
//        int cost = pqDijkstra.startPQDijkstra(graph.userAdjVertices, graph.serverIds);

        int cost = pqDijkstra.minPathCost(graph.table);

//        for (int i=0; i < pqDijkstra.vertexCost.length; ++i){
//            System.out.println(i+"---"+pqDijkstra.vertexCost[i]);
//        }

//        System.out.println("=====");
//
//        for (int i=0; i < graph.table.length; ++i){
//            if (graph.table[i] != null) {
//                System.out.print(i+"::: ");
//                for (Integer id : graph.table[i].keySet()) {
//                    System.out.print(graph.table[i].get(id) + "----");
//                }
//                System.out.println();
//            }
//        }
        System.out.println("===");
        for (List<Integer> list: pqDijkstra.getAllPathList()){
            for (Integer id: list){
                System.out.print(id+" ");
            }
            System.out.println();
        }
//        for (Integer id: graph.serverIds.keySet()){
//            System.out.print(id+" ");
//        }
//        System.out.println();
//        long end = System.currentTimeMillis();

//
        System.out.println(cost);




    }

//    @Test
//    public void deleteUselessVertexTest(){
//        GraphProcess graphProcess = new GraphProcess(graph);
//        graphProcess.updateGraph();
//
//
//    }
//
//    @Test
//    public void createVerticsTableTest(){
////        GraphProcess graphProcess = new GraphProcess(graph);
////        graphProcess.updateGraph();
////        for (int i=0; i < graph.networkVertexnum; ++i){
////            for (int j=0; j < graph.networkVertexnum; ++j){
////                System.out.println(i+"---"+graph.adj[i][j]);
////            }
////        }
//    }
//
//
//    @Test
//    public void dataStatisticTest(){
//        GraphProcess graphProcess = new GraphProcess(graph);
//        graphProcess.updateGraph();
////        for (NetworkVertex list:graph.getNetworkVertices())
////            System.out.println(list);
//
//    }
//
//    @Test
//    public void userAdjVertices(){
//        for (NetworkVertex networkVertex: graph.userAdjVertices)
//            System.out.println(networkVertex);
//    }
//    @Test
//    public void filereadTest(){
//        System.out.println(graph.networkVertexnum+" "+ graph.userVertexnums+" "+ graph.edgenum+" "+ graph.serverValue);
//        List edgeList = graph.getEdges();
//        List vertexList = graph.getNetworkVertices();
//        List usertexList = graph.getUserVertexs();
//        for (int i = 0; i< graph.edgenum; ++i){
//            System.out.println(edgeList.get(i));
//        }
//        System.out.println();
//        for (int i = 0; i< graph.networkVertexnum; ++i){
//            System.out.println(vertexList.get(i));
//        }
//        System.out.println();
//        for (int i = 0; i< graph.userVertexnums; ++i){
//            System.out.println(usertexList.get(i));
//        }
//    }
}
