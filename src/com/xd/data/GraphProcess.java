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


    public GraphProcess(Graph graph){
        this.graph = graph;
    }

    public Graph updateGraph(){

        //流量统计
        addEdgesOfVertex();
        dataStatistic();
        dataSort();
        dataSortUserAdjVertices();


        //添加相邻边
//        addEdgesOfVertex();

        //删除无效节点
//        deleteUselessVertex();


        return graph;
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
        double A = 0.7, B = 0.3;
        for (NetworkVertex networkVertex: graph.getNetworkVertices()){
            networkVertex.serverScore = A*(networkVertex.data*1.0/sumData) + B*(networkVertex.userDatas*1.0/graph.userNeedData);
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
//    private void deleteUselessVertex(){
//        for (int i=0; i < graph.table.length; ++i) {
//            if (graph.table[i] != null) {
//                if (graph.table[i].size() == 1) {
//                    if(graph.getNetworkVertices().get(graph.table[i].get(0).nextVertexId).neighborId == -1) {
//                        System.out.println(graph.table[i].get(0).element);
//                        graph.getEdges().remove(graph.table[i].get(0).element);
//                        graph.table[i] = null;
//                        --graph.edgenum;
//                    }
//                }
//            }
//        }
//    }

    public Graph getGraph(){
        return graph;
    }
}
