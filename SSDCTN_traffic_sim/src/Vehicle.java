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
    boolean hasSunroof;
    boolean headLightsOn;
    boolean brakeLightsOn;

    Vehicle() {

    }

    void decelerate() {

    }

    void accelerate() {

    }

    void turn(int degrees, int radius) {
        // turn blinker on depending on degrees
    }
}
