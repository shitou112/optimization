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
 * @created on 2017/3/8.
 */
public class PQDijkstraImprove {
    private Graph graph;
    private int maxFindEdgeNum;
    private int[] disto;
    private int[] prepath;

    //一次寻找服务器的花费
    private int oneCost;

    //一个用户节点寻找服务器的花费
    private int oneVertexCost;

    //所有用户节点寻找服务器的花费
    private int sumcost;
    private List<List> pathsList = new ArrayList<>();
    private List<List> allPathList = new ArrayList<>();
    private PriorityQueue<Integer> pq;

    public PQDijkstraImprove(Graph graph, int maxFindEdgeNum){
        this.maxFindEdgeNum = maxFindEdgeNum;
        this.graph = graph;
        prepath = new int[graph.networkVertexnum];
        disto = new int[graph.networkVertexnum];
    }

    private void init(){
        for (int i=0; i < prepath.length; ++i){
            disto[i] = 100;
            prepath[i] = -1;
        }
    }


    /**
     * 利用clone的集合来操作图，保存图内的相关信息
     *
     * @param userVertices 与用户需求节点相邻的网络节点
     * @param hashMaps 图相邻边的链表
     * @return 总共花费
     */
    public int searchGraphPaths(List<NetworkVertex> userVertices, HashMap<Integer, Graph.Node>[] hashMaps){

        if (graph.serverIds.size() == 0)
            return Integer.MAX_VALUE;

        List<List> list ;
        for (NetworkVertex userVertex : userVertices){

            list = searchPath(userVertex.id, userVertex.neighborId, userVertex.userDatas, hashMaps);
            if (list == null) {
                return Integer.MAX_VALUE;
            }

            allPathList.addAll(list);

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
    public List<List> searchPath(int s, int userId, int data, HashMap<Integer, Graph.Node>[] hashMaps){
        int onePathWeight;
        int tempData = data;

        pathsList.clear();

        onePathWeight = searchOnePath(s, userId, tempData, hashMaps);
        oneVertexCost += oneCost;
        while (tempData > onePathWeight){
            tempData -= onePathWeight;
            onePathWeight = searchOnePath(s, userId, tempData, hashMaps);

        }
        if (onePathWeight == 101) {
            pathsList.clear();
            return null;
        }
        sumcost += oneVertexCost;
        return pathsList;
    }

    private int searchOnePath(int s, int userId, int data, HashMap<Integer, Graph.Node>[] hashMaps){
        init();

        Edge edge;

        disto[s] = 0;

        HashMap<Integer,Boolean> flag = new HashMap<>();
        pq = new PriorityQueue<>();
        int searchNum = 0;
        Integer temp;
        pq.offer(s);
        while (searchNum < maxFindEdgeNum && ((temp = pq.poll())!=null)){
            if (flag.get(temp) != null)
                continue;
            flag.put(temp,true);
            HashMap<Integer, Graph.Node> hashMap = hashMaps[temp];
            if (hashMap != null){
                for (int id: hashMap.keySet()) {
                    edge = hashMap.get(id).element;
                    int value = disto[temp]+edge.money;

                    //权重大于0或者找到松弛边
                    if (edge.weight>0 && disto[id] > value){
                        disto[id] = value;
                        prepath[id] = temp;
                    }
                    pq.offer(id);
                }
            }
            ++searchNum;
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
    private int updateEdge(int userId, int data, HashMap<Integer, Graph.Node>[] hashMaps){
        List<Integer> list = new ArrayList<>();
        int minMoney = 100;
        int minWeight = 101;
        int minId = -1;

        //一次寻找的服务器的花费
        oneCost = 0;

        //寻找距离源点最近的服务器点id
        for (Integer id: graph.serverIds){
            if (minMoney > disto[id]){
                minMoney = disto[id];
                minId = id;
            }
        }
        int weight = 101;
        int i = minId;

        if (i==-1) {
            return weight;
        }
        //寻找路径中最小的流量
        for (; prepath[i] != -1; i=prepath[i]){
            weight = hashMaps[prepath[i]].get(i).element.weight;
            if (minWeight > weight){
                minWeight = weight;
            }

        }

        if (minWeight > data)
            minWeight = data;

        //更新路径的边，并添加路径到集合list中
        Edge edge;
        for (i=minId; prepath[i] != -1; i=prepath[i]){
            edge = hashMaps[prepath[i]].get(i).element;
            edge.weight -=minWeight;
            oneCost += minWeight*edge.money;
            list.add(i);
        }

        list.add(i);
        list.add(userId);
        list.add(minWeight);
        pathsList.add(list);


        return minWeight;
    }

}
