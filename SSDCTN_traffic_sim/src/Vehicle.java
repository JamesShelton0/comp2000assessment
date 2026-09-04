import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Vehicle extends JComponent {
    // size and position
    protected double width, height;
    protected double x, y;
    protected Point position;
    protected HitBox hitBox;

    // movement
    protected int topSpeed;
    protected double velocity;
    protected double accelerationRate, decelerationRate;
    protected double direction;

    // aesthetics
    protected int pollutionRate;
    protected boolean headLightsOn;
    protected boolean brakeLightsOn;


    public void draw(Graphics g2d) {}

    void setPosition(double x, double y){

        this.x = x;
        this.y = y;

        position = new Point(x, y);

        updatePosition();
    }

    void move(){
        position = Velocity.calPosition(x, y, direction, velocity);

        x = position.getX();
        y = position.getY();

        updatePosition();
    }



    void updatePosition(){ // centring method
        setBounds(
            (int)(x-width/2),
            (int)(y- height/2),
            (int) width,
            (int) height
        );
    }

    // For acceleration pass positive number, for deceleration pass negative number
    void accelerate(double change) {
        velocity += change;
        if (velocity < 0) velocity = 0;
    }

    void turn(int degrees, int radius) {
        // turn blinker on depending on degrees
    }



    void setVelocity(double velocity){
        this.velocity = velocity;
    }


    void setDirection(float direction){
        this.direction = direction;
    }
}

