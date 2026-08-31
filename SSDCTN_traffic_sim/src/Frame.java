import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame { 
    Panel panel;

    Frame(int width, int height, String logoPath) {
        panel = new Panel(width, height);
        this.setTitle("Traffic Sim");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.setResizable(false);
        ImageIcon logo = new ImageIcon(logoPath);
        this.setIconImage(logo.getImage());
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}

class Panel extends JPanel {
    Truck truck;
    Pothole pothole;
    Explosion explosion;

    Panel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(63, 155, 11));

        truck = new Truck(70, 50);
        pothole = new Pothole(5, 5);
        explosion = new Explosion(300, 300, this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);            // paints JPanel stuff like the background

        Graphics2D g2d = (Graphics2D) g;    // for our 2D graphics components

        // draw components
        truck.draw(g2d);
        pothole.draw(g2d);
        explosion.draw(g2d);    // test explosion !!!!! remove this to not show explosion :(
    }
}