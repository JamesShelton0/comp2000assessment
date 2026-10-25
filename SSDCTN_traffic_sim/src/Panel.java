import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.IdentityHashMap;
import java.util.Map;

class Panel extends JPanel implements KeyListener {
    private int width, height;
    private boolean pausedForResize;

    // pothole values
    private static final int VEHICLES_BEFORE_POTHOLE = 20;
    private static final double POTHOLE_GROWTH_CHANCE = 0.005;
    private static final double INITIAL_POTHOLE_SIZE = 12;
    
    VehicleSpawnerTwo vehicleSpawner;
    VehicleDespawner vehicleDespawner;
    private List<EntityStore<Vehicle>> activeVehicles;
    Road roadN, roadE, roadMid, roadW, intersection, roadOnFarRight; // upper road
    Road roadE2, roadS2, roadW2, intersection2;   // lower road
    Pothole pothole;
    SpeedCamera speedCamera;
    TrafficLight trafficLight1, trafficLight2, trafficLight3, trafficLight4, trafficLight5, trafficLight6, trafficLight7, trafficLight8;
    StopLine stopLine1, stopLine2, stopLine3, stopLine4;
    ArrayList<StopLine> stopLineArr = new ArrayList<>();
    // active explosions are temporary traffic-blocking
    private final List<Explosion> explosions = new ArrayList<>();
    TrafficLightController trafficLightController;
    BusStop busStop;
    Graph g;
    Statistics stats;

    // panel creates VehicleSpawner, so also passes any invalid config up
    Panel(int w, int h) throws SimulationConfigurationException, Exception {
        g = new Graph();
        this.width = w;
        this.height = h;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(63, 155, 11));
        // remove any possible stale pixels
        this.setOpaque(true);
        this.setDoubleBuffered(true);

        this.addKeyListener(this);
        this.setFocusable(true);

        stats = new Statistics();

        // ---------------------- Vehicle Spawn Timers ----------------------
        vehicleSpawner = new VehicleSpawnerTwo(w, h, 30, g); // % chance
        vehicleDespawner = new VehicleDespawner();
        Timer vehicleSpawnTimer = new Timer(1390, e -> {        // attempt freq
            // pause spawner when paused for resize 
            if (!pausedForResize) {
                vehicleSpawner.spawn();
                activeVehicles = vehicleSpawner.getVehicles();
            }
        });
        vehicleSpawnTimer.start();
        Timer vehicleDespawnTimer = new Timer(10000, e -> {
            // pause spawner when paused for resize
            if (!pausedForResize) {
                vehicleDespawner.despawnVehicle(activeVehicles);
            }
        });
        vehicleDespawnTimer.start();

        
        // ---------------------- Vehicle Acceleration Timers ----------------------
        Timer vehicleAccelTimer = new Timer(1000, e -> {
            // pause acceleration when paused for resize
            if (pausedForResize) {
                return;
            }
            for (EntityStore<Vehicle> sublist : vehicleSpawner.getVehicles()) {
                for (Vehicle vehicle : sublist.getEntities()) {

                    //Do not accelerate the bus while it is stopped at the bus stop
                    if (vehicle instanceof Bus && ((Bus) vehicle).isStoppedAtBusStop()){
                        continue;
                    }
                  
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
            if (pausedForResize) {
                this.repaint();
                return;
            }

            explosions.removeIf(Explosion::isFinished);
            Vehicle[] collision = CollisionDetection.findCollision(vehicleSpawner.getVehicles());
            if (collision != null) {
                createExplosion(collision[0], collision[1]);
            }
            
            // build each vehicles movement before applying distances
            Map<Vehicle, Double> desiredMovements = new IdentityHashMap<>();
            Map<Vehicle, Boolean> environmentBlocks = new IdentityHashMap<>();
            for (EntityStore<Vehicle> sublist : vehicleSpawner.getVehicles()) {
                for (Vehicle vehicle : sublist.getEntities()) {
                    //Bus stop behaviour
                    if (vehicle instanceof Bus){
                        Bus bus = (Bus) vehicle;

                        bus.checkBusStop(busStop);
                        bus.waitAtBusStop();
                        bus.leaveBusStop();
                    }
                    
                    // shouldStopAtRed handles lights until stop-line collision logic is implemented
                    boolean environmentBlocked = shouldStopAtRed(vehicle)
                        || pothole.shouldBlock(vehicle)
                        || isBlockedByExplosion(vehicle);
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

        
        // ---------------------- Static objects ----------------------
        // road positions and dimensions are specified in updateViewport()
        roadN = new Road(1);
        roadE = new Road(2);
        roadMid = new Road(1);
        roadW = new Road(2);
        roadE2 = new Road(2);
        roadS2 = new Road(1);
        roadW2 = new Road(2);
        intersection = new Road(0);
        intersection2 = new Road(0);

        // draw stop line only at end facing intersection.. idk man 
        roadN.setStopLines(false, true);
        roadE.setStopLines(true, false);
        roadE2.setStopLines(true, false);
        roadMid.setStopLines(true, true);
        roadW.setStopLines(false, true);
        roadW2.setStopLines(false, true);
        roadS2.setStopLines(true, false);

        this.trafficLight1 = new TrafficLight(250, 10);  // top left
        this.trafficLight2 = new TrafficLight(500, 10);  // top right
        this.trafficLight3 = new TrafficLight(250, 305); // bottom left
        this.trafficLight4 = new TrafficLight(500, 305); // bottom right
        this.trafficLight5 = new TrafficLight(250, 405);  // top left
        this.trafficLight6 = new TrafficLight(500, 405);  // top right
        this.trafficLight7 = new TrafficLight(250, 700); // bottom left
        this.trafficLight8 = new TrafficLight(500, 700); // bottom right
        trafficLightController = new TrafficLightController(
           trafficLight1,
           trafficLight2,
           trafficLight3,
           trafficLight4,
           trafficLight5,
           trafficLight6,
           trafficLight7,
           trafficLight8
        );
        trafficLightController.start();
       
        this.busStop = new BusStop(150, 310);//bottom left bus stop

        // place pothole halfway along road
        pothole = new Pothole(
            width * 0.57,
            height * 0.6,
            INITIAL_POTHOLE_SIZE,
            INITIAL_POTHOLE_SIZE,
            VEHICLES_BEFORE_POTHOLE,
            POTHOLE_GROWTH_CHANCE
        );
        speedCamera = new SpeedCamera(610, 320);
        //explosion = new Explosion(200, 550, this);  // pass panel for callbacks/repaint
        

    }

    public void beginResizePause() {
        if (!pausedForResize) {
            pausedForResize = true;
            trafficLightController.pause();
        }
        repaint();
    }

    public void endResizePause() {
        if (pausedForResize) {
            pausedForResize = false;
            trafficLightController.resume();
            repaint();
        }
    }

    private boolean shouldStopAtRed(Vehicle vehicle) {

        Point p = vehicle.hitBox.getPointAhead();

        double x = p.getX();
        double y = p.getY();

        int direction = (int) Math.round(vehicle.getDirection());

        double topIntersection = height * 0.125;
        double bottomIntersection = height * 0.375;
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

    private void createExplosion(Vehicle firstVehicle, Vehicle secondVehicle) {
        // place explosion halfway between vehicles then remove them 
        double x = (firstVehicle.getPosition().getX() + secondVehicle.getPosition().getX()) / 2;
        double y = (firstVehicle.getPosition().getY() + secondVehicle.getPosition().getY()) / 2;
        explosions.add(new Explosion((int) x, (int) y, this));
        removeVehicle(firstVehicle);
        removeVehicle(secondVehicle);
    }

    private void removeVehicle(Vehicle vehicle) {
        for (EntityStore<Vehicle> lane : vehicleSpawner.getVehicles()) {
            lane.remove(vehicle);
        }
    }

    private boolean isBlockedByExplosion(Vehicle vehicle) {
        // stop vehicles near explosion 
        for (Explosion explosion : explosions) {
            if (explosion.blocks(vehicle.getPosition())
                    || explosion.blocks(vehicle.getHitBox().getPointAhead())) {
                return true;
            }
        }
        return false;
    }

    // extend road and intersection to resized window 
    private void updateViewport(double left, double top, double right, double bottom) {
        double intersectionLeft1 = width * 0.375;
        double intersectionRight1 = width * 0.625;
        double intersectionTop1 = height * (0.25-0.125);
        double intersectionBottom1 = height * (0.25+0.125);

        double intersectionLeft2 = width * 0.375;
        double intersectionRight2 = width * 0.625;
        double intersectionTop2 = height * (0.75-0.125);
        double intersectionBottom2 = height * (0.75+0.125);

        double roadWidth = width * 0.25;
        double roadHeight = height * 0.25;

        // intersections
        intersection.setBounds(intersectionLeft1, intersectionTop1, roadWidth, roadHeight);
        intersection2.setBounds(intersectionLeft2, intersectionTop2, roadWidth, roadHeight);

        // vertical roads
        roadN.setBounds(intersectionLeft1, top, roadWidth, intersectionTop1 - top);
        roadMid.setBounds(intersectionLeft1, intersectionBottom1, roadWidth, intersectionTop2 - intersectionBottom1);
        roadS2.setBounds(intersectionLeft2, intersectionBottom2, roadWidth, bottom - intersectionBottom2);

        // horizontal roads
        roadW.setBounds(left, intersectionTop1, intersectionLeft1 - left, roadHeight);
        roadW2.setBounds(left, intersectionTop2, intersectionLeft2 - left, roadHeight);
        roadE.setBounds(intersectionRight1, intersectionTop1, right - intersectionRight1, roadHeight);
        roadE2.setBounds(intersectionRight2, intersectionTop2, right - intersectionRight2, roadHeight);

        // spawning and despawning
        //vehicleSpawner.updateViewportBounds(left, top, right, bottom);
        stats.updateViewportBounds(width, height);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);            // paints JPanel stuff like the background
        // draw disposable graphics copy so swing one is not changed
        Graphics2D g2d = (Graphics2D) g.create();
        // preserve curves and images when assets are enlarged etc 
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // use one uniform scale so all assets keep proportions
        double scale = Math.min(getWidth() / (double) width, getHeight() / (double) height);

        // centre simulation when window and simulation have different aspect ratios
        double xOffset = (getWidth() - width * scale) / 2.0;
        double yOffset = (getHeight() - height * scale) / 2.0;

        // convert panel edges into coords 
        double viewportLeft = -xOffset / scale;
        double viewportTop = -yOffset / scale;
        double viewportRight = (getWidth() - xOffset) / scale;
        double viewportBottom = (getHeight() - yOffset) / scale;

        // synchronise all size dependent simulation bounds before resizing
        updateViewport(viewportLeft, viewportTop, viewportRight, viewportBottom);

        Graphics2D g2dUncentred = (Graphics2D) g2d.create();    // for components that should be placed relative to 0,0

        // view transform only; physics are in logical units
        g2d.translate(xOffset, yOffset);
        g2d.scale(scale, scale);
        g2dUncentred.scale(scale, scale);

        // draw components
        roadW.draw(g2d);
        roadMid.draw(g2d);
        roadE.draw(g2d);
        roadN.draw(g2d);
        intersection.draw(g2d);
        intersection2.draw(g2d);
        roadW2.draw(g2d);
        roadS2.draw(g2d);
        roadE2.draw(g2d);

        pothole.draw(g2d);
        speedCamera.draw(g2d);
        trafficLight1.draw(g2d);
        trafficLight2.draw(g2d);
        trafficLight3.draw(g2d);
        trafficLight4.draw(g2d);
        trafficLight5.draw(g2d);
        trafficLight6.draw(g2d);
        trafficLight7.draw(g2d);
        trafficLight8.draw(g2d);
        busStop.draw(g2d);
        //explosion.draw(g2d);    // test explosion !!!!! remove this to not show explosion :(
        

        for (EntityStore<Vehicle> sublist : vehicleSpawner.getVehicles()) {
            for (Vehicle vehicle : sublist.getEntities()) {
                vehicle.draw(g2d);
            }
        }

        for (Explosion explosion : explosions) {
            explosion.draw(g2d);
        }

        stats.draw(g2dUncentred);

        // release graphics copy
        g2d.dispose();
        Toolkit.getDefaultToolkit().sync();

        // prevent pause notice from looking weird 
        if (pausedForResize) {
            drawPauseOverlay((Graphics2D) g);
        }
    }

    // draw pause overlay
    private void drawPauseOverlay(Graphics2D g) {
        Graphics2D overlay = (Graphics2D) g.create();
        overlay.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // dim background
        overlay.setColor(new Color(0, 0, 0, 105));
        overlay.fillRect(0, 0, getWidth(), getHeight());

        int cardWidth = Math.min(330, Math.max(230, getWidth() - 40));
        int cardHeight = 116;
        int cardX = (getWidth() - cardWidth) / 2;
        int cardY = (getHeight() - cardHeight) / 2;

        // draw pause card
        overlay.setColor(new Color(24, 29, 36, 235));
        overlay.fillRoundRect(cardX, cardY, cardWidth, cardHeight, 22, 22);
        overlay.setColor(new Color(255, 255, 255, 210));
        overlay.setStroke(new BasicStroke(2f));
        overlay.drawRoundRect(cardX, cardY, cardWidth, cardHeight, 22, 22);

        // draw pause symbol
        int pauseX = cardX + 28;
        int pauseY = cardY + 28;
        overlay.setColor(Color.WHITE);
        overlay.fillRoundRect(pauseX, pauseY, 12, 38, 4, 4);
        overlay.fillRoundRect(pauseX + 22, pauseY, 12, 38, 4, 4);

        overlay.setFont(getFont().deriveFont(Font.BOLD, 17f));
        overlay.drawString("SIMULATION PAUSED", cardX + 80, cardY + 48);
        overlay.setFont(getFont().deriveFont(Font.PLAIN, 13f));
        overlay.setColor(new Color(220, 225, 230));
        overlay.drawString("Window is being resized.", cardX + 80, cardY + 73);
        overlay.dispose();
    }

    // Key stroke listeners
    @Override public void keyPressed(KeyEvent e) {
        System.out.println("Key pressed: " + e.getKeyChar());
        if (e.getKeyCode() == KeyEvent.VK_S) stats.keyPressed();
    }

    // Not used rn but KeyListener interface requires implementations
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
