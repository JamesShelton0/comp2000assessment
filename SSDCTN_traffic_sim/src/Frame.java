import javax.swing.*;
import java.awt.*;

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

class Panel extends JPanel {
    private int width, height;
    VehicleSpawner vehicleSpawner;
    Road roadN, roadE, roadS, roadW, intersection;
    Pothole pothole;
    TrafficLight trafficLight1, trafficLight2, trafficLight3, trafficLight4;
    Explosion explosion;
    TrafficLightController trafficLightController;

    // panel creates VehicleSpawner, so also passes any invalid config up
    Panel(int w, int h) throws SimulationConfigurationException {
        this.width = w;
        this.height = h;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(63, 155, 11));


        // ---------------------- Vehicle Spawn Timers ----------------------
        vehicleSpawner = new VehicleSpawner(width, height, 30); // % chance
        Timer vehicleSpawnTimer = new Timer(2000, e -> {           // attempt freq
            vehicleSpawner.spawn();
        });
        vehicleSpawnTimer.start();

        Timer vehicleDespawnTimer = new Timer(20000, e -> {
            vehicleSpawner.despawn();
        });
        vehicleDespawnTimer.start();
        
        // ---------------------- Static objects ----------------------
        roadN = new Road(width*0.5, height*0.18, width*0.25, height*0.4, 1);
        roadE = new Road(width*0.82, height*0.5, width*0.4, height*0.25, 2);
        roadS = new Road(width*0.5, height*0.82, width*0.25, height*0.4, 1);
        roadW = new Road(width*0.18, height*0.5, width*0.4, height*0.25, 2);
        intersection = new Road(width*0.5, height*0.5, width*0.25, height*0.25, 0);

        this.trafficLight1 = new TrafficLight(250, 210); // top left
        this.trafficLight2 = new TrafficLight(500, 210); // top right
        this.trafficLight3 = new TrafficLight(250, 500); // bottom left
        this.trafficLight4 = new TrafficLight(500, 500); // bottom right

        trafficLightController = new TrafficLightController(
           trafficLight1,
           trafficLight2,
           trafficLight3,
           trafficLight4
        );

        trafficLightController.start();

        pothole = new Pothole(8, 8);
        explosion = new Explosion(200, 550, this);  // pass panel for callbacks/repaint


        // ---------------------- Movement Timer ----------------------

        // Vehicle movement & repaint timer (~60fps)
        Timer moveTimer = new Timer(16, e -> {

            for (Vehicle vehicle : vehicleSpawner.getVehicles()) {

                if (!shouldStopAtRed(vehicle)) {
                    vehicle.move();
                }

            }

            this.repaint();
        });

        moveTimer.start();
    }


    private boolean shouldStopAtRed(Vehicle vehicle) {

        Point p = vehicle.getPosition();

        double x = p.getX();
        double y = p.getY();

        int direction = (int) Math.round(vehicle.getDirection());

        double topIntersection = height * 0.375;
        double bottomIntersection = height * 0.625;
        double leftIntersection = width * 0.375;
        double rightIntersection = width * 0.625;

        double detectionDistance = 50;


        // Vehicle travelling SOUTH
        if (direction == 90) {

            if (trafficLight2.getLightState() == 2) {

                return y >= topIntersection - detectionDistance
                        && y < topIntersection;
            }
        }


        // Vehicle travelling NORTH
        if (direction == 270) {

            if (trafficLight3.getLightState() == 2) {

                return y <= bottomIntersection + detectionDistance
                        && y > bottomIntersection;
            }
        }


        // Vehicle travelling WEST
        if (direction == 180) {

            if (trafficLight4.getLightState() == 2) {

                return x <= rightIntersection + detectionDistance
                        && x > rightIntersection;
            }
        }


        // Vehicle travelling EAST
        if (direction == 0) {

            if (trafficLight1.getLightState() == 2) {

                return x >= leftIntersection - detectionDistance
                        && x < leftIntersection;
            }
        }


        return false;
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
        trafficLight1.draw(g2d);
        trafficLight2.draw(g2d);
        trafficLight3.draw(g2d);
        trafficLight4.draw(g2d);
        //explosion.draw(g2d);    // test explosion !!!!! remove this to not show explosion :(

        for (Vehicle vehicle : vehicleSpawner.getVehicles()) {
            vehicle.draw(g2d);
        }
    }

}