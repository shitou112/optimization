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

    public PQDijkstra(Graph graph, int maxFindEdgeNum){
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
    public int searchGraphPaths(List<NetworkVertex> userVertices, HashMap<Integer, Edge>[] hashMaps){
        if (graph.serverIds.size() ==0 ){
            graph.serverIds.add(graph.userAdjVertices.get(0).id);
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
            List<Integer> cannotFindPathList = new ArrayList<>();
            cannotFindPathList.add(s);
            cannotFindPathList.add(userId);
            cannotFindPathList.add(data);

            graph.serverIds.add(s);
            pathsList.add(cannotFindPathList);

            return pathsList;
        }
        sumcost += oneVertexCost;
        return pathsList;
    }

    private int searchOnePath(int s, int userId, int data, HashMap<Integer, Edge>[] hashMaps){
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
            HashMap<Integer, Edge> hashMap = hashMaps[temp];
            if (hashMap != null){
                for (int id: hashMap.keySet()) {
                    edge = hashMap.get(id);
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
    private int updateEdge(int userId, int data, HashMap<Integer, Edge>[] hashMaps){
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
            weight = hashMaps[prepath[i]].get(i).weight;
            if (minWeight > weight){
                minWeight = weight;
            }

        }

        if (minWeight > data)
            minWeight = data;

        //更新路径的边，并添加路径到集合list中
        Edge edge;
        for (i=minId; prepath[i] != -1; i=prepath[i]){
            edge = hashMaps[prepath[i]].get(i);
            edge.weight -=minWeight;
            oneCost += minWeight*edge.money;
            list.add(i);
        }

        list.add(i);
        list.add(userId);
        list.add(minWeight);
        pathsList.add(list);

        oneVertexCost += oneCost;

        return minWeight;
    }

}
