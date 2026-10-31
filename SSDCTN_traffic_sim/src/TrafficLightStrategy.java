//Strategy interface for deciding whether a vehicle shoud stop at a traffic light.
public interface TrafficLightStrategy {
    // Retrurns true if the vehicle should stop.
    boolean shouldStop(boolean isRed);
}
