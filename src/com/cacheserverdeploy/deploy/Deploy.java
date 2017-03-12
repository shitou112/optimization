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

        List list = null;
        Graph graph = StringsUtils.readStrings(graphContent);
        GraphProcess graphProcess = new GraphProcess(graph);

        GA ga = null;

        ga =new GA(30, 0.9, 0.6, 0.1, 0.2, 0.4,graph.networkVertexnum, 1000, graphProcess);
        ga.startGA();
        list = ga.getBestList();
        return StringsUtils.ListT0Strings(list);
    }

}
