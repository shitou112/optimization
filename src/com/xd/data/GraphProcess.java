package com.xd.data;

import com.xd.graph.Edge;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;

import java.util.*;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class GraphProcess {
    private Graph graph;
    private List datalist;
    private int sumData;

    private int[] adjKingVertexData;
    public double A = 0.2, B = 0.5, C=0.3;


    public GraphProcess(Graph graph){
        this.graph = graph;
        graph.aliveNetVerticesNum = graph.networkVertexnum;
        adjKingVertexData = new int[graph.networkVertexnum];
    }

    public Graph updateGraph(){

        //构造邻边节点hashmap表
        addEdgesOfVertex();

        //删除无效边
        deleteUselessVertex();

        dataStatistic();

        dataSort();

        // 流量统计

//        dataSortUserAdjVertices();
        computeAliveVertices();
//        for (NetworkVertex networkVertex: graph.getNetworkVertices()){
//            System.out.println(networkVertex);
//        }



        return graph;
    }

    public void computeAliveVertices(){
        if (50 <= graph.networkVertexnum && graph.networkVertexnum  < 100 ){
            int tmp = (int)Math.round(0.7*graph.networkVertexnum);
            if (graph.aliveNetVerticesNum > tmp){
                graph.aliveNetVerticesNum = tmp;
            }
        }else if (100 <= graph.networkVertexnum && graph.networkVertexnum  < 200){

            int tmp = (int)Math.round(0.4*graph.networkVertexnum);
            if (graph.aliveNetVerticesNum > tmp){
                graph.aliveNetVerticesNum = tmp;
            }

        }else if (200 <= graph.networkVertexnum){

            int tmp = (int)Math.round(0.3*graph.networkVertexnum);
            if (graph.aliveNetVerticesNum > tmp){
                graph.aliveNetVerticesNum = tmp;
            }

        }
    }

    /**
     * 计算与用户节点相邻的网络节点得分，其作用是用于确定找路径的顺序
     */
    private void computeUserScore(){
        NetworkVertex networkVertex = null;
        for (int i=0; i < graph.userAdjVertices.size(); ++i){
            networkVertex = graph.userAdjVertices.get(i);
            networkVertex.userScore = 100*1.0/graph.table[i].size()*0.7 +  networkVertex.userDatas*1.0/graph.userNeedData*30;
        }
    }

    /**
     *计算各个节点的服务器得分
     */
    private void computeServerScore(){
        NetworkVertex networkVertex;

        for (int i=0; i < graph.table.length; ++i){
            int tmp = 1;
            networkVertex = graph.getNetworkVertices().get(i);
            if (networkVertex.neighborId == -2){
                tmp = -1;
            }

//            System.out.println(adjKingVertexData[i]);
//            System.out.println(i+": "+A*(networkVertex.userDatas)+"---"+B*(networkVertex.data)+"---"+(B-C)*adjKingVertexData[i]);
            networkVertex.serverScore = A*(networkVertex.userDatas) + B*(networkVertex.data) + (B-C)*adjKingVertexData[i] + 100*tmp;
        }
    }

    /**
     * 处理网络拓扑图中的流量，统计每个节点的流量数据
     *
     */
    private void dataStatistic(){
        List<NetworkVertex> networkVertexList = graph.getNetworkVertices();
        List<Edge> edgelist = graph.getEdges();

        Edge edge = null;
        for (int i=0; i < graph.edgenum; ++i){
            edge = edgelist.get(i);

            if (graph.getNetworkVertices().get(edge.v).neighborId > -1){
                adjKingVertexData[edge.w] += edge.weight;
            }

            if (graph.getNetworkVertices().get(edge.w).neighborId > -1){
                adjKingVertexData[edge.v] += edge.weight;
            }

            networkVertexList.get(edge.v).data += edge.weight;
            networkVertexList.get(edge.w).data += edge.weight;
            sumData +=edge.weight*2;
        }

    }

    /**
     * 初始化graph中的table，统计每个节点的相邻边
     */
    public void addEdgesOfVertex(){
        graph.table = new HashMap[graph.networkVertexnum];
        Edge copyEdge;
        for (Edge edge: graph.getEdges()){
            copyEdge = edge.clone();
            graph.add(copyEdge.v, copyEdge.w, copyEdge);
//            copyEdge = edge.clone();
            graph.add(copyEdge.w, copyEdge.v, copyEdge);
        }
    }

    public void dataSortUserAdjVertices(){
        computeUserScore();
        Collections.sort(graph.userAdjVertices, new Comparator<NetworkVertex>() {
            @Override
            public int compare(NetworkVertex o1, NetworkVertex o2) {
                if (o1.userScore < o2.userScore)
                    return 1;
                else if (o1.userScore == o2.userScore)
                    return 0;
                else
                    return -1;
            }
        });
    }

    /**
     * 对网络拓扑图中各个节点的流量进行排序
     *
     */
    private void dataSort(){
        computeServerScore();
        Collections.sort(graph.getNetworkVertices());
    }

    /**
     * 删除图中无效边，例如不和消费节点相连的一条边节点
     */
    private void deleteUselessVertex(){
        Edge edge = null;
        for (int i=0; i < graph.table.length; ++i) {
            if (graph.table[i] != null) {
                if (graph.table[i].size() == 1) {
                    if(graph.getNetworkVertices().get(i).neighborId == -1) {
                        for (Integer id: graph.table[i].keySet()) {
                            edge = graph.table[i].get(id);
                        }
                        graph.table[i] = null;
                        graph.getNetworkVertices().get(i).neighborId = -2;
                        graph.aliveNetVerticesNum--;
                        if (edge.w != i) {

                            graph.table[edge.w].remove(edge.v);
                            graph.getEdges().remove(edge);
                            --graph.edgenum;
                        }else {
                            graph.table[edge.v].remove(edge.w);
                            graph.getEdges().remove(edge);
                            --graph.edgenum;
                        }
                    }
                }
            }
        }
    }

    public Graph getGraph(){
        return graph;
    }
}
