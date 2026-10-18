import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;

public class BreadthFirstSearch {

    public static ArrayList<Vertex> breadthFirstSearch(){
        return null;
    }

    public static ArrayList<Vertex> breadthFirstSearch(Integer start, Integer finish, Graph g){
        // setting up the queue and the vertex arr
        Queue<Integer> vertexQueue = new LinkedList<Integer>();
        ArrayList<Vertex> route = new ArrayList<Vertex>();
        ArrayList<Integer> adjList = new ArrayList<>();

        //setting a tracker to start
        int tracker = start;
        vertexQueue.add(tracker);
        g.getVertex(tracker).setMarked();

        // adding it to the route
        route.add(0, g.getVertex(tracker));
        
        while(!vertexQueue.isEmpty()){
            tracker = vertexQueue.poll();
            adjList = g.getVertex(tracker).getAdjVertexArr();
            Iterator<Integer> vIt = adjList.iterator();
            while(vIt.hasNext()){
                Integer u = vIt.next();
                if(!g.getVertex(u).getMarked()){
                    vertexQueue.add(u);
                    g.getVertex(u).setMarked();
                }
            }
        }
        return null;
    } 
}
