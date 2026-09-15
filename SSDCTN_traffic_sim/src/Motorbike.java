import java.awt.*;

public class Motorbike extends Vehicle {

    Motorbike(Point p, double width, double height) {
        super.accelerationRate = 8;
        super.decelerationRate = -5;

        super.position = p;
        super.width = width;
        super.height = height;

        super.hitBox = new HitBox(position, super.direction, width*2, height*2.1); // some magic numbers to extend hitbox over the wheels
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
        g.fillOval((int) bikeX + 15, (int) bikeY + 5, (int) width, (int) (height*0.66667));
        g.fillOval((int) bikeX + 15, (int) bikeY + 55, (int) width, (int) (height*0.66667));

        // body
        g.setColor(Color.RED);
        g.fillRoundRect((int) bikeX + 12, (int) bikeY + 22, (int) (width*1.3), (int) (height*1.3333), (int) (width*0.4), (int) (height*0.26667));

        // seat
        g.setColor(Color.BLACK);
        g.fillRoundRect((int) bikeX + 16, (int) bikeY + 35, (int) (width*0.9), (int) (height*0.5), (int) (width*0.25), (int) (height*0.16667));

        // handlebar
        g.setColor(Color.DARK_GRAY);
        g.fillRect((int) bikeX + 10, (int) bikeY + 20, (int) (width*1.5), (int) (height*0.1));

        g.dispose();

    }
}
