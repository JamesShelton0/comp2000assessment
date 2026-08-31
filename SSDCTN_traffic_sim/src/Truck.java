import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Truck extends Vehicle {
    int colour;
    int size;
    int shape;              

    boolean hasSunroof;

    public Truck(double w, double h) {
        super.width = w;
        super.height = h;
        super.velocity = 0;
        super.direction = 0;
        super.accelerationRate = 0;
        super.decelerationRate = 0;
       
    }

    public void draw(Graphics2D g2d) {
        Rectangle2D.Double truckCabin =
        new Rectangle2D.Double(x, y, width/7*2, height/5*2); // 20, 20

        g2d.setColor(Color.RED);
        g2d.fill(truckCabin);

        Rectangle2D.Double truckLoad =
        new Rectangle2D.Double(x+20, y-5, width/7*5, height/5*3); // 50, 30 

        g2d.setColor(Color.BLUE);
        g2d.fill(truckLoad);
    }

}