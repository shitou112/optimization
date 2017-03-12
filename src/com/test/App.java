package com.test;

import com.filetool.util.FileUtil;
import com.xd.algorithm.PQDijkstra;
import com.xd.algorithm.PQDijkstraImprove;
import com.xd.graph.Edge;
import org.junit.Test;
import com.xd.data.ServerPoint;
import com.xd.data.GraphProcess;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;
import com.xd.myutils.StringsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class App {
    String FILEPATH = "E:\\case_example\\mycase0.txt";
    Graph graph = StringsUtils.readStrings(FileUtil.read(FILEPATH,null));

    @Test
    public void graphCloneTest() throws CloneNotSupportedException {
        GraphProcess graphProcess = new GraphProcess(graph);
        graphProcess.addEdgesOfVertex();
        graphProcess.updateGraph();



        graph.serverIds = new ArrayList<>();
        graph.serverIds.add(3);





        System.out.println(graph.serverIds.size()+"---");
        PQDijkstraImprove pqDijkstra = new PQDijkstraImprove(graph, 1000);

        int sum = pqDijkstra.searchGraphPaths(graph.userAdjVertices, graph.table);

        System.out.println();
        for (List<Integer> list:pqDijkstra.getAllPathList()){
            for (Integer integer:list){
                System.out.print(integer+" ");
            }
            System.out.println();
        }
        System.out.println(sum);
        System.out.println(graph.serverIds.size());

//        for (int i=0; i < graph.table.length; ++i){
//            if (graph.table[i]!=null) {
//                for(Integer id: graph.table[i].keySet()){
//                    System.out.print(i+"---"+id+"---"+graph.table[i].get(id)+":::");
//                }
//            }
//            System.out.println();
//        }
//
//        System.out.println("======================");
//        graphProcess.addEdgesOfVertex();
//        for (int i=0; i < graph.table.length; ++i){
//            if (graph.table[i]!=null) {
//                for(Integer id: graph.table[i].keySet()){
//                    System.out.print(i+"---"+id+"---"+graph.table[i].get(id)+":::");
//                }
//            }
//            System.out.println();
//        }

    }

    /**
     * 注意此测试中的寻找路径为空需要后续处理，例如添加服务器节点
     */
//    @Test
//    public void searchPathTest(){
//        long start = System.currentTimeMillis();
//
//        graph.serverIds = new ArrayList<>();
//        graph.serverIds.add(35);
//        graph.serverIds.add(16);
//        graph.serverIds.add(17);
//        graph.serverIds.add(9);
//        graph.serverIds.add(3);
//        graph.serverIds.add(26);
//
//        GraphProcess graphProcess = new GraphProcess(graph);
//        graphProcess.updateGraph();
//        PQDijkstra pqDijkstra = new PQDijkstra(graph, 1000);
//
//        for (NetworkVertex networkVertex:graph.userAdjVertices){
//            List<List> lists = pqDijkstra.searchPath(networkVertex.id, networkVertex.neighborId,networkVertex.userDatas, );
//            if (lists==null){
//                graph.serverIds.add(networkVertex.id);
////                System.out.println(networkVertex.id+"---"+networkVertex.neighborId+"---"+networkVertex.userDatas);
//            }
//            else {
//                for (List<Integer> list1 : lists) {
//                    for (Integer integer : list1) {
//                        System.out.print(integer + " ");
//                    }
//                    System.out.println();
//                }
//            }
//        }
//
//        long end = System.currentTimeMillis();
//
//        System.out.println(end - start);
//
//
//
//
//    }

    @Test
    public void deleteUselessVertexTest(){
        GraphProcess graphProcess = new GraphProcess(graph);

        System.out.println(graph.getEdges().size());
        graphProcess.updateGraph();
        System.out.println(graph.getEdges().size());

    }

    @Test
    public void createVerticsTableTest(){
//        GraphProcess graphProcess = new GraphProcess(graph);
//        graphProcess.updateGraph();
//        for (int i=0; i < graph.networkVertexnum; ++i){
//            for (int j=0; j < graph.networkVertexnum; ++j){
//                System.out.println(i+"---"+graph.adj[i][j]);
//            }
//        }
    }

    @Test
    public void serverPointsTest(){
        GraphProcess graphProcess = new GraphProcess(graph);
        graphProcess.updateGraph();
        ServerPoint serverPoint = new ServerPoint(graph);
        serverPoint.getServerIds(0, graph.networkVertexnum);
        for (int i=0; i < graph.serverIds.size(); ++i)
            System.out.println(graph.getNetworkVertices().get(graph.serverIds.get(i)).data);
    }
    @Test
    public void dataStatisticTest(){
        GraphProcess graphProcess = new GraphProcess(graph);
        graphProcess.updateGraph();
        for (NetworkVertex list:graph.getNetworkVertices())
            System.out.println(list);

    }

    @Test
    public void userAdjVertices(){
        for (NetworkVertex networkVertex: graph.userAdjVertices)
            System.out.println(networkVertex);
    }
    @Test
    public void filereadTest(){
        System.out.println(graph.networkVertexnum+" "+ graph.userVertexnums+" "+ graph.edgenum+" "+ graph.serverValue);
        List edgeList = graph.getEdges();
        List vertexList = graph.getNetworkVertices();
        List usertexList = graph.getUserVertexs();
        for (int i = 0; i< graph.edgenum; ++i){
            System.out.println(edgeList.get(i));
        }
        System.out.println();
        for (int i = 0; i< graph.networkVertexnum; ++i){
            System.out.println(vertexList.get(i));
        }
        System.out.println();
        for (int i = 0; i< graph.userVertexnums; ++i){
            System.out.println(usertexList.get(i));
        }
    }
}
