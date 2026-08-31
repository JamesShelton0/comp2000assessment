import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;


public class Truck extends Vehicle {
    int colour;
    int size;
    int shape;              

    boolean hasSunroof;

    public Truck(int w, int h){
        super.width = w;
        super.height = h;
        // super.accelerationRate =
        // super.decelerationRate =
    }

    public void draw(Graphics2D g2d) {
        Rectangle2D.Double truckCabin =
        new Rectangle2D.Double(100, 105, width/7*2, height/5*2);   // (the maths keeps proportions while allowing width and height to be set through constructor)

        g2d.setColor(Color.RED);
        g2d.fill(truckCabin);

        Rectangle2D.Double truckLoad =
        new Rectangle2D.Double(120, 100, width/7*5, height/5*3);

        g2d.setColor(Color.BLUE);
        g2d.fill(truckLoad);
    }

}