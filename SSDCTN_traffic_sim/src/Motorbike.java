import java.awt.*;

public class Motorbike extends Vehicle {

    Motorbike(double width, double height) {
        super.accelerationRate = 8;
        super.decelerationRate = -5;

        super.width = width;
        super.height = height;
    }

    public void draw(Graphics2D g2d) {
        // wheels
        g2d.setColor(Color.BLACK);
        g2d.fillOval(15,5,20,20);
        g2d.fillOval(15,55,20,20);

        // body
        g2d.setColor(Color.RED);
        g2d.fillRoundRect(12,22,26,40,8,8);

        // seat
        g2d.setColor(Color.BLACK);
        g2d.fillRoundRect(16,35,18, 15,5,5);

        // handlebar
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(10,20,30,3);
    }
}

    // hi good idea. added accelerate(float) to Vehicle and set up constructor to initialise super attributes :)
