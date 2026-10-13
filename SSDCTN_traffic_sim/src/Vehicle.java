import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

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
    private boolean movementBlocked;
    

    // aesthetics
    protected int pollutionRate;
    protected boolean headLightsOn;
    protected boolean brakeLightsOn;

    // vehicle colours
    
    private static final Random RAND = new Random();

    private static final Color[] SELECT = {
        Color.RED, 
        Color.YELLOW, 
        Color.BLUE,
        Color.BLACK,
        Color.PINK,
        Color.GREEN,
        Color.ORANGE,
        Color.WHITE,
        Color.GRAY,
        Color.DARK_GRAY,
        new Color(0, 70, 160),      // SUBARU WR blue :)
        new Color(153, 76, 0),      // Colour for Matthew
        new Color(0, 153, 0),   // British Racing Green
        new Color(229, 204,255),   // Porsche Frozenberrymetallic (Y08)
    };
    protected Color bodyColour = SELECT[RAND.nextInt(SELECT.length)];

    public Color getBodyColour() {
        return bodyColour;
    }


    // draw() is implemented in specific vehicle subclasses.
    // it contains the actual shapes and fills used to draw the object
    public void draw(Graphics2D g2d) {}

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
        // use vehicles desired velocity when no traffic constraints are applied
        move(velocity);
    }

    public void move(double movementVelocity){
        // allow smooth, temporary speed matching without overwriting desired velocity
        position = Velocity.calPosition(x, y, direction, movementVelocity);
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
    }

    void turn(int degrees, int radius) {
        // turn blinker on depending on degrees
    }



    void setVelocity(double velocity){
        this.velocity = velocity;
    }

    // record whether traffic light or obstacle is preventing movement
    void setMovementBlocked(boolean movementBlocked) {
        this.movementBlocked = movementBlocked;
    }

    // let acceleration pause whilst vehicle is waiting in traffic
    boolean isMovementBlocked() {
        return movementBlocked;
    }


    void setDirection(float direction){
        this.direction = direction;
    }

    double getDirection(){
        return direction;
    }

    public HitBox getHitBox(){
        return hitBox;
    }

    public void updateHitBox(){
        hitBox.updateHitbox(position, direction, velocity);
    }

    public Point getPosition() {
        return position;
    }
}