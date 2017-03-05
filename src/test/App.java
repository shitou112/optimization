package test;

import org.junit.Test;
import xd.data.ServerPoint;
import xd.data.GraphProcess;
import xd.graph.Graph;
import xd.graph.NetworkVertex;
import xd.utils.FileUtils;

import java.util.List;

/**
 * @author Qian Shaofeng
 * @created on 2017/3/4.
 */
public class App {
    Graph graph = FileUtils.readFile();

    @Test
    public void serverPointsTest(){
        GraphProcess graphProcess = new GraphProcess(graph);
        graphProcess.updateGraph();
        ServerPoint serverPoint = new ServerPoint(graph);
        serverPoint.getServerIds(0, graph.networkVertexnum);
        for (int i=0; i < graph.serverIds.size(); ++i)
            System.out.println(graph.getNetworkVertices().get(graph.serverIds.get(i)).data);
    }
    @Test
    public void dataStatisticTest(){
        GraphProcess graphProcess = new GraphProcess(graph);
        graphProcess.updateGraph();
        for (NetworkVertex list:graph.getNetworkVertices())
            System.out.println(list);

    }

    @Test
    public void filereadTest(){
        System.out.println(graph.networkVertexnum+" "+ graph.userVertexnums+" "+ graph.edgenum+" "+ graph.serverValue);
        List edgeList = graph.getEdges();
        List vertexList = graph.getNetworkVertices();
        List usertexList = graph.getUserVertexs();
        for (int i = 0; i< graph.edgenum; ++i){
            System.out.println(edgeList.get(i));
        }
        System.out.println();
        for (int i = 0; i< graph.networkVertexnum; ++i){
            System.out.println(vertexList.get(i));
        }
        System.out.println();
        for (int i = 0; i< graph.userVertexnums; ++i){
            System.out.println(usertexList.get(i));
        }
    }
}
