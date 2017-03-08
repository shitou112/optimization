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

    public GraphProcess(Graph graph){
        this.graph = graph;
    }

    public Graph updateGraph(){

        //流量统计
        dataStatistic();

        //添加相邻边
        addEdgesOfVertex();

        //删除无效节点
//        deleteUselessVertex();

        //创建节点邻接表
//        createVerticesTable();
//        dataSort();
        return graph;
    }

    /**
     * 创建节点的邻接表
     */
    public void createVerticesTable(){
        graph.adj = new Graph.VertexInfo[graph.networkVertexnum][graph.networkVertexnum];
        for (Edge edge: graph.getEdges()){

            Graph.VertexInfo vertexInfo = graph.new VertexInfo(edge.w, edge);
            graph.adj[edge.v][edge.w] = vertexInfo;
            graph.adj[edge.v][edge.w].edge = edge;

            vertexInfo = graph.new VertexInfo(edge.v, edge);
            graph.adj[edge.w][edge.v] = vertexInfo;

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
        }

    }

    /**
     * 初始化graph中的table，统计每个节点的相邻边
     */
    public void addEdgesOfVertex(){
        graph.table = new HashMap[graph.networkVertexnum];
        for (Edge edge: graph.getEdges()){
            graph.add(edge.v, edge.w, edge.money, edge);
            graph.add(edge.w, edge.v, edge.money, edge);
        }
    }

    /**
     * 对网络拓扑图中各个节点的流量进行排序
     *
     * @return
     */
    private void dataSort(){
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
}
