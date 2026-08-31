import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Path {
    protected float x, y;
    protected int width, height; 
    protected Color primary, accent; 

    Path(){}

    Path(int width, int height) {
        // Rectangle2D.Double 
        this.width = width;
        this.height = height;
    }

    void draw(Graphics g) {
        // Rectangle2D.Double = new
    }

    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
