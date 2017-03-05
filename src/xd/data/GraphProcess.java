package xd.data;

import xd.graph.Edge;
import xd.graph.Graph;
import xd.graph.NetworkVertex;

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
        dataStatistic();
        dataSort();
        addEdgesOfVertex();
        return graph;
    }

    /**
     * 处理网络拓扑图中的流量，统计每个节点的流量数据
     *
     */
    private void dataStatistic(){
        List<NetworkVertex> networkVertexList = graph.getNetworkVertices();
        List<Edge> edgelist = graph.getEdges();

        Edge edge = null;
        for (int i=0,id=0; i < graph.edgenum; ++i){
            edge = edgelist.get(i);
            networkVertexList.get(edge.v).data += edge.weight;
            networkVertexList.get(edge.w).data += edge.weight;
        }

    }

    /**
     * 初始化graph中的table，统计每个节点的相邻边
     */
    public void addEdgesOfVertex(){
        graph.table = new Graph.Node[graph.networkVertexnum];
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
