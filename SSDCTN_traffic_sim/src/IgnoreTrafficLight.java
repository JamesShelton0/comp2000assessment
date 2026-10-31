// A driving strategy that ignores traffic lights.
public class IgnoreTrafficLight implements TrafficLightStrategy {

    @Override
    public boolean shouldStop(boolean isRed) {
        // Always keep moving, even when the light is red.
        return false;
    }
}
