package com.test;

import com.xd.algorithm.PQDijkstra;
import com.xd.graph.Edge;
import org.junit.Test;
import com.xd.algorithm.BreadthFirstPaths;
import com.xd.data.ServerPoint;
import com.xd.data.GraphProcess;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;
import com.xd.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class App {
    Graph graph = FileUtils.readFile();

    @Test
    public void searchPathTest(){
        graph.serverIds = new ArrayList<>();
        graph.serverIds.add(35);
//        graph.serverIds.add(16);
//        graph.serverIds.add(17);
//        graph.serverIds.add(9);
//        graph.serverIds.add(3);
//        graph.serverIds.add(26);

        GraphProcess graphProcess = new GraphProcess(graph);
        graphProcess.updateGraph();
        PQDijkstra pqDijkstra = new PQDijkstra(graph, 1000);
        List<List> lists = pqDijkstra.searchPath(0, 0,38);
            for (List<Integer> list1:lists){
                for (Integer integer:list1){
                    System.out.print(integer+" ");
                }
                System.out.println();
            }
//        for (NetworkVertex networkVertex:graph.userAdjVertices){
//            List<List> lists = pqDijkstra.searchPath(networkVertex.id,networkVertex.userDatas);
//            for (List<Integer> list1:lists){
//                for (Integer integer:list1){
//                    System.out.print(integer+" ");
//                }
//                System.out.println();
//            }
//        }



    }

    @Test
    public void deleteUselessVertexTest(){
        GraphProcess graphProcess = new GraphProcess(graph);

        System.out.println(graph.getEdges().size());
        graphProcess.updateGraph();
        System.out.println(graph.getEdges().size());

    }

    @Test
    public void createVerticsTableTest(){
        GraphProcess graphProcess = new GraphProcess(graph);
        graphProcess.updateGraph();
        for (int i=0; i < graph.networkVertexnum; ++i){
            for (int j=0; j < graph.networkVertexnum; ++j){
                System.out.println(i+"---"+graph.adj[i][j]);
            }
        }
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
