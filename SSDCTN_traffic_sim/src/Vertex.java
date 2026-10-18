import java.util.ArrayList;

public class Vertex {
    // defining vertex types
    private final static int START = 0;
    private final static int TURN = 1;
    private final static int FINISH =2;
    private int type;
    // turn associated with vertices and arraylist of next vertices
    private Turn turn;
    private Start start;
    private Finish finish;
    private ArrayList<Integer> adjVertexArr;
    private boolean marked;
    private int ID;

    //constructor
    public Vertex(Turn turn, ArrayList<Integer> adjVertexArr){
        this.turn = turn;
        this.start = null;
        this.finish = null;
        this.adjVertexArr = adjVertexArr;
        this.marked = false;
        this.ID = turn.getID();
        type = TURN;
    }

    public Vertex(Start start, ArrayList<Integer> adjVertexArr){
        this.start = start;
        this.turn = null;
        this.finish = null;
        this.adjVertexArr = adjVertexArr;
        this.marked = false;
        this.ID = this.start.getID();
        this.type = START;
    }

    public Vertex(Finish finish, ArrayList<Integer> adjVertexArr){
        this.finish = finish;
        this.start = null;
        this.turn = null;
        this.adjVertexArr = adjVertexArr;
        this.marked = false;
        this.ID = this.finish.getID();
        this.type = FINISH;
    }

    // returning the turn
    public Turn getTurn(){
        return turn;
    }

    public Start getStart(){
        return start;
    }

    public Finish getFinish(){
        return finish;
    }

    // returning the vertex arr
    public ArrayList<Integer> getAdjVertexArr(){
        return adjVertexArr;
    }

    // setting as marked
    public void setMarked(){
        marked = true;
    }

    public void setUnmarked(){
        marked = false;
    }

    public boolean getMarked(){
        return marked;
    }

    public int getID(){
        return ID;
    }

    public int getType(){
        return type;
    }
}
