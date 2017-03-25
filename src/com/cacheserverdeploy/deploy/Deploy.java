package com.cacheserverdeploy.deploy;


import com.xd.algorithm.GA;
import com.xd.algorithm.MyGA;
import com.xd.algorithm.PathCost;
import com.xd.data.GraphProcess;
import com.xd.graph.Graph;
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

        int popSize = (int) Math.round(graph.aliveNetVerticesNum * 0.5);
        if (popSize % 2 ==0)
            popSize ++;

//        System.out.println("===");
//        System.out.println(graph.aliveNetVerticesNum);
//        System.out.println(popSize);


        GA ga1 = null;
        for (int i = 0; i < 10; ++i) {
            //参数：种群大小，存活网络节点个数, 代数
            ga1 = new GA(31, graph.aliveNetVerticesNum, 500, graphProcess);
            ga1.startGA();
            list = ga1.getBestList();
            System.out.print(ga1.getBestCost()+" ");
            System.out.println(ga1.getBestId());


            PathCost pathCost = new PathCost(graph);

            graphProcess.addEdges();
            ga1.addSuperSource(ga1.bestServer);
            graph.serverIds = ga1.bestServer;

            System.out.println(pathCost.minPathCost(graph.table));
            list = pathCost.getAllPathList();
            System.out.println("======");
        }




//        for (Integer id: ga1.bestServer.keySet()){
//            System.out.println(id);
//        }



        return StringsUtils.ListT0Strings(list);
    }

}
