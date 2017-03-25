package com.xd.algorithm;

import com.xd.graph.Edge;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/15.
 */
public class FastPQDijkstra {
    private Graph graph;
    private int[] disto;
    private int[] prepath;
    private int[] minWeight;

    //一次寻找服务器的花费
    private int oneCost;

    //一个用户节点寻找服务器的花费
    private int oneVertexCost;

    //所有用户节点寻找服务器的花费
    private int sumcost;

    private List<List> pathsList = new ArrayList<>();
    private List<List> allPathList = new ArrayList<>();
    private PriorityQueue<EdgeValue> pq;

    public FastPQDijkstra(Graph graph){
        this.graph = graph;
        prepath = new int[graph.networkVertexnum];
        disto = new int[graph.networkVertexnum];
        minWeight = new int[graph.networkVertexnum];
    }

    private void init(){
        for (int i=0; i < prepath.length; ++i){
            disto[i] = 100;
            prepath[i] = -1;
            minWeight[i] = Integer.MAX_VALUE;
        }
    }


    /**
     * 利用clone的集合来操作图，保存图内的相关信息
     *
     * @param userVertices 与用户需求节点相邻的网络节点
     * @param hashMaps 图相邻边的链表
     * @return 总共花费
     */
    public int searchGraphPaths(List<NetworkVertex> userVertices, HashMap<Integer, Edge>[] hashMaps){
        sumcost = 0;
        if (graph.serverIds.size() ==0 ){
            graph.serverIds.put(graph.userAdjVertices.get(0).id, true);
        }
        for (NetworkVertex userVertex : userVertices){

            //一个节点寻找服务器的花费
            oneVertexCost = 0;
            allPathList.addAll(searchPath(userVertex.id, userVertex.neighborId, userVertex.userDatas, hashMaps));
        }

        sumcost += graph.serverIds.size()*graph.serverValue;

        return sumcost;
    }

    public List<List> getAllPathList(){
        return allPathList;
    }

    /**
     *
     * @param s 路径源点
     * @param data 需求的流量
     * @param userId 用户节点
     * @return 路径集合
     */
    public List<List> searchPath(int s, int userId, int data, HashMap<Integer, Edge>[] hashMaps){
        int onePathWeight;
        int tempData = data;

        pathsList = new ArrayList<>();

        int serverId;

        serverId = findServerId(s, tempData, hashMaps);
        onePathWeight = updateEdge(userId, serverId, hashMaps);



        //判断选择的服务器节点能否满足用户需求
        while (tempData > onePathWeight){
            tempData -= onePathWeight;
            serverId = findServerId(s, tempData, hashMaps);
            onePathWeight = updateEdge(userId, serverId, hashMaps);
        }
        if (onePathWeight == Integer.MAX_VALUE) {
            pathsList.clear();
            List<Integer> cannotFindPathList = new ArrayList<>();
            cannotFindPathList.add(s);
            cannotFindPathList.add(userId);
            cannotFindPathList.add(data);

            graph.serverIds.put(s, true);
            pathsList.add(cannotFindPathList);

            return pathsList;
        }
        sumcost += oneVertexCost;
        return pathsList;
    }

    private int findServerId(int s, int data, HashMap<Integer, Edge>[] hashMaps){
        init();


        int serverId = -1;
        disto[s] = 0;
        minWeight[s] = data;

        HashMap<Integer,Boolean> flag = new HashMap<Integer, Boolean>();
        pq = new PriorityQueue<EdgeValue>();


        pq.add(new EdgeValue(s, 0, 0));

        EdgeValue edgeValue = null;
        Edge edge = null;
        HashMap<Integer, Edge> edgeHashMap = null;
        while (!pq.isEmpty()){

            edgeValue = pq.poll();

            if (graph.serverIds.get(edgeValue.start) != null) {
                return edgeValue.start;
            }


            if (disto[edgeValue.start]*minWeight[edgeValue.start] > graph.serverValue) {
//                System.out.println(s+"----"+edgeValue.start+"---"+disto[edgeValue.start]*minWeight[edgeValue.start]);
                break;
            }


            edgeHashMap = hashMaps[edgeValue.start];
            if (edgeHashMap != null && flag.get(edgeValue.start) == null){
                for (Integer id: edgeHashMap.keySet()){

                    edge = edgeHashMap.get(id);
                    if (edge.weight > 0) {
                        int value = disto[edgeValue.start] + edge.money;
                        if (value < disto[id]) {
                            disto[id] = value;
                            prepath[id] = edgeValue.start;
                            minWeight[id] = minWeight[edgeValue.start] < edge.weight ? minWeight[edgeValue.start] : edge.weight;
                            if (flag.get(id) == null) {
                                pq.add(new EdgeValue(id, edge.weight, disto[id]));
                            }
                        }

                    }


                }
                flag.put(edgeValue.start,true);
            }

        }
//        System.out.println("------11111111111111111------");
        return -1;

    }

    /**
     * 更新边的权重并将路径保存到list集合中
     *
     * @param userId 需求的流量
     * @param serverId 用户节点Id
     * @return 路径上的流量，如果找一条路径最小流量大于需求流量则返回所需求的流量，否则返回路径最小流量
     */
    private int updateEdge(int userId, int serverId, HashMap<Integer, Edge>[] hashMaps){

        if (serverId == -1)
            return Integer.MAX_VALUE;

        List<Integer> list = new ArrayList<>();
        int myweight;
        oneCost = 0;
        int i;

        myweight = minWeight[serverId];


//        System.out.println(minWeight[minId]);

        //更新路径的边，并添加路径到集合list中
        Edge edge;
        for (i=serverId; prepath[i] != -1; i=prepath[i]){
            edge = hashMaps[prepath[i]].get(i);

//            //添加负向流
//            hashMaps[i].get(prepath[i]).weight -= myweight;

            edge.weight -=myweight;
            oneCost += myweight*edge.money;
            list.add(i);
        }

        list.add(i);
        list.add(userId);
        list.add(myweight);
        pathsList.add(list);

//        for (Integer id: list){
//            System.out.print(id+" ");
//        }
//        System.out.print("oneceos"+oneCost);
//        System.out.println();

        oneVertexCost += oneCost;


        return myweight;
    }

    public class EdgeValue implements Comparable<EdgeValue>{
        public int start;
        public int weight;
        public int value;
        public EdgeValue(int start, int weight, int value){
            this.start = start;
            this.weight = weight;
            this.value = value;
        }

        /**
         * 优先队列排序基准，先以路径的价格优先比较，之后对链路边权重的大小优先比较，价格小的在前，价格一样的，链路边权重大的在前
         * @param o 排序对象
         * @return
         */
        @Override
        public int compareTo(EdgeValue o) {
            if (o.value < this.value) {
                return 1;
            } else if (o.value > this.value) {
                return -1;
            } else {
                if (o.weight > this.weight)
                    return 1;
                else if(o.weight < this.weight)
                    return -1;
                return 0;
            }
        }

        @Override
        public String toString() {
            return start +"---" + value;
        }
    }
}
