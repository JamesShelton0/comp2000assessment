// A driving strategy that follows traffic light rules.
public class FollowTrafficLight implements TrafficLightStrategy {

    public boolean shouldStop(boolean isRed) {
        // Stop when the traffic light is red.
        return isRed;
    }
}
