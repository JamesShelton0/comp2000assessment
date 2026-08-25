import java.awt.geom.Area;
import java.awt.Color;

public class Path {
    protected Area;

    protected float x, y;
    protected int width, height; 
    protected Color primary, accent; 

    Path(){}

    Path(int width, int height) {
        this.width = width;
        this.height = height;
    }

    void draw() {

    }

    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
