public class Start {
    private Point startPoint;
    private int ID;

    Start(Point startPoint, int ID){
        this.startPoint = startPoint;
        this.ID = ID;
    }

    public int getID(){
        return ID;
    }

    public Point getStartPoint(){
        return startPoint;
    }
}
