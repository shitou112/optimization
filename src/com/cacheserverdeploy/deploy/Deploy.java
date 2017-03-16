package com.cacheserverdeploy.deploy;


import com.xd.algorithm.GA;
import com.xd.algorithm.NewGA;
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

        NewGA ga = null;
        for (int i=0; i < 1; ++i) {
            //参数：种群大小，存活网络节点个数, 代数，交叉概率,1变0概率，0变1概率，服务器选择概率， 与概率
//            ga = new GA(31, graph.aliveNetVerticesNum, 1000, 0.7, 0.6, 0.1, 0.1, 0.6, graphProcess);
            ga = new NewGA(30, 0.7,  0.4,     0.1,      0.2,        0.6, graph.aliveNetVerticesNum, 1000, graphProcess);
            ga.startGA();
            list = ga.getBestList();
            System.out.println(ga.getBestCost()+" "+ga.getBestId());
        }
        return StringsUtils.ListT0Strings(list);
    }

}
