import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Truck extends Vehicle {

    int colour;
    int size;
    int shape;              
    boolean hasSunroof;

    Point p;
    HitBox h;

    public Truck(double w, double h) {
        super.width = w;
        super.height = h;

        p = new Point(500, 500);
        this.h = new HitBox(p, 0, w, h);
    }

    @Override
    public void draw(Graphics2D g2d) {

        Graphics2D g = (Graphics2D) g2d.create();

        g.rotate(
            Math.toRadians(direction - 180),
            x,
            y
        );

        double left = x - width / 2;
        double top = y - height / 2;

        // Truck cabin
        Rectangle2D.Double truckCabin = new Rectangle2D.Double(
            left,
            y - (height / 5 * 2) / 2,
            width / 7 * 2,
            height / 5 * 2
        );

        g.setColor(Color.RED);
        g.fill(truckCabin);

        // Truck load
        Rectangle2D.Double truckLoad = new Rectangle2D.Double(
            left + width / 7 * 2,
            y - (height / 5 * 3) / 2,
            width / 7 * 5,
            height / 5 * 3
        );

        g.setColor(Color.BLUE);
        g.fill(truckLoad);

        // Front left wheel
        Rectangle2D.Double frontLeftWheel = new Rectangle2D.Double(
            left + 5,
            y - (height / 5 * 2) / 2 - 2,
            10,
            4
        );

        // Front right wheel
        Rectangle2D.Double frontRightWheel = new Rectangle2D.Double(
            left + 5,
            y + (height / 5 * 2) / 2 - 2,
            10,
            4
        );

        // Rear left wheel
        Rectangle2D.Double rearLeftWheel = new Rectangle2D.Double(
            left + width - 15,
            y - (height / 5 * 3) / 2 - 2,
            10,
            4
        );

        // Rear right wheel
        Rectangle2D.Double rearRightWheel = new Rectangle2D.Double(
            left + width - 15,
            y + (height / 5 * 3) / 2 - 2,
            10,
            4
        );

        // Fill wheels
        g.setColor(Color.BLACK);

        g.fill(frontLeftWheel);
        g.fill(frontRightWheel);
        g.fill(rearLeftWheel);
        g.fill(rearRightWheel);
        g.dispose();
    }
}
