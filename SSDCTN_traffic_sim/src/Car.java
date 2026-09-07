import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class Car extends Vehicle {

    public Car(double width, double height) {
        super.width = width;
        super.height = height;

        super.accelerationRate = 8;
        super.decelerationRate = 10;
        super.pollutionRate = 50;
    }

    @Override
    public void draw(Graphics2D g2d) {

        Graphics2D g = (Graphics2D) g2d.create();

        g.rotate(
            Math.toRadians(direction + 90),
            x,
            y
        );

        double left = x - width / 2;
        double top = y - height / 2;


        // ---------------- Car Body ----------------

        RoundRectangle2D.Double carBody =
            new RoundRectangle2D.Double(
                left,
                top,
                width,
                height,
                8,
                8
            );

        g.setColor(Color.RED);
        g.fill(carBody);


        // ---------------- Windows ----------------

        Rectangle2D.Double frontWindow =
            new Rectangle2D.Double(
                left + width / 5,
                top + height / 5,
                width / 5 * 3,
                height / 6
            );

        g.setColor(Color.CYAN);
        g.fill(frontWindow);


        Rectangle2D.Double rearWindow =
            new Rectangle2D.Double(
                left + width / 5,
                top + height / 5 * 3,
                width / 5 * 3,
                height / 6
            );

        g.fill(rearWindow);


        // ---------------- Wheels ----------------

        Rectangle2D.Double frontLeftWheel =
            new Rectangle2D.Double(
                left - 2,
                top + 8,
                4,
                10
            );


        Rectangle2D.Double frontRightWheel =
            new Rectangle2D.Double(
                left + width - 2,
                top + 8,
                4,
                10
            );


        Rectangle2D.Double rearLeftWheel =
            new Rectangle2D.Double(
                left - 2,
                top + height - 18,
                4,
                10
            );


        Rectangle2D.Double rearRightWheel =
            new Rectangle2D.Double(
                left + width - 2,
                top + height - 18,
                4,
                10
            );


        g.setColor(Color.BLACK);

        g.fill(frontLeftWheel);
        g.fill(frontRightWheel);
        g.fill(rearLeftWheel);
        g.fill(rearRightWheel);
        g.dispose();
    }
}