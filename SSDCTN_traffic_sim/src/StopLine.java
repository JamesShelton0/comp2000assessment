public class StopLine {
    private Edge e;
    private double direction;
    private boolean isActive;

    public StopLine(Point left, Point right, double direction){
        e = new Edge(left, right);
        this.direction = direction;
        this.isActive = false;
    }

    public void setActive(){
        isActive = true;
    }

    public void setInactive(){
        isActive = false;
    }

    public boolean getActive(){
        return isActive;
    }

    public double getDirection(){
        return direction;
    }

    public Edge getEdge(){
        return e;
    }
}
