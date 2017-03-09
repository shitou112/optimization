package com.xd.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class Graph {
    /**
     * 网络拓扑图中边的数量
     */
    public int edgenum;

    /**
     * 网络拓扑图中链路节点的数量
     */
    public int networkVertexnum;

    /**
     * 网络拓扑图中用户节点的数量
     */
    public int userVertexnums;

    /**
     * 网络拓扑图中一台服务器的价格
     */
    public int serverValue;

    /**
     * table是节点数组，每一个元素中存储着与该节点相邻边的信息，
     * 其每个数组元素中存放的是一个边的链表
     */
    public HashMap<Integer, Node>[] table;

    /**
     * 邻接节点表用二维数组表示
     */
    public VertexInfo[][] adj;


    /**
     * 部署服务器的节点id
     */
    public List<Integer> serverIds;

    /**
     * 与用户节点相邻的网络节点
     */
    public List<NetworkVertex> userAdjVertices;

    /**
     * 网络拓扑图中存放边的集合
     */
    private List<Edge> edges;

    /**
     * 网络拓扑图存放链路节点的集合
     */
    private List<NetworkVertex> networkVertices;

    /**
     * 网络拓扑图中存放用户节点的集合
     */
    private List<UserVertex> userVertexs;

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void setNetworkVertices(List<NetworkVertex> networkVertices) {
        this.networkVertices = networkVertices;
    }

    public void setUserVertexs(List<UserVertex> userVertexs) {
        this.userVertexs = userVertexs;
    }


    public List<Edge> getEdges() {
        return edges;
    }

    public List<NetworkVertex> getNetworkVertices() {
        return networkVertices;
    }

    public List<UserVertex> getUserVertexs() {
        return userVertexs;
    }

    /**
     * 向id号顶点中添加一条链路边
     *
     * @param firstId 当前节点id号
     * @param nextVertexId 下个节点的id号
     * @param minCost 当前节点到下节点边的最小花费
     * @param edge 添加的链路边
     */
    public void add(int firstId, int nextVertexId, int minCost, Edge edge){
        if (table[firstId] == null)
            table[firstId] = new HashMap<>();
        table[firstId].put(nextVertexId,new Node(nextVertexId ,minCost, edge));

    }

    public class VertexInfo{
        public int id;
        public Edge edge;
        public VertexInfo(int id, Edge edge){
            this.id = id;
            this.edge = edge;
        }

        @Override
        public String toString() {
            return "id:"+id+"---"+"edge:"+edge;
        }
    }

    public class Node{

        public Edge element;
        public int minCost;
        public List<Integer> path = new LinkedList<>();
        public Node(int nextId,int minCost, Edge element){
            this.minCost = minCost;
            this.element = element;
            path.add(nextId);
        }


        @Override
        public String toString() {
            return minCost+"----"+path;
        }
    }
}
