// implemented by objects that receive traffic-light state changes from the controller
public interface TrafficLightObserver {
    void updateLightState(int state);
}
