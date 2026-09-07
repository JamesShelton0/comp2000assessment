import java.awt.*;

public class SpeedCamera {
   private double x, y;
    private int speedLimit = 80;

SpeedCamera(double x, double y){
    this.x = x;
    this.y =y;
}
boolean checkSpeed(Vehicle vehicle){
    if(vehicle.x>= x-10 && vehicle.x <= x+10){ //if the vehicle is btw -10,+10 unitss from the camera

    if(vehicle.velocity > speedLimit){
        //add action whne speeding is detected
        return true;
    }
}
    return false;
}

    // draw speed camera ^_^
    public void draw(Graphics2D g2d) {
        // shadow and pole
        g2d.setColor(new Color(80, 80, 80, 80));
        g2d.fillOval((int) x + 2, (int) y + 48, 32, 7);
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect((int) x + 16, (int) y + 28, 4, 22);

        // housing and lens
        g2d.fillRoundRect((int) x, (int) y + 8, 36, 24, 6, 6);
        g2d.setColor(Color.BLACK);
        g2d.fillOval((int) x + 19, (int) y + 13, 11, 11);

        // red indicator that we can make change white or green or something if activated?
        g2d.setColor(Color.RED);
        g2d.fillOval((int) x + 5, (int) y + 14, 7, 7);
    }
}
