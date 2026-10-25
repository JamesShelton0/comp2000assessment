import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
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
        new Color(0, 153, 0),       // British Racing Green
        new Color(229, 204,255),    // Porsche Frozenberrymetallic (Y08)
    };
    protected Color bodyColour = SELECT[RAND.nextInt(SELECT.length)];

    // constructor
    public Vehicle() {}

    public Color getBodyColour() {
        return bodyColour;
    }


    // draw() is implemented in specific vehicle subclasses.
    // it contains the actual shapes and fills used to draw the object
    public void draw(Graphics2D g2d) {}

    // ----------------- Position -----------------
    public void setPosition(double x, double y) {
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

    private void updatePosition() { // centring method
        setBounds(
            (int)(x-width/2),
            (int)(y- height/2),
            (int) width,
            (int) height
        );
    }

    public Point getPosition() {
        return position;
    }

    // ----------------- Movement -----------------
    public void move(double movementVelocity){
        position = Velocity.calPosition(x, y, direction, movementVelocity);
        x = position.getX();
        y = position.getY();
        updatePosition();
    }

    // For acceleration pass positive number, for deceleration pass negative number
    public void accelerate(double change) {
        velocity += change;
        if (velocity < 0) velocity = 0;
        // clamp acceleration at top speed so fractional rates cant go above it
        if (change > 0 && velocity > topSpeed) velocity = topSpeed;
    }


    public void setVelocity(double velocity){
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    // record whether traffic light or obstacle is preventing movement
    public void setMovementBlocked(boolean movementBlocked) {
        this.movementBlocked = movementBlocked;
    }

    // let acceleration pause whilst vehicle is waiting in traffic
    public boolean isMovementBlocked() {
        return movementBlocked;
    }

    // ----------------- Direction -----------------
    public void setDirection(float direction){
        this.direction = direction;
        // rotate hitbox immediately so collision checks use vehicles new travel direction
        if (hitBox != null && position != null) hitBox.updateHitbox(position, direction);
    }

    public double getDirection(){
        return direction;
    }

    // ----------------- Hitbox -----------------
    public void updateHitBox(){
        hitBox.updateHitbox(position, direction);
    }

    public HitBox getHitBox(){
        return hitBox;
    }
}