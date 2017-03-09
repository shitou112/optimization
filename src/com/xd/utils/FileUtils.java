package com.xd.utils;

import com.xd.graph.Edge;
import com.xd.graph.Graph;
import com.xd.graph.NetworkVertex;
import com.xd.graph.UserVertex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class FileUtils {

    public static final String FILEPATH = "E:\\case_example\\case0.txt";

    /**
     * 从文件中读取数据并返回一个网络拓扑图
     *
     * @return 网络拓扑图
     */
    public static Graph readFile(){
        Graph graph = new Graph();
        try {
            File file = new File(FILEPATH);
            BufferedReader br = new BufferedReader(new FileReader(file));

            List<UserVertex> userVextexList = new ArrayList<>();
            List<NetworkVertex> networkVertexList = new ArrayList<>();
            List<NetworkVertex> userAdjVertices = new ArrayList<>();
            List<Edge> edgeList = new ArrayList<>();

            String str;
            String[] strs;



            //分别读取网络拓扑的网络节点数、链路数、消费节点数
            str = br.readLine();
            strs = str.split(" ");

            graph.networkVertexnum = Integer.valueOf(strs[0]);
            graph.edgenum = Integer.valueOf(strs[1]);
            graph.userVertexnums = Integer.valueOf(strs[2]);


            //读取空行
            br.readLine();

            //读取服务器价格
            str = br.readLine();
            graph.serverValue = Integer.valueOf(str);

            //读取空行
            br.readLine();

            //读取相关的链路边
            Edge edge;
            for (int i = 0; i < graph.edgenum; ++i){
                str = br.readLine();
                strs = str.split(" ");
                edge = new Edge(Integer.valueOf(strs[0]), Integer.valueOf(strs[1]),
                        Integer.valueOf(strs[2]),Integer.valueOf(strs[3]));
                edgeList.add(edge);
            }
            graph.setEdges(edgeList);

            //读取空行
            br.readLine();

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
                str = br.readLine();
                strs = str.split(" ");

                userId = Integer.valueOf(strs[0]);
                neighborId = Integer.valueOf(strs[1]);
                userDatas = Integer.valueOf(strs[2]);
                userVertex = new UserVertex(userId, neighborId, userDatas);

                networkVertexList.get(neighborId).userDatas = userDatas;
                userAdjVertices.add(networkVertexList.get(neighborId));

                //为网络节点添加相邻的用户节点id号
                networkVertexList.get(neighborId).neighborId = userId;

                userVextexList.add(userVertex);
            }
            graph.setUserVertexs(userVextexList);
            graph.userAdjVertices = userAdjVertices;



            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }


        return graph;
    }


}
