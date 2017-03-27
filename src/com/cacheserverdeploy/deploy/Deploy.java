package com.cacheserverdeploy.deploy;


import com.xd.algorithm.GA;
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



        List allPathList = null;
        List dijkstraList = null;
        List mcmfList = null;
        int dijkstraCost = 0, mcmfCost = 0;
        Graph graph = StringsUtils.readStrings(graphContent);
        GraphProcess graphProcess = new GraphProcess(graph);
        graphProcess.updateGraph();

        int popSize = (int) Math.round(graph.aliveNetVerticesNum * 0.5);
        if (popSize % 2 ==0)
            popSize ++;

//        System.out.println("===");
//        System.out.println(graph.aliveNetVerticesNum);
//        System.out.println(popSize);

        double[] pm2 = {0.003, 0.005, 0.007, 0.01,0.03};
        double[] pm0 = {0.003};


        int gen = 800;
        GA ga1 = null;


        if (graph.networkVertexnum < 250){
            gen = 800;
            popSize = 51;

        }
        else if (graph.networkVertexnum < 500){
            gen = 800;
            popSize = 41;

        }
        else {
            gen = 800;
            popSize = 41;

        }

        for (int i = 0; i < 1; ++i) {

            long s = System.currentTimeMillis();
            //参数：种群大小，存活网络节点个数, 代数
            ga1 = new GA(popSize, graph.aliveNetVerticesNum, gen, graphProcess);


            //遗传算法寻找服务器
            ga1.startGA();
            dijkstraList = ga1.getBestList();
            dijkstraCost = ga1.getBestCost();
            System.out.print(dijkstraCost+" ");
            System.out.println(ga1.getBestId());


            //最大流找路径
            PathCost pathCost = new PathCost(graph);
            graph.serverIds = ga1.bestServer;
            graphProcess.addEdges();
            ga1.addSuperSource(ga1.bestServer);
            System.out.println(ga1.bestServer.size());

            mcmfCost = pathCost.minPathCost(graph.table);
            System.out.println(mcmfCost);
            mcmfList = pathCost.getAllPathList();
            long e = System.currentTimeMillis();
            System.out.println("mcmf:"+mcmfList.size());
            System.out.println("dijkstra:"+dijkstraList.size());
            System.out.println(e-s+"  ======");
        }


        allPathList = dijkstraCost < mcmfCost? dijkstraList: mcmfList;

        return StringsUtils.ListT0Strings(allPathList);
    }

}
