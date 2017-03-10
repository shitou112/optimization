package com.xd.graph;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class NetworkVertex extends Vertex{

    public Integer userDatas;

    public NetworkVertex(int id, int data){
        this.id = id;
        this.data = data;
    }
    public NetworkVertex(int id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "id:"+id+"---neighbor:"+neighborId+"---userdatas"+userDatas+"---data"+data;
    }
}
