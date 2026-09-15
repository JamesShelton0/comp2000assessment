import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Pothole {

    int width; 
    int height;

    public Pothole(int w, int h){
        width = w;
        height = h;
    }

    protected void draw(Graphics2D g2d){
        Ellipse2D.Double c = new Ellipse2D.Double(100,80, width, height);
        g2d.setColor(new Color(0,0,0));
        g2d.fill(c);
    }
}
