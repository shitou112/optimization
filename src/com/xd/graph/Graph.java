package com.xd.graph;

import java.util.*;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class Graph{
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
     * 图中存活的节点数
     */
    public int aliveNetVerticesNum;

    /**
     * 用户总需求流量
     */
    public int userNeedData;


    /**
     * table是节点数组，每一个元素中存储着与该节点相邻边的信息，
     * 其每个数组元素中存放的是一个边的链表
     */
    public HashMap<Integer, Edge>[] table;


    /**
     * 部署服务器的节点id
     */
    public HashMap<Integer, Boolean> serverIds = new HashMap<>();

    /**
     * 与用户节点相邻的网络节点
     */
    public HashMap<Integer, NetworkVertex> kingNetworks;
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



    public Graph(){

    }
    /**
     * 向id号顶点中添加一条链路边
     *
     * @param edge 添加的链路边
     */
    public void add(Edge edge){
        if (table[edge.v] == null)
            table[edge.v] = new HashMap<Integer, Edge>();
        table[edge.v].put(edge.w,edge);

    }

    public void shuffleUseradjVertice(List list){
        Collections.shuffle(list);
    }






}
