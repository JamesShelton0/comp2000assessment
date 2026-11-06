// Strategy interface for deciding whether a vehicle should stop at a traffic light.
public interface TrafficLightStrategy {
    // Returns true if the vehicle should stop.
    boolean shouldStop(boolean isRed);
}
