package com.xd.data;

import com.xd.graph.Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/5.
 */
public class ServerPoint {
    private Graph graph;

    public ServerPoint(Graph graph){
        this.graph = graph;
    }

    /**
     * 计算网络拓扑图部署服务器节点的位置，其结果存储在构造器传来的graph中
     * @param start
     * @param end
     */
    public void getServerIds(int start, int end){
        int userDataRequirement = computeUserDataRequirement();
        int networkDatas = 0;
        List<Integer> list = new ArrayList<>();
        for (int i=start; i < end; ++i){
            if (networkDatas < userDataRequirement) {
                networkDatas += graph.getNetworkVertices().get(i).data;
                list.add(i);
            }else {
                break;
            }
        }
        graph.serverIds = list;
    }

    /**
     * 计算用户总需求流量
     * @return 返回用户总需求流量的数值
     */
    private int computeUserDataRequirement(){
        int userDataRequirement = 0;
        for (int i=0; i < graph.userVertexnums; ++i){
            userDataRequirement += graph.getUserVertexs().get(i).data;
        }
        return userDataRequirement;
    }
}
