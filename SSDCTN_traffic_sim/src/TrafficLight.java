import javax.swing.*;
import java.awt.*;

public class TrafficLight {
    // 0 = green, 1 = orange, 2 = red
    private int currentLight = 0;

    public TrafficLight() {
        //setOpaque(false);
    }


    public void draw(Graphics2D g2d) {
        // shadow 
        g2d.setColor(new Color(80,80,80,80));
        g2d.fillOval(10,75,30,7);

        // pole 
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(23,52,4,25);

        // traffic light housing
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRoundRect(10,5,30,55,6,6);

        // green light ^-^
        if (currentLight == 0) {
            g2d.setColor(Color.GREEN);
        } else {
            g2d.setColor(Color.GRAY);
        }
        g2d.fillOval(17,9,16,16);

        // orange light o-o
        if (currentLight == 1) {
            g2d.setColor(Color.ORANGE);
        } else {
            g2d.setColor(Color.GRAY);
        }
        g2d.fillOval(17,25,16,16);

        // red light -_-
        if (currentLight == 2) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.GRAY);
        }
        g2d.fillOval(17,41,16,16);
    }

    public void changeLight() {
        currentLight++;

        if (currentLight > 2) {
            currentLight = 0;
        }

        //repaint();
    }
}
