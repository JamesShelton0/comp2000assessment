import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

class Panel extends JPanel {
    // pothole values
    private static final int VEHICLES_BEFORE_POTHOLE = 20;
    private static final double POTHOLE_GROWTH_CHANCE = 0.005;
    private static final double INITIAL_POTHOLE_SIZE = 12;

    private int width, height;
    VehicleSpawner vehicleSpawner;
    Road roadN, roadE, roadS, roadW, intersection, roadOnFarRight;
    Pothole pothole;
    SpeedCamera speedCamera;
    TrafficLight trafficLight1, trafficLight2, trafficLight3, trafficLight4;
    StopLine stopLine1, stopLine2, stopLine3, stopLine4;
    ArrayList<StopLine> stopLineArr = new ArrayList<>();
    Explosion explosion;
    TrafficLightController trafficLightController;
    BusStop busStop;

    // panel creates VehicleSpawner, so also passes any invalid config up
    Panel(int w, int h) throws SimulationConfigurationException {
        this.width = w;
        this.height = h;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(63, 155, 11));
        // remove any possible stale pixels
        this.setOpaque(true);
        this.setDoubleBuffered(true);


        // ---------------------- Vehicle Spawn Timers ----------------------
        vehicleSpawner = new VehicleSpawner(width, height, 30); // % chance
        Timer vehicleSpawnTimer = new Timer(1390, e -> {           // attempt freq
            vehicleSpawner.spawn();
        });
        vehicleSpawnTimer.start();
        Timer vehicleDespawnTimer = new Timer(20000, e -> {
            vehicleSpawner.despawn();
        });
        vehicleDespawnTimer.start();

        
        // ---------------------- Vehicle Acceleration Timers ----------------------
        Timer vehicleAccelTimer = new Timer(1000, e -> {
            for (EntityStore<Vehicle> sublist : vehicleSpawner.getVehicles()) {
                for (Vehicle vehicle : sublist.getEntities()) {
                    // dont build up speed when movement is blocked
                    if (!vehicle.isMovementBlocked() && vehicle.velocity < vehicle.topSpeed) {
                        vehicle.accelerate(vehicle.accelerationRate);
                    }
                }
            }
        });
        vehicleAccelTimer.start();


        // ---------------------- Movement Timer ----------------------
        // Vehicle movement & repaint timer (~60fps)
        Timer moveTimer = new Timer(16, e -> {
            if (trafficLight1.getLightState() == 2) {
                stopLine1.setActive();
                /*for (Vehicle vehicle : vehicleSpawner.getVehicles()) {
                    vehicle.setVelocity(0);
                }*/
            } else {
                stopLine1.setInactive();
                /*for (Vehicle vehicle : vehicleSpawner.getVehicles()) {
                    vehicle.move();
                }*/
            }
            
            // build each vehicles movement before applying distances
            Map<Vehicle, Double> desiredMovements = new IdentityHashMap<>();
            Map<Vehicle, Boolean> environmentBlocks = new IdentityHashMap<>();
            for (EntityStore<Vehicle> sublist : vehicleSpawner.getVehicles()) {
                for (Vehicle vehicle : sublist.getEntities()) {
                    // shouldStopAtRed handles lights until stop-line collision logic is implemented
                    boolean environmentBlocked = shouldStopAtRed(vehicle)
                        || pothole.shouldBlock(vehicle);
                    environmentBlocks.put(vehicle, environmentBlocked);
                    desiredMovements.put(
                        vehicle,
                        // keep normal speed; rendering quality handles visual smoothing separately
                        environmentBlocked ? 0 : vehicle.velocity
                    );
                }
            }

            // let queues match movement ahead instead of switching between stopping and full speed
            Map<Vehicle, Double> allowedMovements = CollisionDetection.calculateAllowedMovements(
                vehicleSpawner.getVehicles(),
                desiredMovements
            );

            // only one vehicle can be swallowed so a temporary list is not needed
            Vehicle swallowedVehicle = null;
            for (EntityStore<Vehicle> sublist : vehicleSpawner.getVehicles()) {
                for (Vehicle vehicle : sublist.getEntities()) {
                    double allowedMovement = allowedMovements.getOrDefault(vehicle, 0.0);
                    // pause acceleration whenever an obstacle limits the desired movement
                    double desiredMovement = desiredMovements.getOrDefault(vehicle, 0.0);
                    boolean movementBlocked = environmentBlocks.getOrDefault(vehicle, false)
                        || allowedMovement + 0.0001 < desiredMovement;
                    vehicle.setMovementBlocked(movementBlocked);

                    if (allowedMovement > 0) {
                        vehicle.move(allowedMovement);
                    }
                    vehicle.updateHitBox();

                    // let pothole count pass and flag swallowing events
                    if (pothole.update(vehicle)) {
                        swallowedVehicle = vehicle;
                    }
                }
            }

            // remove the swallowed vehicle after movement iteration finishes
            if (swallowedVehicle != null) {
                for (EntityStore<Vehicle> sublist : vehicleSpawner.getVehicles()) {
                    sublist.remove(swallowedVehicle);
                }
            }
            this.repaint();
        });
        moveTimer.start();

        // Timer moveTimer2 = new Timer(16, e -> {
        //     for (Vehicle vehicle : vehicleSpawner.getVehicles()) {
        //         if (!shouldStopAtRed(vehicle)) {
        //             vehicle.move();
        //         }
        //     }
        //     this.repaint();
        // });
        //moveTimer2.start();

        
        // ---------------------- Static objects ----------------------
        roadN = new Road(width*0.5, height*0.18, width*0.25, height*0.4, 1);
        roadE = new Road(width*0.82, height*0.5, width*0.4, height*0.25, 2);
        roadS = new Road(width*0.5, height*0.82, width*0.25, height*0.4, 1);
        roadW = new Road(width*0.18, height*0.5, width*0.4, height*0.25, 2);
        intersection = new Road(width*0.5, height*0.5, width*0.25, height*0.25, 0);
        roadOnFarRight = new Road(width*1.0, height*0.18, width*0.25, height*0.4, 1);

        this.stopLine1 = new StopLine(new Point(100, 100), new Point(100, 200), 0.0);
        this.stopLine2 = new StopLine(null, null, 90.0);
        this.stopLine3 = new StopLine(null, null, 180.0);
        this.stopLine4 = new StopLine(null, null, 270.0);
        stopLineArr.add(stopLine1);
        stopLineArr.add(stopLine2);
        stopLineArr.add(stopLine3);
        stopLineArr.add(stopLine4);

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
       
        this.busStop = new BusStop(260, 30);//top left bus stop

        // place pothole halfway along road
        pothole = new Pothole(
            width * 0.57,
            height * 0.18,
            INITIAL_POTHOLE_SIZE,
            INITIAL_POTHOLE_SIZE,
            VEHICLES_BEFORE_POTHOLE,
            POTHOLE_GROWTH_CHANCE
        );
        speedCamera = new SpeedCamera(610, 240);
        explosion = new Explosion(200, 550, this);  // pass panel for callbacks/repaint
        
        

    }


    private boolean shouldStopAtRed(Vehicle vehicle) {

        Point p = vehicle.hitBox.getPointAhead();

        double x = p.getX();
        double y = p.getY();

        int direction = (int) Math.round(vehicle.getDirection());

        double topIntersection = height * 0.375;
        double bottomIntersection = height * 0.625;
        double leftIntersection = width * 0.375;
        double rightIntersection = width * 0.625;

        double detectionDistance = 15;

        // Vehicle travelling SOUTH
        if (direction == 90) {
            if (trafficLight2.getLightState() == 2 || trafficLight2.getLightState() == 1) {
                return y >= topIntersection - detectionDistance
                        && y < topIntersection;
            }
        }

        // Vehicle travelling NORTH
        if (direction == 270) {
            if (trafficLight3.getLightState() == 2 || trafficLight3.getLightState() == 1) {
                return y <= bottomIntersection + detectionDistance
                        && y > bottomIntersection;
            }
        }

        // Vehicle travelling WEST
        if (direction == 180) {
            if (trafficLight4.getLightState() == 2 || trafficLight4.getLightState() == 1) {
                return x <= rightIntersection + detectionDistance
                        && x > rightIntersection;
            }
        }

        // Vehicle travelling EAST
        if (direction == 0) {
            if (trafficLight1.getLightState() == 2 || trafficLight1.getLightState() == 1) {
                return x >= leftIntersection - detectionDistance
                        && x < leftIntersection;
            }
        }

        return false;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);            // paints JPanel stuff like the background
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // draw components
        roadW.draw(g2d);
        roadS.draw(g2d);
        roadE.draw(g2d);
        roadN.draw(g2d);
        intersection.draw(g2d);
        roadOnFarRight.draw(g2d);
        pothole.draw(g2d);
        speedCamera.draw(g2d);
        trafficLight1.draw(g2d);
        trafficLight2.draw(g2d);
        trafficLight3.draw(g2d);
        trafficLight4.draw(g2d);
        explosion.draw(g2d);    // test explosion !!!!! remove this to not show explosion :(
        busStop.draw(g2d);

        for (EntityStore<Vehicle> sublist : vehicleSpawner.getVehicles()) {
            for (Vehicle vehicle : sublist.getEntities()) {
                vehicle.draw(g2d);
            }
        }

        g2d.dispose();
        Toolkit.getDefaultToolkit().sync();
    }

}