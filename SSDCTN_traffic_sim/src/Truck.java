import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Truck extends Vehicle {
    int colour;
    int size;
    int shape;              
    boolean hasSunroof;
    Point p;

    public Truck(double w, double h) {
        super.width = w;
        super.height = h;
        p = new Point(500, 500);
        super.hitBox = new HitBox(p, 0, w, h);
        
        // super.accelerationRate =
        // super.decelerationRate =
    }

    protected void paintComponent(Graphics g){

        Graphics2D g2d = (Graphics2D) g;

        super.velocity = 0;
        super.direction = 0;
        super.accelerationRate = 0;
        super.decelerationRate = 0;
       
    }

    @Override
    public void draw(Graphics2D g2d) {
        // Truck cabin
        Rectangle2D.Double truckCabin = new Rectangle2D.Double(
            x,
            y,
            width / 7 * 2,
            height / 5 * 2
        );
        g2d.setColor(Color.RED);
        g2d.fill(truckCabin);

        // Truck load
        Rectangle2D.Double truckLoad = new Rectangle2D.Double(
            x + 20,
            y - 5,
            width / 7 * 5,
            height / 5 * 3
        );
        g2d.setColor(Color.BLUE);
        g2d.fill(truckLoad);

        // Front left wheel
        Rectangle2D.Double frontLeftWheel = new Rectangle2D.Double(
            x + 5,
            y - 2,
            10,
            4
        );

        // Front right wheel
        Rectangle2D.Double frontRightWheel = new Rectangle2D.Double(
            x + 5,
            y + height / 5 * 2 - 2,
            10,
            4
        );

        // Rear left wheel
        Rectangle2D.Double rearLeftWheel = new Rectangle2D.Double(
            x + width - 15,
            y - 7,
            10,
            4
        );

        // Rear right wheel
        Rectangle2D.Double rearRightWheel = new Rectangle2D.Double(
            x + width - 15,
            y + height / 5 * 3 - 7,
            10,
            4
        );

        // Fill wheels
        g2d.setColor(Color.BLACK);
        g2d.fill(frontLeftWheel);
        g2d.fill(frontRightWheel);
        g2d.fill(rearLeftWheel);
        g2d.fill(rearRightWheel);
    }

}