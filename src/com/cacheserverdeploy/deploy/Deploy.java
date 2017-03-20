package com.cacheserverdeploy.deploy;


import com.xd.algorithm.FastPQDijkstra;
import com.xd.algorithm.GA;
import com.xd.algorithm.NewGA;
import com.xd.data.GraphProcess;
import com.xd.graph.Edge;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;
import com.xd.myutils.StringsUtils;

import java.util.List;

public class Deploy
{
    /**
     * todo
     * 
     * @param graphContent caseinfo
     * @return  caseouput info
     * @see [huawei]
     */
    public static String[] deployServer(String[] graphContent)
    {
        /**do your work here**/



        List list = null;
        Graph graph = StringsUtils.readStrings(graphContent);
        GraphProcess graphProcess = new GraphProcess(graph);
        graphProcess.updateGraph();

        int popSize = (int) Math.round(graph.aliveNetVerticesNum * 0.8);
        if (popSize % 2 ==0)
            popSize ++;

//        System.out.println("===");

//        System.out.println(popSize);
        GA ga = null;
        for (int i=0; i < 5; ++i) {
            //参数：种群大小，存活网络节点个数, 代数
            ga = new GA(71, graph.aliveNetVerticesNum, 600, graphProcess);
            ga.startGA();
            list = ga.getBestList();
            System.out.println(ga.getBestCost()+" "+ga.getBestId());
            System.out.println(ga.newPop[0]);
        }


//        graph.serverIds.clear();
//        NetworkVertex networkVertex;
//        for (int i=0; i < ga.oldPop[0].chrom.length; ++i){
//            if (ga.oldPop[0].chrom[i] == 1) {
//
//                networkVertex = graph.getNetworkVertices().get(i);
//                graph.serverIds.put(networkVertex.id, true);
//                System.out.print(networkVertex.id+" ");
//            }
//        }


//        System.out.println("===");

//        graph.serverIds.put(0,true);
//        graph.serverIds.put(1,true);
//        graph.serverIds.put(24,true);


//        FastPQDijkstra fastPQDijkstra = new FastPQDijkstra(graph);
//        int sum = fastPQDijkstra.searchGraphPaths(graph.userAdjVertices, graph.table);
//        list = fastPQDijkstra.getAllPathList();
//        System.out.println(sum);
        return StringsUtils.ListT0Strings(list);
    }

}
