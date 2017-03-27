package com.xd.algorithm;

import com.xd.graph.Edge;
import com.xd.graph.Graph;

import java.util.*;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/15.
 */
public class PathCost {
    private Graph graph;
    private int[] disto;
    private int[] prepath;
    private int[] vertexCost;



    private List<List> allPathList = new ArrayList<>();
    private PriorityQueue<EdgeValue> pq;

    public PathCost(Graph graph){
        this.graph = graph;
        prepath = new int[graph.networkVertexnum+2];
        disto = new int[graph.networkVertexnum+2];
        vertexCost = new int[graph.networkVertexnum+2];
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
     *
     * @return 总共花费
     */
    public int minPathCost(HashMap<Integer, Edge>[] hashMaps){
        int sumCost = 0;
        sumCost = MCMF(graph.networkVertexnum+1, graph.networkVertexnum, graph.table);

        for (Integer id: graph.kingNetworks.keySet()){
            //王权节点到汇点边权重不为0，则表明不能满足需求
            if (hashMaps[graph.kingNetworks.get(id).id].get(graph.networkVertexnum).weight != 0){

           //     sumCost -= vertexCost[id] ;
                sumCost += graph.serverValue;
            }
        }

        sumCost += graph.serverValue * graph.serverIds.size();
        return sumCost;

    }

    public List<List> getAllPathList(){
        return allPathList;
    }

    private boolean dijkstra(int s, int t, HashMap<Integer, Edge>[] hashMaps){
        init();

        disto[s] = 0;

        HashMap<Integer,Boolean> flag = new HashMap<>();
        pq = new PriorityQueue<>();


        pq.add(new EdgeValue(s, Integer.MAX_VALUE, 0));

        EdgeValue edgeValue = null;
        Edge edge = null;
        HashMap<Integer, Edge> edgeHashMap = null;
        while (!pq.isEmpty()){

            edgeValue = pq.poll();

            if (edgeValue.start == t) {
                return true;
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
                            if (flag.get(id) == null) {
                                pq.add(new EdgeValue(id, edge.weight, disto[id]));
                            }
                        }

                    }
                }
                flag.put(edgeValue.start,true);
            }

        }

        if (prepath[t] == -1){
            return false;
        }
        return true;

    }
    int MCMF(int s,int t, HashMap<Integer, Edge>[] hashMaps)
    {

        int mincost=0,minflow, onecost, maxflow = 0;///最小费用，路径中最小流量，总流量
        List<Integer> list;
        while(dijkstra(s,t,hashMaps))///找当前的最短路
        {
            list = new LinkedList<>();

            minflow=Integer.MAX_VALUE;
            onecost = 0;
            for(int i=t; prepath[i] != -1; i=prepath[i])
                minflow=Math.min(minflow,hashMaps[prepath[i]].get(i).weight);///从路径中找最小的流量

            Edge edge;
            list.add(graph.kingNetworks.get(prepath[t]).neighborId);
            for (int i=t; prepath[i] != -1; i=prepath[i]){
                edge = hashMaps[ prepath[i] ].get(i);
                edge.weight -= minflow;
                onecost += minflow * edge.money;
                list.add(0,prepath[i]);

            }
            list.remove(0);
            list.add(minflow);


            allPathList.add(list);

            mincost += onecost;
            maxflow += minflow;
            vertexCost[prepath[t]] += onecost;

            if (maxflow == graph.userNeedData){
//                System.out.println(maxflow);
                break;
            }
        }

        return mincost;
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
