import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;


public class Graph {
    // setting up file reading
    BufferedReader br;

    // arraylist of vertices, start points, and finish points - could be a stream
    ArrayList<Vertex> vertexArr;
    ArrayList<Vertex> startArr;
    ArrayList<Vertex> finishArr;
    HashMap<Vertex, Vertex> finishPointUnreachableByStartPoint;

    // constructor
    public Graph() throws IOException{
        // setting up the arrays
        vertexArr = new ArrayList<>();
        startArr = new ArrayList<>();
        finishArr = new ArrayList<>();
        // add start vertices to graph
        String vertexDesc;
        Vertex vertex;
        br = new BufferedReader(new FileReader("src/GraphStartVertices.txt"));
        br.readLine();
        while((vertexDesc = br.readLine()) != null){
            vertex = createStartVertexFromFile(vertexDesc);
            vertexArr.add(vertex);
            startArr.add(vertex);
        }
        // add turn vertices to graph
        br = new BufferedReader(new FileReader("src/GraphTurnVertices.txt"));
        br.readLine();
        while((vertexDesc = br.readLine()) != null){
            vertex = createTurnVertexFromFile(vertexDesc);
            vertexArr.add(vertex);
        }
        // add finish vertices to graph
        br = new BufferedReader(new FileReader("src/GraphFinishVertices.txt"));
        br.readLine();
        while((vertexDesc = br.readLine()) != null){
            vertex = createFinishVertexFromFile(vertexDesc);
            vertexArr.add(vertex);
            finishArr.add(vertex);
        }

        finishPointUnreachableByStartPoint = mapFinishToStart();
    }

    private HashMap<Vertex, Vertex> mapFinishToStart(){
        HashMap<Vertex, Vertex> map = new HashMap<>();
        for(int i = 0; i < startArr.size() && i < finishArr.size(); i++){
            map.put(startArr.get(i), finishArr.get(i));
        }
        return map;
    }

    //create start vertices
    private Vertex createStartVertexFromFile(String vertexDesc){
        Vertex vertex;
        Start start;
        ArrayList<Integer> adjVertices = new ArrayList<>();
        String[] vertexDescStrings = vertexDesc.split(",");
        int ID = Integer.parseInt(vertexDescStrings[0]);
        double x = Double.parseDouble(vertexDescStrings[2]);
        double y = Double.parseDouble(vertexDescStrings[3]);
        for(int i = 4; i < vertexDescStrings.length; i++){
            adjVertices.add(Integer.parseInt(vertexDescStrings[i]));
        }
        start = new Start(new Point(x, y), ID);
        vertex = new Vertex(start, adjVertices);
        return vertex;
    }

    // create turn Vertices
    private Vertex createTurnVertexFromFile(String vertexDesc){
        Vertex vertex;
        Turn turn;
        ArrayList<Integer> adjVertices = new ArrayList<>();
        String[] vertexDescStrings = vertexDesc.split(",");
        int ID = Integer.parseInt(vertexDescStrings[0]);
        double x = Double.parseDouble(vertexDescStrings[2]);
        double y = Double.parseDouble(vertexDescStrings[3]);
        float radius = Float.parseFloat(vertexDescStrings[4]);
        String direction = vertexDescStrings[5];
        int turnDirection = 0;
        switch (direction){
            case "left":
                turnDirection = 0;
                break;
            case "right":
                turnDirection = 1;
                break;
            case "straight":
                turnDirection = 2;
                break;
        }
        float startAngle = Float.parseFloat(vertexDescStrings[6]);
        for(int i = 7; i < vertexDescStrings.length; i++){
            adjVertices.add(Integer.parseInt(vertexDescStrings[i]));
        }
        turn = new Turn(ID, new Point(x, y), radius, turnDirection, startAngle);
        vertex = new Vertex(turn, adjVertices);
        return vertex;
    }

    // create finish vertices
    private Vertex createFinishVertexFromFile(String vertexDesc){
        Vertex vertex;
        Finish finish;
        ArrayList<Integer> adjVertices = new ArrayList<>();
        String[] vertexDescStrings = vertexDesc.split(",");
        int ID = Integer.parseInt(vertexDescStrings[0]);
        double x = Double.parseDouble(vertexDescStrings[2]);
        double y = Double.parseDouble(vertexDescStrings[3]);
        finish = new Finish(new Point(x, y), ID);
        vertex = new Vertex(finish, adjVertices);
        return vertex;
    }



    public ArrayList<Integer> getAdjacentVertices(int ID){
        return getVertex(ID).getAdjVertexArr();

    }

    public Vertex getVertex(int ID){
        for(int i = 0; i < vertexArr.size(); i++){
            if(ID == vertexArr.get(i).getTurn().getID()){
                return vertexArr.get(i);
            }
        }
        return null;
    }

    public ArrayList<Vertex> getVertexArr(){
        return vertexArr;
    }

    public ArrayList<Vertex> getStartArr(){
        return startArr;
    }

    public ArrayList<Vertex> getFinishArr(){
        return finishArr;
    }
}
