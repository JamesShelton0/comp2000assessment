import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Vehicle extends JComponent {
    // size and position
    protected int length, width;
    protected double x, y;
    protected Point position;

    // movement
    protected int topSpeed;
    protected float velocity;
    protected float accelerationRate, decelerationRate;
    protected float direction;

    // aesthetics
    protected int pollutionRate;
    protected boolean headLightsOn;
    protected boolean brakeLightsOn;

    // For acceleration pass positive number, for deceleration pass negative number
    void accelerate(float change) {
        velocity += change;
        if (velocity < 0) velocity = 0;
    }

    void turn(int degrees, int radius) {
        // turn blinker on depending on degrees
    }
}

