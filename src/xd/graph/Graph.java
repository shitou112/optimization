package xd.graph;

import java.util.List;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class Graph {
    public int edgenum;
    public int networkVertexnum;
    public int userVertexnums;
    public int serverValue;

    public Node[] table;

    private List<Edge> edges;
    private List<NetworkVertex> networkVertices;
    private List<UserVertex> userVertexs;

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void setNetworkVertices(List<NetworkVertex> networkVertices) {
        this.networkVertices = networkVertices;
    }

    public void setUserVertexs(List<UserVertex> userVertexs) {
        this.userVertexs = userVertexs;
    }


    public List<Edge> getEdges() {
        return edges;
    }

    public List<NetworkVertex> getNetworkVertices() {
        return networkVertices;
    }

    public List<UserVertex> getUserVertexs() {
        return userVertexs;
    }

    /**
     * 向id号顶点中添加一条链路边
     *
     * @param id 顶点号
     * @param edge 添加的链路边
     */
    public void add(int id, Edge edge){
        Node node = table[id];
        table[id] = new Node(edge,node);

    }

    private class Node{
        private Edge element;
        private Node next;

        public Node(Edge element, Node next){
            this.element = element;
            this.next = next;
        }
    }
}
