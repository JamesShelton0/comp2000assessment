import javax.swing.JOptionPane; // used to show GUI error instead of crashing silently

public class App {
    public static void main(String[] args) {
        try {
            new Frame(800, 800);
        } catch (SimulationConfigurationException exception) {
            // catch config errors here, instead of broad "throws exception" 
            JOptionPane.showMessageDialog(
                null,
                exception.getMessage(),
                "Unable to start traffic simulation",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
