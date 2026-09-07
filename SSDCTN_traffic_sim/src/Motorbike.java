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


        // draw the motorbike
        //g.setColor(Color.BLUE);
        //g.fillRoundRect((int) x, (int) y, (int) width, (int) height, 10, 10);

        // what was the above used for ?? When rotated it makes a big blue square,

        // wheels
        g.setColor(Color.BLACK);
        g.fillOval((int) x+15, (int) y+5,20,20);
        g.fillOval((int) x+15, (int) y+55,20,20);

        // body
        g.setColor(Color.RED);
        g.fillRoundRect((int) x+12, (int) y+22,26,40,8,8);

        // seat
        g.setColor(Color.BLACK);
        g.fillRoundRect((int) x+16, (int) y+35,18, 15,5,5);

        // handlebar
        g.setColor(Color.DARK_GRAY);
        g.fillRect((int) x+10, (int) y+20,30,3);

        g.dispose();

    }
}

    // hi good idea. added accelerate(float) to Vehicle and set up constructor to initialise super attributes :)
