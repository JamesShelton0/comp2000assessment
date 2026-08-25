import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;


public class Truck extends Vehicle {
    int colour;
    int size;
    int shape;              
    // int topSpeed;        [declared in Vehicle]   
    // int pollutionRate;   [declared in Vehicle]
    boolean hasSunroof;
    // boolean lightsOn;    [Vehicle contains attributes headLightsOn and tailLightsOn]
    // int width;           [declared in Vehicle]
    // int height;          [declared in Vehicle (length)]

    public Truck(int w, int h){
        super.width = w;
        super.height = h;
        // super.accelerationRate =
        // super.decelerationRate =
    }

    protected void paintComponent(Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        Rectangle2D.Double truckCabin =
        new Rectangle2D.Double(100, 105, 20, 20);

        g2d.setColor(Color.RED);
        g2d.fill(truckCabin);

        Rectangle2D.Double truckLoad =
        new Rectangle2D.Double(120, 100, 50, 30);

        g2d.setColor(Color.BLUE);
        g2d.fill(truckLoad);
    }

}




