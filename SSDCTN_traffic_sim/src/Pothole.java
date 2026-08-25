import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Pothole extends JComponent{

    int width; 
    int height;

    public Pothole(int w, int h){
        width = w;
        height = h;
    }
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Ellipse2D.Double c = new Ellipse2D.Double(100,100,5,5);
        g2d.setColor(new Color(255,255,255));
        g2d.fill(c);

    }
}
