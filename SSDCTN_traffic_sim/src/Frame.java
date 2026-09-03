import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Frame extends JFrame { 
    Panel panel;

    Frame(int width, int height) {
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

class Panel extends JPanel {
    private int width, height;
    VehicleSpawner vehicleSpawner;
    Road roadN, roadE, roadS, roadW, intersection;

    Pothole pothole;
    Explosion explosion;
    TrafficLight trafficLight;

    Panel(int w, int h) {
        this.width = w;
        this.height = h;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(63, 155, 11));

        // ---------------------- Timers ----------------------
        // chance to spawn a vehicle at each location every tick
        vehicleSpawner = new VehicleSpawner(width, height, 50); // % chance
        Timer vehicleSpawnTimer = new Timer(3000, e -> {              // attempt freq
            vehicleSpawner.spawn();
        });
        vehicleSpawnTimer.start();

        Timer lightTimer = new Timer(2000, e -> { trafficLight.changeLight(); });
        lightTimer.start();

        // Vehicle movement & repaint timer (~60fps)
        Timer moveTimer = new Timer(16, e -> {
            if(trafficLight.getLightState() == 2) {
                for (Vehicle vehicle : vehicleSpawner.getVehicles()) {
                    vehicle.setVelocity(0);
                }
            } else {
                for (Vehicle vehicle : vehicleSpawner.getVehicles()) {
                    vehicle.move();
                }
            }
            this.repaint();
        });
        moveTimer.start();


        // ---------------------- Vehicles ----------------------


        // ---------------------- Static objects ----------------------
        roadN = new Road(width*0.5, height*0.18, width*0.25, height*0.4, 1);
        roadE = new Road(width*0.82, height*0.5, width*0.4, height*0.25, 2);
        roadS = new Road(width*0.5, height*0.82, width*0.25, height*0.4, 1);
        roadW = new Road(width*0.18, height*0.5, width*0.4, height*0.25, 2);
        intersection = new Road(width*0.5, height*0.5, width*0.25, height*0.25, 0);

        trafficLight = new TrafficLight(350, 200);
        pothole = new Pothole(5, 5);
        explosion = new Explosion(200, 550, this);  // pass panel for callbacks/repaint
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);            // paints JPanel stuff like the background

        Graphics2D g2d = (Graphics2D) g;    // for our 2D graphics components

        // draw components
        roadN.draw(g2d);
        roadE.draw(g2d);
        roadS.draw(g2d);
        roadW.draw(g2d);
        intersection.draw(g2d);
        pothole.draw(g2d);
        trafficLight.draw(g2d);
        explosion.draw(g2d);    // test explosion !!!!! remove this to not show explosion :(

        for (Vehicle vehicle : vehicleSpawner.getVehicles()) {
            vehicle.draw(g2d);
        }
    }

}

