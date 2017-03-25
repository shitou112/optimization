package com.xd.algorithm;

import com.xd.graph.Edge;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;

import java.util.*;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/8.
 */
public class PQDijkstra {
    private Graph graph;
    private int num_Size;
    private int[] vertexWeight;

    public HashMap<Integer, UserAdjNetworksPath> userAdjNetworksPathMaps;

    private PriorityQueue<EdgeValue> pq;


    public PQDijkstra(Graph graph){
        this.graph = graph;
        userAdjNetworksPathMaps = new HashMap<>();
        num_Size = graph.networkVertexnum;
        vertexWeight = new int[num_Size];
    }



    private void init(UserAdjNetworksPath userAdjNetworksPath){
        for (int i=0; i < num_Size; ++i){
            userAdjNetworksPath.unitCost[i] = 101;
            userAdjNetworksPath.prePath[i] = -1;
            userAdjNetworksPath.minWeight[i] = 101;
        }
    }

    public void initPath(List<NetworkVertex> userVertices, HashMap<Integer, Edge>[] hashMaps){
        searchGraphPaths(userVertices, hashMaps);
    }

    public int startPQDijkstra(List<NetworkVertex> userAdjNetworks, HashMap<Integer, Boolean> serverMaps){

        HashMap<Integer, UserAdjNetworksPath> hashMap = new HashMap<>();
        for (Integer id: userAdjNetworksPathMaps.keySet()){
            hashMap.put(id, userAdjNetworksPathMaps.get(id).clone());
        }

        return sumcost(userAdjNetworks, serverMaps, hashMap);
    }

    /**
     * 利用clone的集合来操作图，保存图内的相关信息
     *
     * @param userVertices 与用户需求节点相邻的网络节点
     * @param hashMaps 图相邻边的链表
     * @return 总共花费
     */
    public void searchGraphPaths(List<NetworkVertex> userVertices, HashMap<Integer, Edge>[] hashMaps){
        if (graph.serverIds.size() ==0 ){
            graph.serverIds.put(graph.userAdjVertices.get(0).id, true);
        }
        UserAdjNetworksPath userAdjNetworksPath;
        for (NetworkVertex userVertex : userVertices){

            //一个节点寻找服务器的花费
            userAdjNetworksPath = new UserAdjNetworksPath(num_Size);
            findPath(userVertex.id, userAdjNetworksPath, hashMaps);
            userAdjNetworksPathMaps.put(userVertex.id, userAdjNetworksPath);

//            for (int i=0; i < num_Size; ++i){
//                System.out.print(i+":"+userAdjNetworksPath.minWeight[i]+" ");
//            }
//            System.out.println();
        }

    }


    private void findPath(int s, UserAdjNetworksPath uap, HashMap<Integer, Edge>[] hashMaps){
        init(uap);

        uap.unitCost[s] = 0;

        HashMap<Integer,Boolean> flag = new HashMap<>();
        pq = new PriorityQueue<>();


        pq.add(new EdgeValue(s, Integer.MAX_VALUE, 0));

        EdgeValue edgeValue = null;
        Edge edge = null;
        HashMap<Integer, Edge> edgeHashMap = null;
        while (!pq.isEmpty()){

            edgeValue = pq.poll();

            if (uap.unitCost[edgeValue.start] > graph.serverValue) {
//                System.out.println(s+"----"+disto[edgeValue.start]*minWeight[edgeValue.start]);
                break;
            }

            edgeHashMap = hashMaps[edgeValue.start];
            if (edgeHashMap != null && flag.get(edgeValue.start) == null){
                for (Integer id: edgeHashMap.keySet()){

                    edge = edgeHashMap.get(id);
                    if (edge.weight > 0) {
                        int value = uap.unitCost[edgeValue.start] + edge.money;
                        if (value < uap.unitCost[id]) {
                            uap.unitCost[id] = value;
                            uap.prePath[id] = edgeValue.start;
                            uap.minWeight[id] = uap.minWeight[edgeValue.start] < edge.weight ? uap.minWeight[edgeValue.start] : edge.weight;
                        }
                        if (flag.get(id) == null) {
                            pq.add(new EdgeValue(id, edge.weight, value));
                        }
                    }


                }
                flag.put(edgeValue.start,true);
            }

        }

    }


    private int sumcost(List<NetworkVertex> userAdjNetworks, HashMap<Integer, Boolean> serverMaps, HashMap<Integer, UserAdjNetworksPath> userAdjNetworksPathMaps){
//        for (Integer id: userAdjNetworksPathMaps.keySet()){
//            System.out.println(id);
//            UserAdjNetworksPath us = userAdjNetworksPathMaps.get(id);
//            System.out.println(us);
//        }
//        System.out.println("=====");

        UserAdjNetworksPath userAdjNetworksPath;
        int sumCost = 0, maxCost, minCost, minId = 101, userneeddata, oneCost;
        HashMap<Integer, Integer> useData;

        for (NetworkVertex networkVertex: userAdjNetworks){

            userAdjNetworksPath = userAdjNetworksPathMaps.get(networkVertex.id);
            userneeddata = networkVertex.userDatas;
            minId = 101;
            useData = new HashMap<>();
            oneCost = 0;

//            System.out.print(networkVertex.id+" ");

            while (userneeddata > 0) {
                minId = 101;
                maxCost = 0;
                minCost = Integer.MAX_VALUE;

                for (Integer id : serverMaps.keySet()) {
                    if (userAdjNetworksPath.minWeight[id] > 0) {
                        if (maxCost < userAdjNetworksPath.unitCost[id]) {
                            maxCost = userAdjNetworksPath.unitCost[id];
                        }
                        if (minCost > userAdjNetworksPath.unitCost[id]) {
                            minCost = userAdjNetworksPath.unitCost[id];
                            minId = id;
                        }
                    }
                }

                if (minId != 101) {
//                    System.out.print(minId+" ");
                    if (userneeddata < userAdjNetworksPath.minWeight[minId]) {
                        oneCost += userneeddata * userAdjNetworksPath.unitCost[minId];
                        useData.put(minId, userneeddata);
                        userneeddata -= userneeddata;
                    }
                    else {
                        oneCost += userAdjNetworksPath.minWeight[minId] * userAdjNetworksPath.unitCost[minId];
                        useData.put(minId, userAdjNetworksPath.minWeight[minId]);
                        userneeddata -= userAdjNetworksPath.minWeight[minId];

                        int minValue = Integer.MAX_VALUE, recordId=-1;
                        for (Integer id: graph.table[minId].keySet()){
                            if (id != networkVertex.id){
                                if(userAdjNetworksPath.unitCost[id]*graph.table[minId].get(id).money < minValue){
                                    recordId = id;
                                    minValue = (int) (userAdjNetworksPath.unitCost[id]*graph.table[minId].get(id).money * 1.3);
                                }
                            }
                        }
                        if (recordId != -1){
                            userAdjNetworksPath.unitCost[minId] = minValue;
                            userAdjNetworksPath.minWeight[minId] = userAdjNetworksPath.minWeight[recordId] < graph.table[minId].get(recordId).weight?
                                    userAdjNetworksPath.minWeight[recordId]:graph.table[minId].get(recordId).weight;
                        }
                    }
                }
                else {
                    break;
                }
            }

            if (minId != 101) {

//                System.out.print(oneCost);

                vertexWeight[minId] -= userneeddata;
                sumCost += oneCost;
            }
            else {
                graph.serverIds.put(networkVertex.id,true);
                sumCost += graph.serverValue;
            }

//            System.out.println();

        }
        sumCost += graph.serverValue*graph.serverIds.size();
        return sumCost;
    }


    public class UserAdjNetworksPath implements Cloneable{
        int[] unitCost;
        int[] prePath;
        int[] minWeight;

        public UserAdjNetworksPath(int num_size){
            unitCost = new int[num_size];
            prePath = new int[num_size];
            minWeight = new int[num_size];
        }

        @Override
        public String toString() {
            String str = "";
            for (int i=0; i < num_Size; ++i)
                str+="id:"+i+" "+unitCost[i];
            str+="\n";
            for (int i=0; i < num_Size; ++i)
                str+="id:"+i+" "+minWeight[i];
            str+="\n";
            return str;
        }

        @Override
        protected UserAdjNetworksPath clone(){
            UserAdjNetworksPath userAdjNetworksPath = new UserAdjNetworksPath(num_Size);

            userAdjNetworksPath.prePath = prePath;

            int[] weight = new int[num_Size];
            int[] cost = new int[num_Size];

            for (int i=0; i < num_Size; ++i){
                weight[i] = minWeight[i];
                cost[i] = unitCost[i];
            }
            userAdjNetworksPath.minWeight = weight;
            userAdjNetworksPath.unitCost = cost;

            return userAdjNetworksPath;
        }
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
