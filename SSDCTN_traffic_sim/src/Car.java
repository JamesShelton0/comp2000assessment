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

        // Main car body
        RoundRectangle2D.Double carBody = new RoundRectangle2D.Double(
            x,
            y,
            width,
            height,
            8,
            8   
        );

        g2d.setColor(Color.RED);
        g2d.fill(carBody);


        // Front window
        Rectangle2D.Double frontWindow = new Rectangle2D.Double(
            x + width / 5,
            y + height / 5,
            width / 5 * 3,
            height / 6
        );

        g2d.setColor(Color.CYAN);
        g2d.fill(frontWindow);


        // Rear window
        Rectangle2D.Double rearWindow = new Rectangle2D.Double(
            x + width / 5,
            y + height / 5 * 3,
            width / 5 * 3,
            height / 6
        );

        g2d.fill(rearWindow);


       // Front left wheel
        Rectangle2D.Double frontLeftWheel = new Rectangle2D.Double(
            x - 2,
            y + 8,
            4,
            10
        );

        // Front right wheel
        Rectangle2D.Double frontRightWheel = new Rectangle2D.Double(
            x + width - 2,
            y + 8,
            4,
            10
        );

        // Rear left wheel
        Rectangle2D.Double rearLeftWheel = new Rectangle2D.Double(
            x - 2,
            y + height - 18,
            4,
            10
        );

        // Rear right wheel
        Rectangle2D.Double rearRightWheel = new Rectangle2D.Double(
            x + width - 2,
            y + height - 18,
            4,
            10
        );

        g2d.setColor(Color.BLACK);

        g2d.fill(frontLeftWheel);
        g2d.fill(frontRightWheel);
        g2d.fill(rearLeftWheel);
        g2d.fill(rearRightWheel);   
    }
}