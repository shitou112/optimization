package xd.graph;

import java.util.Comparator;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public abstract class Vertex implements Comparable<Vertex>{

    public int id;
    public int data;

    /**
     * 逆序比较，大的在前，小的后
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Vertex o) {
        if (this.data < o.data)
            return 1;
        else if (this.data == o.data)
            return 0;
        else
            return -1;
    }

    @Override
    public String toString() {
        return "id名称:"+id+"---"+"节点流量："+data;
    }
}
