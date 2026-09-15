import javax.swing.*;

public class Frame extends JFrame { 
    Panel panel;

    // propagate invalid setup info to app, where user can then see
    Frame(int width, int height) throws SimulationConfigurationException {
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
    }
}

// Panel is in its own file now. Panel.java