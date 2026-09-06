public class Point {
    private double x, y;

    Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    double getX(){
        return x;
    }

    double getY(){
        return y;
    }

    public String toString() {
        return ("x: "+x+" | y: "+y);
    }
}
