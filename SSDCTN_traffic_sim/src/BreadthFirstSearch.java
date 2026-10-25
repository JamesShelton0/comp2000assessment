import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

public class BreadthFirstSearch {

    public static ArrayList<Vertex> breadthFirstSearch(Integer startID, Integer finishID, Graph g){
        // setting up the queue and the vertex arr
        Queue<Integer> vertexQueue = new LinkedList<Integer>();
        ArrayList<Integer> adjList = new ArrayList<>();
        HashMap<Vertex, Vertex> mapOfPrevVisited = new HashMap<>();

        //setting a tracker to start
        int tracker = startID;
        vertexQueue.add(tracker);
        g.getVertex(tracker).setMarked();
        mapOfPrevVisited.put(g.getVertex(tracker), null);
        
        while(!vertexQueue.isEmpty()){
            tracker = vertexQueue.poll();
            if(g.getVertex(tracker).getID() == finishID){
                setAllUnmarked(g);
                return breadthFirstSearchHelper(mapOfPrevVisited, tracker, g);
            }
            adjList = g.getVertex(tracker).getAdjVertexArr();
            Iterator<Integer> vIt = adjList.iterator();
            while(vIt.hasNext()){
                Integer u = vIt.next();
                if(!g.getVertex(u).getMarked()){
                    vertexQueue.add(u);
                    g.getVertex(u).setMarked();
                }
                mapOfPrevVisited.put(g.getVertex(u), g.getVertex(tracker));
            }
        }
        setAllUnmarked(g);
        return null;
    }
    
    private static void setAllUnmarked(Graph g){
        for(int i = 0; i < g.getVertexArr().size(); i++){
            g.getVertexArr().get(i).setUnmarked();
        }
    }

    public static ArrayList<Vertex> breadthFirstSearchHelper(HashMap<Vertex, Vertex> vertexMap, int tracker, Graph g){
        ArrayList<Vertex> route = new ArrayList<>();
        route.add(g.getVertex(tracker));
        while(vertexMap.get(g.getVertex(tracker)) != null){
            route.add(0, vertexMap.get(g.getVertex(tracker)));
            tracker = vertexMap.get(g.getVertex(tracker)).getID();
        }
        return route;
    }
}
