package com.xd.data;

import com.xd.graph.Edge;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class GraphProcess {
    private Graph graph;
    private List datalist;
    private int sumData;

    private int[] adjKingVertexData;

    //A是用户需求占比， B是王权节点占比， C是其他流量占比
    public double A = 1.4, B = 0.8, C=0.4;


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

        computeAliveVertices();

        dataSort();

        // 流量统计

//        dataSortUserAdjVertices();
//
//        int i=0;
//
//        for (NetworkVertex networkVertex: graph.getNetworkVertices()){
//            System.out.println((i++)+"   "+networkVertex);
//        }
//        System.out.println(graph.aliveNetVerticesNum);

        return graph;
    }

    public void computeAliveVertices(){
        if (graph.networkVertexnum  < 100 ){
            int tmp = (int)Math.round(0.9 * graph.networkVertexnum);
            if (graph.aliveNetVerticesNum > tmp){
                graph.aliveNetVerticesNum = tmp;
            }
        }else if (graph.networkVertexnum  < 200){
//            A = 1.6; B = 1.2; C= 0.8;

            A = 2.6; B = 1.4; C= 1.2;
            System.out.println(A+" "+B+" "+C);
            int tmp = (int)Math.round(0.42 * graph.networkVertexnum);
            if (graph.aliveNetVerticesNum > tmp){
                graph.aliveNetVerticesNum = tmp;
            }

        }else if (graph.networkVertexnum  < 500){
            A = 2.8; B = 1.4; C= 1.2;
            System.out.println(A+" "+B+" "+C);
            int tmp = (int)Math.round(0.4 * graph.networkVertexnum);
            if (graph.aliveNetVerticesNum > tmp){
                graph.aliveNetVerticesNum = tmp;
            }

        }
        else if (500 <= graph.networkVertexnum){
            A = 5; B = 1.4; C= 1.2;

            System.out.println(A+" "+B+" "+C);
            int tmp = (int)Math.round(0.28 * graph.networkVertexnum);
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
            networkVertex.userScore = networkVertex.userDatas / graph.userNeedData ;
//            networkVertex.userScore =  graph.userNeedData / networkVertex.userDatas;
//               1000*1.0/graph.table[i].size()*0.7 +
        }
    }

    /**
     *计算各个节点的服务器得分
     */
    private void computeServerScore(){
        NetworkVertex networkVertex;

        for (int i=0; i < graph.networkVertexnum; ++i){
            int tmp = 1;
            networkVertex = graph.getNetworkVertices().get(i);
            if (networkVertex.neighborId == -2){
                tmp = -1;
            }

            networkVertex.serverScore = A*(networkVertex.userDatas) + C*(networkVertex.data) + (B-C)*adjKingVertexData[i] + 100*tmp;
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

        }

    }

    /**
     * 初始化graph中的table，统计每个节点的相邻边
     */
    public void addEdgesOfVertex(){
        graph.table = new HashMap[graph.networkVertexnum+2];

        for (Edge edge: graph.getEdges()){
            graph.add(new Edge(edge.v, edge.w, edge.weight, edge.money));
            graph.add(new Edge(edge.w,edge.v,edge.weight,edge.money));
        }

    }

    public void addEdges(){
        graph.table = new HashMap[graph.networkVertexnum+2];

        for (Edge edge: graph.getEdges()){
            graph.add(new Edge(edge.v, edge.w, edge.weight, edge.money));
            graph.add(new Edge(edge.w,edge.v,edge.weight,edge.money));
        }
        for (Integer id:graph.kingNetworks.keySet()){
            graph.add(new Edge(id, graph.networkVertexnum, graph.kingNetworks.get(id).userDatas, 0));
        }
    }




    /**
     * 对网络拓扑图中各个节点的流量进行排序
     *
     */
    private void dataSort(){
        computeServerScore();
        Collections.sort(graph.getNetworkVertices());
    }

    private void dataSortUserAdjVertices(){
        computeUserScore();
        Collections.sort(graph.userAdjVertices);
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
