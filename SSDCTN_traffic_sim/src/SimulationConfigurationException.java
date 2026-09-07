// checked exception for invalid startup values, such as negative panel size or spawn percentage outside 0-100 range

public class SimulationConfigurationException extends Exception {
    public SimulationConfigurationException(String message) {
        super(message);
    }
}
