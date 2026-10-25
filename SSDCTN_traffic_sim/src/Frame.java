import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Frame extends JFrame { 
    Panel panel;
    // remember display dimensions so doesn't show pause on load 
    private int lastWindowWidth;
    private int lastWindowHeight;

    // propagate invalid setup info to app, where user can then see
    Frame(int width, int height) throws SimulationConfigurationException, Exception {
        if (width <= 0 || height <= 0) {
            throw new SimulationConfigurationException("Simulation dimensions must be positive.");
        }

        panel = new Panel(width, height);
        this.setTitle("Traffic Sim");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.setResizable(false);
        // ImageIcon logo = new ImageIcon(logoPath);
        // this.setIconImage(logo.getImage());
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        panel.requestFocusInWindow();   // recommended for key listening

        // record size before user actually changes it
        lastWindowWidth = getWidth();
        lastWindowHeight = getHeight();

        // require window size to remain unchanged for Xms before resuming 
        Timer resizeFinishedTimer = new Timer(500, e -> panel.endResizePause());
        resizeFinishedTimer.setRepeats(false);

        // pause simulation
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                // ignore any duplicate events
                if (getWidth() == lastWindowWidth && getHeight() == lastWindowHeight) {
                    return;
                }
                lastWindowWidth = getWidth();
                lastWindowHeight = getHeight();
                panel.beginResizePause();
                resizeFinishedTimer.restart();
            }
        });
    }
}

// Panel is in its own file now. Panel.java