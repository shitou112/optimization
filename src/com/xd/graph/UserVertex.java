package com.xd.graph;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class UserVertex extends Vertex{



    public UserVertex(int id, int neighborId, int data){
        this.id = id;
        this.neighborId = neighborId;
        this.data = data;
    }

    @Override
    public String toString() {
        return "id名称:"+id+"---"+"neighborId："+neighborId+"---"+"节点流量："+data;
    }
}
