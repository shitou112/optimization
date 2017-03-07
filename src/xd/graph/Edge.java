package xd.graph;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class Edge {
    public int v;
    public int w;
    public int weight;
    public int money;
    public boolean visited;

    public Edge(int v, int w, int weight, int money){
        this.v = v;
        this.w = w;
        this.weight = weight;
        this.money = money;
    }

    @Override
    public String toString() {
        return v+" "+w+" "+weight+" "+money;
    }
}
