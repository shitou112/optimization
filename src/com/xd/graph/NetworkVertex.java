package com.xd.graph;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class NetworkVertex extends Vertex implements Comparable<NetworkVertex>{

    public int userDatas;

    public double serverScore;

    public double userScore;



    public NetworkVertex(int id, int data){
        this.id = id;
        this.data = data;
    }
    public NetworkVertex(int id){
        this.id = id;
    }


    /**
     * 逆序比较，大的在前，小的后
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(NetworkVertex o) {
        if (this.serverScore < o.serverScore)
            return 1;
        else if (this.serverScore == o.serverScore)
            return 0;
        else
            return -1;
    }

    @Override
    public String toString() {
        return "id:"+id+"---neighbor:"+neighborId+"---userdatas"+
                userDatas+"---data"+data+"----userscore:"+userScore+"----serverscore: "+serverScore;
    }


}
