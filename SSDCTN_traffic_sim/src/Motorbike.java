import java.awt.*;

public class Motorbike extends Vehicle {

    Point p;
    Motorbike(Point p, double width, double height) {
        super.accelerationRate = 8;
        super.decelerationRate = -5;

        super.width = width;
        super.height = height;
        this.p = p;
        super.hitBox = new HitBox(p, super.direction, width, height);
    }

    @Override
    public void draw(Graphics2D g2d) { 

        Graphics2D g = (Graphics2D) g2d.create();// same with this, I need a g2d copy to rotate the motorbike without breaking the sim

        g.rotate(
            Math.toRadians(direction + 90),
            x,
            y
        );     

        // offset bike so that it appears centered 
        double bikeX = x - 25;
        double bikeY = y - 40;

        // wheels
        g.setColor(Color.BLACK);
        g.fillOval((int) bikeX + 15, (int) bikeY + 5,20,20);
        g.fillOval((int) bikeX + 15, (int) bikeY + 55,20,20);

        // body
        g.setColor(Color.RED);
        g.fillRoundRect((int) bikeX + 12, (int) bikeY + 22,26,40,8,8);

        // seat
        g.setColor(Color.BLACK);
        g.fillRoundRect((int) bikeX + 16, (int) bikeY + 35,18, 15,5,5);

        // handlebar
        g.setColor(Color.DARK_GRAY);
        g.fillRect((int) bikeX + 10, (int) bikeY + 20,30,3);

        g.dispose();

    }
}
