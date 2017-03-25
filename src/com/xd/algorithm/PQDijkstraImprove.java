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
 * @created on 2017/3/13.
 */
public class PQDijkstraImprove {

    private Graph graph;
    private int maxFindEdgeNum;
    private int[] disto;
    private int[] prepath;
    private int[] minWeight;

    //一次寻找服务器的花费
    private int oneCost;

    //一个用户节点寻找服务器的花费
    private int oneVertexCost;

    //所有用户节点寻找服务器的花费
    private int sumcost;
    private List<List> pathsList = new ArrayList<List>();
    private List<List> allPathList = new ArrayList<List>();
    private PriorityQueue<EdgeValue> pq;

    public PQDijkstraImprove(Graph graph, int maxFindEdgeNum){
        this.maxFindEdgeNum = maxFindEdgeNum;
        this.graph = graph;
        prepath = new int[graph.networkVertexnum];
        disto = new int[graph.networkVertexnum];
        minWeight = new int[graph.networkVertexnum];
    }

    private void init(){
        for (int i=0; i < prepath.length; ++i){
            disto[i] = 100;
            prepath[i] = -1;
            minWeight[i] = 101;
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

        pathsList.clear();


        onePathWeight = searchOnePath(s, userId, tempData, hashMaps);


        //判断选择的服务器节点能否满足用户需求
        while (tempData > onePathWeight){
            tempData -= onePathWeight;
            onePathWeight = searchOnePath(s, userId, tempData, hashMaps);
        }
        if (onePathWeight == 101) {
            pathsList.clear();
            List<Integer> cannotFindPathList = new ArrayList<Integer>();
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

    private int searchOnePath(int s, int userId, int data, HashMap<Integer, Edge>[] hashMaps){
        init();


        int serverId = -1;
        disto[s] = 0;
        minWeight[s] = data;

        HashMap<Integer,Boolean> flag = new HashMap<Integer, Boolean>();
        pq = new PriorityQueue<EdgeValue>();
        int searchNum = 0;


        pq.add(new EdgeValue(s, 0));

        EdgeValue edgeValue = null;
        Edge edge = null;
        HashMap<Integer, Edge> edgeHashMap = null;
        while (!pq.isEmpty()){


            edgeValue = pq.poll();
            flag.put(edgeValue.start,true);

//            if (graph.getNetworkVertices().get(edgeValue.start).isServer){
//                break;


            if (edgeValue.value*minWeight[edgeValue.start] > graph.serverValue)
                continue;


            edgeHashMap = hashMaps[edgeValue.start];
            if (edgeHashMap != null){
                for (Integer id: edgeHashMap.keySet()){

                    edge = edgeHashMap.get(id);
                    int value = disto[edgeValue.start] + edge.money;
                    if (edge.weight >0 && value < disto[id]) {

                        disto[id] = value;
                        prepath[id] = edgeValue.start;
                        minWeight[id] = minWeight[edgeValue.start] < edge.weight?minWeight[edgeValue.start]:edge.weight;
                        if (flag.get(id) == null) {
                            pq.add(new EdgeValue(id, value));
                        }
                    }


                }
            }

        }

        return updateEdge(userId, data, hashMaps);
    }

    /**
     * 更新边的权重并将路径保存到list集合中
     *
     * @param data 需求的流量
     * @param userId 用户节点Id
     * @return 路径上的流量，如果找一条路径最小流量大于需求流量则返回所需求的流量，否则返回路径最小流量
     */
    private int updateEdge(int userId, int data, HashMap<Integer, Edge>[] hashMaps){
        List<Integer> list = new ArrayList<Integer>();
        int minMoney = 100;
        int myweight;
        int minId = -1;

        //一次寻找的服务器的花费
        oneCost = 0;

        //寻找距离源点最近的服务器点id
//        for (Integer id: graph.serverIds){
//            if (minMoney > disto[id]){
//                minMoney = disto[id];
//                minId = id;
//            }
//        }
        int weight = 101;
        int i = minId;

//        System.out.println("minid"+minId);
        if (i==-1) {
            return weight;
        }
        myweight = minWeight[minId];


//        System.out.println(minWeight[minId]);

        //更新路径的边，并添加路径到集合list中
        Edge edge;
        for (i=minId; prepath[i] != -1; i=prepath[i]){
            edge = hashMaps[prepath[i]].get(i);
            edge.weight -=myweight;


            //考虑单向边
            hashMaps[i].get(prepath[i]).weight -= myweight;

            oneCost += myweight*edge.money;
            list.add(i);
        }

        list.add(i);
        list.add(userId);
        list.add(myweight);
        pathsList.add(list);

        oneVertexCost += oneCost;

//        for (Integer id:list){
//            System.out.print(id+" ");
//        }
//        System.out.println();
//        System.out.println("myweight"+myweight);
//        System.out.println();
        return myweight;
    }

    public class EdgeValue implements Comparable<EdgeValue>{
        public int start;
        public int value;
        public EdgeValue(int start, int value){
            this.start = start;
            this.value = value;
        }

        @Override
        public int compareTo(EdgeValue o) {
            if (o.value < this.value) {
                return 1;
            } else if (o.value > this.value) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return start +"---" + value;
        }
    }

}
