package xd.data;

import xd.graph.Edge;
import xd.graph.Graph;
import xd.graph.NetworkVertex;

import java.util.*;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class DataProcess {
    private Graph graph;
    private List datalist;

    public DataProcess(Graph graph){
        this.graph = graph;
    }

    /**
     * 处理网络拓扑图中的数据，统计各个节点的流量并进行排序，排序的结果保存在网络拓扑结构图中
     *
     * @return 更新流量信息的网络拓扑图
     */
    public Graph dataStatistic(){
        List<NetworkVertex> networkVertexList = graph.getNetworkVertices();
        List<Edge> edgelist = graph.getEdges();

        Edge edge = null;
        for (int i=0,id=0; i < graph.edgenum; ++i){
            edge = edgelist.get(i);
            networkVertexList.get(edge.v).data += edge.weight;
            networkVertexList.get(edge.w).data += edge.weight;
        }

        dataSort();

        return graph;
    }


    public void addEdgesOfVertex(){
        List<Edge> edgeList = graph.getEdges();
        for (int i=0; i < graph.edgenum; ++i){
            graph.add(edgeList.get(i).v, edgeList.get(i));
            graph.add(edgeList.get(i).w, edgeList.get(i));
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
}
