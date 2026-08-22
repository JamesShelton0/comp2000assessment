public abstract class Vehicle {
    // Movement
    float x, y;
    int topSpeed;
    int accelerationRate;
    int velocity; 

    // Aesthetics
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

