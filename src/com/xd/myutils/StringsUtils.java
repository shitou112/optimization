package com.xd.myutils;

import com.xd.graph.Edge;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;
import com.xd.graph.UserVertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class StringsUtils {



    /**
     * 从字符串数组中读取数据并返回一个网络拓扑图
     *
     * @param graphContent 图结构信息的字符串数组
     * @return 网络拓扑图
     */
    public static Graph readStrings(String[] graphContent){

        String str;
        int lineNum = 0;

        Graph graph = new Graph();
        List<UserVertex> userVextexList = new ArrayList<>();
        List<NetworkVertex> networkVertexList = new ArrayList<>();
        List<NetworkVertex> userAdjVertices = new ArrayList<>();
        HashMap<Integer, NetworkVertex> kingNetworks = new HashMap<>();
        List<Edge> edgeList = new ArrayList<Edge>();


        //分别读取网络拓扑的网络节点数、链路数、消费节点数
        String[] strs = graphContent[lineNum++].split(" ");

        graph.networkVertexnum = Integer.valueOf(strs[0]);
        graph.edgenum = Integer.valueOf(strs[1]);
        graph.userVertexnums = Integer.valueOf(strs[2]);

        // 空行
        lineNum++;

        //读取服务器价格
        str = graphContent[lineNum++];
        graph.serverValue = Integer.valueOf(str);

        //空行
        lineNum++;

        //读取相关的链路边
        Edge edge;
        for (int i = 0; i < graph.edgenum; ++i){

            str = graphContent[lineNum++];
            strs = str.split(" ");
            edge = new Edge(Integer.valueOf(strs[0]), Integer.valueOf(strs[1]),
                    Integer.valueOf(strs[2]),Integer.valueOf(strs[3]));
            edgeList.add(edge);
        }
        graph.setEdges(edgeList);

        //读取空行
        lineNum++;

        //构造网络链路节点
        NetworkVertex networkVertex;
        for (int i = 0; i < graph.networkVertexnum; ++i){
            networkVertex = new NetworkVertex(i);
            networkVertexList.add(networkVertex);
        }
        graph.setNetworkVertices(networkVertexList);

        //读取消费节点
        UserVertex userVertex;
        int userId = 0,neighborId = 0,userDatas = 0;
        for (int i = 0; i < graph.userVertexnums; ++i){
            str = graphContent[lineNum++];
            strs = str.split(" ");

            userId = Integer.valueOf(strs[0]);
            neighborId = Integer.valueOf(strs[1]);
            userDatas = Integer.valueOf(strs[2]);
            graph.userNeedData += userDatas;
            userVertex = new UserVertex(userId, neighborId, userDatas);

            networkVertexList.get(neighborId).userDatas = userDatas;
            userAdjVertices.add(networkVertexList.get(neighborId));
            kingNetworks.put(networkVertexList.get(neighborId).id,networkVertexList.get(neighborId));

            //为网络节点添加相邻的用户节点id号
            networkVertexList.get(neighborId).neighborId = userId;

            userVextexList.add(userVertex);
        }
        graph.setUserVertexs(userVextexList);
        graph.userAdjVertices = userAdjVertices;
        graph.kingNetworks = kingNetworks;


        return graph;
    }

    public static String[] ListT0Strings(List<List> lists){
        int i=0;
        StringBuilder sb;
        List<Integer> list = null;
        String[] strings = new String[lists.size()+2];
        strings[i++] = String.valueOf(lists.size());
        strings[i++] = "";
        for (int j=0; j < lists.size(); ++i,++j){

            sb = new StringBuilder();
            list = lists.get(j);
            for (Integer id : list) {
                sb.append(id + " ");
            }
            strings[i] = sb.toString().trim();

        }
        return strings;
    }


}
