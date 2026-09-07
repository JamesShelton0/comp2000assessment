import java.awt.*;

public class TrafficLight {
    // 0 = green, 1 = orange, 2 = red
    private int currentLight = 0;
    private double x, y;

    public TrafficLight(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public int getLightState() {
        return currentLight;
    }

    public void setLightState(int state) {
        this.currentLight = state;
    }

        // traffic light position is relative but scaling is still hard coded for now
    public void draw(Graphics2D g2d) {
        // shadow 
        g2d.setColor(new Color(80,80,80,80));
        g2d.fillOval((int) x+10, (int) y+75,30,7);

        // pole 
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect((int) x+23, (int) y+52,4,25);

        // traffic light housing
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRoundRect((int) x+10, (int) y+5,30,55,6,6);

        // green light ^-^
        if (currentLight == 0) {
            g2d.setColor(Color.GREEN);
        } else {
            g2d.setColor(Color.GRAY);
        }
        g2d.fillOval((int) x+17, (int) y+9,16,16);

        // orange light o-o
        if (currentLight == 1) {
            g2d.setColor(Color.ORANGE);
        } else {
            g2d.setColor(Color.GRAY);
        }
        g2d.fillOval((int) x+17, (int) y+25,16,16);

        // red light -_-
        if (currentLight == 2) {
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.GRAY);
        }
        g2d.fillOval((int) x+17, (int) y+41,16,16);
    }

    public void changeLight() {
        currentLight++;

        if (currentLight > 2) {
            currentLight = 0;
        }
    }
    
}
