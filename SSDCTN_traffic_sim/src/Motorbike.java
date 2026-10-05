import java.awt.*;

public class Motorbike extends Vehicle {

    Motorbike(Point p, double width, double height) {
        super.accelerationRate = 0.8;
        super.decelerationRate = -5;
        super.topSpeed = 10;

        super.position = p;
        super.width = 80;
        super.height = 120;

        super.hitBox = new HitBox(position, super.direction, super.width, super.height*0.5); // some magic numbers to extend hitbox over the wheels
    }

    @Override
    public void draw(Graphics2D g2d) { 

        Graphics2D g = (Graphics2D) g2d.create(); // same with this, I need a g2d copy to rotate the motorbike without breaking the sim

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
        g.fillOval((int) bikeX + 15, (int) bikeY + 5, (int) width/4, (int) (height/6));
        g.fillOval((int) bikeX + 15, (int) bikeY + 55, (int) width/4, (int) (height/6));

        // body
        g.setColor(Color.RED);
        g.fillRoundRect((int) bikeX + 12, (int) bikeY + 22, (int) 26, (int) 40, (int) 8, (int) 8);

        // seat
        g.setColor(Color.BLACK);
        g.fillRoundRect((int) bikeX + 16, (int) bikeY + 35, (int) 18, (int) 15, (int) 5, (int) 5);

        // handlebar
        g.setColor(Color.DARK_GRAY);
        g.fillRect((int) bikeX + 10, (int) bikeY + 20, (int) 30, (int) 3);

        g.dispose();

    }
}
