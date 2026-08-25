import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;


public abstract class Vehicle extends JComponent {
     float velocity;
    private int length;
    private int width;
    private double x, y;
    private Point position;
    private float direction;
     int topSpeed;
    private int accelerationRate;
    private float deceleration;
    private int pollutionRate;
    private boolean headLightsOn;
    private boolean brakeLightsOn;

    void accelerate() {
        
    }

    void decelerate() {

    }

    void turn(int degrees, int radius) {
        // turn blinker on depending on degrees
    }
}

