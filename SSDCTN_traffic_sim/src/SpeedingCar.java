import java.awt.Color;

// A faster car that uses a strategy to decide whether to obey traffic lights.
public class SpeedingCar extends Car {

    // Determines how this car responds to traffic lights.
    private TrafficLightStrategy trafficLightStrategy;

    public SpeedingCar(Point p, double width, double height, float direction,
                       TrafficLightStrategy trafficLightStrategy) {

        // Initialise the car using the existing Car constructor.
        super(p, width, height, direction);

        // Set a higher maximum speed and acceleration rate.
        super.topSpeed = 16;
        super.accelerationRate = 4;
        super.bodyColour = Color.RED;

        // Assign the driving strategy.
        this.trafficLightStrategy = trafficLightStrategy;
    }

    // Use the selected strategy to decide whether to stop.
    public boolean shouldStopAtTrafficLight(boolean isRed) {
        return trafficLightStrategy.shouldStop(isRed);
    }
}