package com.cacheserverdeploy.deploy;


import com.xd.algorithm.GA;
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
        int bestcost = Integer.MAX_VALUE;
        double best_pro_corss =0,best_pro_mutution=0;
        List list = null;
        Graph graph = StringsUtils.readStrings(graphContent);
        GraphProcess graphProcess = new GraphProcess(graph);
        double[] pro_cross = {0.618};
        double[] pro_mutution = {0.1};
        double[] pro_better_mutution = {0.01};
        double pro_xnor = 0.1;
        GA ga = null;
        for (int i=0; i < pro_cross.length; ++i){
            for (int j=0; j < pro_mutution.length; ++j){
                ga =new GA(20, pro_cross[i], pro_mutution[j], pro_better_mutution[0], pro_xnor,graph.networkVertexnum, 1000, graphProcess);
                ga.startGA();
                if (ga.getBestCost() < bestcost){
                    best_pro_mutution = pro_mutution[j];
                    best_pro_corss = pro_cross[i];
                    list = ga.getBestList();
                }
            }
        }

        System.out.println(best_pro_corss);
        System.out.println(best_pro_mutution);
        System.out.println("=====");
        System.out.println(ga.getBestCost());
        System.out.println(ga.getBestId());



        return StringsUtils.ListT0Strings(list);
    }

}
