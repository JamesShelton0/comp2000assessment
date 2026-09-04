import javax.swing.*;
import java.awt.Graphics2D;

public abstract class Vehicle extends JComponent {
    // size and position
    protected double width, height;
    protected double x, y;
    protected Point position;
    protected HitBox hitBox;

    // movement
    protected int topSpeed;
    protected float velocity;
    protected float accelerationRate, decelerationRate;
    protected double direction;

    // aesthetics
    protected int pollutionRate;
    protected boolean headLightsOn;
    protected boolean brakeLightsOn;


    public void draw(Graphics2D g2d) {

    }

    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
        this.position = new Point(x, y);
        updatePosition();
    }

    public void setPosition(Point point) {
        this.x = point.getX();
        this.y = point.getY();
        this.position = point;
        updatePosition();
    }

    public void move(){
        position = Velocity.calPosition(x, y, direction, 1);
        x = position.getX();
        y = position.getY();
        updatePosition();
    }

    private void updatePosition() { // centring method
        setBounds(
            (int)(x-width/2),
            (int)(y- height/2),
            (int) width,
            (int) height
        );
    }

    // For acceleration pass positive number, for deceleration pass negative number
    void accelerate(float change) {
        velocity += change;
        if (velocity < 0) velocity = 0;
    }

    void turn(int degrees, int radius) {
        // turn blinker on depending on degrees
    }



    void setVelocity(float velocity){
        this.velocity = velocity;
    }


    void setDirection(float direction){
        this.direction = direction;
    }
}

