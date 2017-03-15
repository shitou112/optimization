package com.xd.graph;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class Edge implements Cloneable{
    /**
     * 边的起点
     */
    public int v;

    /**
     * 边的终点
     */
    public int w;

    /**
     * 边的最大流量
     */
    public int weight;

    /**
     * 边的花费
     */
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
