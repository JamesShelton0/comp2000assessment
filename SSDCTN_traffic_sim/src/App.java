import javax.swing.JOptionPane; // used to show GUI error instead of crashing silently
import java.io.IOException;

public class App {
    public static int width = 800;
    public static int height = 800;
    public static void main(String[] args)throws Exception{
        try {
            new Frame(width, height);
        } catch (SimulationConfigurationException exception) {
            // catch config errors here, instead of broad "throws exception" 
            JOptionPane.showMessageDialog(
                null,
                exception.getMessage(),
                "Unable to start traffic simulation",
                JOptionPane.ERROR_MESSAGE
            );
        } catch(IOException e){
            System.out.println("Program can NOT open file");
        }
    }
}
