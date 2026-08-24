public abstract class Vehicle {
    private float velocity;
    private int length;
    private int width;
    private double x, y;
    private Point position;
    private float direction;
    int topSpeed;
    int accelerationRate;
    private float deceleration;
    int pollutionRate;
    boolean headLightsOn;
    boolean brakeLightsOn;

    void accelerate() {
        
    }

    void decelerate() {

    }

    void turn(int degrees, int radius) {
        // turn blinker on depending on degrees
    }
}

