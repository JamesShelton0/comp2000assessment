import java.awt.*;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class SpeedCamera {
    private double x, y;
    private double laneY;                    // y of the lane this camera watches
    private long flashUntil = 0;             // time the cone should disappear
    private static final int FLASH_MS = 500; // how long the cone shows for
    // vehicle can be beside camera for several frames, but recieves one fine
    private final Set<Vehicle> finedVehicles =
        Collections.newSetFromMap(new WeakHashMap<Vehicle, Boolean>());

    SpeedCamera(double x, double y, double laneY) {
        this.x = x;
        this.y = y;
        this.laneY = laneY;
    }

    // flash if a speeding car goes past the camera in its lane
    boolean checkSpeed(Vehicle vehicle) {
        boolean besideCamera = vehicle.x >= x && vehicle.x <= x + 36;   
        boolean inLane = Math.abs(vehicle.y - laneY) < 20;              

        if (vehicle instanceof SpeedingCar && besideCamera && inLane
                && finedVehicles.add(vehicle)) {
            flashUntil = System.currentTimeMillis() + FLASH_MS;
            return true;
        }
        return false;
    }


    public int speedcameraFlashes(){
        int count = 0;
        if(System.currentTimeMillis() < flashUntil){
            count++;
        }
        return count;
    }

    // draw speed camera ^_^
    public void draw(Graphics2D g2d) {
        // flash cone from the camera centre to the lane 
        if (System.currentTimeMillis() < flashUntil) {
            int lensX = (int) x + 24;
            int lensY = (int) y + 18;
            int[] coneX = {lensX, lensX - 40, lensX + 40};
            int[] coneY = {lensY, (int) laneY, (int) laneY};
            g2d.setColor(new Color(255, 255, 255, 128)); // white, 50% opacity
            g2d.fillPolygon(coneX, coneY, 3);
        }

        // shadow and pole
        g2d.setColor(new Color(80, 80, 80, 80));
        g2d.fillOval((int) x + 2, (int) y + 48, 32, 7);
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect((int) x + 16, (int) y + 28, 4, 22);

        // housing and lens
        g2d.fillRoundRect((int) x, (int) y + 8, 36, 24, 6, 6);
        g2d.setColor(Color.BLACK);
        g2d.fillOval((int) x + 19, (int) y + 13, 11, 11);

        // red indicator
        g2d.setColor(Color.RED);
        g2d.fillOval((int) x + 5, (int) y + 14, 7, 7);
    }
}
