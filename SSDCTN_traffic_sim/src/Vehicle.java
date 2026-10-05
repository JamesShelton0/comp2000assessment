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
    protected double velocity;
    protected double accelerationRate, decelerationRate;
    protected double direction;

    // aesthetics
    protected int pollutionRate;
    protected boolean headLightsOn;
    protected boolean brakeLightsOn;

    // draw() is implemented in specific vehicle subclasses.
    // it contains the actual shapes and fills used to draw the object
    public void draw(Graphics2D g2d) {}

    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
        this.position = new Point(x, y);
        updatePosition();
        // keep collision geometry synced when vehicle is repositioned 
        if (hitBox != null) hitBox.updateHitbox(position, direction);
    }

    public void setPosition(Point point) {
        this.x = point.getX();
        this.y = point.getY();
        this.position = point;
        updatePosition();
        // keep collision geometry synced when vehicle is repositioned 
        if (hitBox != null) hitBox.updateHitbox(position, direction);
    }

    public void move(){
        position = Velocity.calPosition(x, y, direction, velocity);
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
    void accelerate(double change) {
        velocity += change;
        if (velocity < 0) velocity = 0;
        // clamp acceleration at top speed so fractional rates cant go above it
        if (change > 0 && velocity > topSpeed) velocity = topSpeed;
    }

    void turn(int degrees, int radius) {
        // turn blinker on depending on degrees
    }



    void setVelocity(double velocity){
        this.velocity = velocity;
    }


    void setDirection(float direction){
        this.direction = direction;
        // rotate hitbox immediately so collision checks use vehicles new travel direction
        if (hitBox != null && position != null) hitBox.updateHitbox(position, direction);
    }

    double getDirection(){
        return direction;
    }

    public HitBox getHitBox(){
        return hitBox;
    }

    public void updateHitBox(){
        hitBox.updateHitbox(position, direction);
    }

    public Point getPosition() {
        return position;
    }
}