import java.awt.*;

public class Path {
    protected double x, y;
    protected double width, height; 
    protected Color primary, accent; 

    Path(){}

    Path(double width, double height) {
        this.width = width;
        this.height = height;
    }

    void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
