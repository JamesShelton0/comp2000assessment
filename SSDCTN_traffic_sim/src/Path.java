import java.awt.*;

public class Path {
    protected float x, y;
    protected int width, height; 
    protected Color primary, accent; 

    Path(){}

    Path(int width, int height) {
        this.width = width;
        this.height = height;
    }

    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
