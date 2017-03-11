package com.xd.graph;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class Edge implements Cloneable{
    public int v;
    public int w;
    public int weight;
    public int money;

    public Edge(int v, int w, int weight, int money){
        this.v = v;
        this.w = w;
        this.weight = weight;
        this.money = money;
    }

    @Override
    public Edge clone(){
        Edge edge = new Edge(v, w, weight, money);
        return edge;
    }

    @Override
    public String toString() {
        return v+" "+w+" "+weight+" "+money;
    }
}
