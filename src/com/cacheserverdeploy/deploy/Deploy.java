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


//        System.out.println("===");
//        System.out.println(graph.aliveNetVerticesNum);
//        System.out.println(popSize);

        double[] A = {4};
        double[] B = {1.5};
        double[] C = {1};


        int gen = 800;
        GA ga1 = null;


        for (int i = 0; i < 1; ++i) {

            for (double a :A){
                for (double b: B){
                    for (double c: C){

                        Graph graph = StringsUtils.readStrings(graphContent);
                        int popSize = (int) Math.round(graph.aliveNetVerticesNum * 0.5);
                        if (popSize % 2 ==0)
                            popSize ++;
                        GraphProcess graphProcess = new GraphProcess(graph);

                        if (graph.networkVertexnum < 250){
                            gen = 650;
                            popSize = 55;

                        }
                        else if (graph.networkVertexnum < 500){
                            gen = 650;
                            popSize = 55;
                        }
                        else {
                            gen = 600;
                            popSize = 41;
                        }

                        graphProcess.A = a;
                        graphProcess.B = b;
                        graphProcess.C = c;

                        System.out.println("a: "+a+" b: "+b+" c: "+c);

                        long s = System.currentTimeMillis();

                        graphProcess.updateGraph();
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
                }
            }

        }


        allPathList = dijkstraCost < mcmfCost? dijkstraList: mcmfList;

        return StringsUtils.ListT0Strings(allPathList);
    }

}
