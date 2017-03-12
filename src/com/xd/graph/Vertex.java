package com.xd.graph;

/**
 * 该类是所有节点的基类，包含节点id和数据流量信息
 * 其实现了Comparable接口，方便通过流量的大小进行
 * 排序。
 *
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public abstract class Vertex{

    /**
     * 节点的id号
     */
    public int id;

    /**
     * 相邻节点（用户节点与之网络节点相邻的id 或 网络节点与之用户节点相邻的id号）
     */
    public int neighborId = -1;

    /**
     * 节点的流量大小
     */
    public int data;




    @Override
    public String toString() {
        return "id名称:"+id+"---"+"节点流量："+data;
    }
}
