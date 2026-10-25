import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

class Panel extends JPanel implements KeyListener {
    private int width, height;
    private boolean pausedForResize;

    VehicleSpawner vehicleSpawner;
    Road roadN, roadE, roadS, roadW, intersection;
    Pothole pothole;
    SpeedCamera speedCamera;
    TrafficLight trafficLight1, trafficLight2, trafficLight3, trafficLight4;
    StopLine stopLine1, stopLine2, stopLine3, stopLine4;
    ArrayList<StopLine> stopLineArr = new ArrayList<>();
    Explosion explosion;
    TrafficLightController trafficLightController;
    BusStop busStop;
    Statistics stats;

    // panel creates VehicleSpawner, so also passes any invalid config up
    Panel(int w, int h) throws SimulationConfigurationException {
        this.width = w;
        this.height = h;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(63, 155, 11));
        this.addKeyListener(this);
        this.setFocusable(true);

        // ---------------------- Vehicle Spawn Timers ----------------------
        vehicleSpawner = new VehicleSpawner(width, height, 30); // % chance
        Timer vehicleSpawnTimer = new Timer(1390, e -> {           // attempt freq
            // pause spawner when paused for resize 
            if (!pausedForResize) {
                vehicleSpawner.spawn();
            }
        });
        vehicleSpawnTimer.start();
        Timer vehicleDespawnTimer = new Timer(20000, e -> {
            // pause spawner when paused for resize
            if (!pausedForResize) {
                vehicleSpawner.despawn();
            }
        });
        vehicleDespawnTimer.start();

        
        // ---------------------- Vehicle Acceleration Timers ----------------------
        Timer vehicleAccelTimer = new Timer(1000, e -> {
            // pause acceleration when paused for resize
            if (pausedForResize) {
                return;
            }
            for (EntityStore<Vehicle> sublist : vehicleSpawner.getVehicleList()) {
                for (Vehicle vehicle : sublist.getEntities()) {
                    if (vehicle.velocity < vehicle.topSpeed) {
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
            if (trafficLight1.getLightState() == 2) {
                stopLine1.setActive();
                /*for (Vehicle vehicle : vehicleSpawner.getVehicleList()) {
                    vehicle.setVelocity(0);
                }*/
            } else {
                stopLine1.setInactive();
                /*for (Vehicle vehicle : vehicleSpawner.getVehicleList()) {
                    vehicle.move();
                }*/
            }
            
            ArrayList<Vehicle> stopAtColl = CollisionDetection.checkVehicleCollisions(vehicleSpawner.getVehicleList());
            ArrayList<Vehicle> stopAtLine = CollisionDetection.checkVehicleAtStopLine(stopLineArr, vehicleSpawner.getVehicleList());

            for (Vehicle vehicle : stopAtColl) {
                vehicle.setVelocity(0);
            }

            for (EntityStore<Vehicle> sublist : vehicleSpawner.getVehicleList()) {
                for (Vehicle vehicle : sublist.getEntities()) {
                    if (!shouldStopAtRed(vehicle)) {
                        vehicle.move();
                    } else {
                        vehicle.velocity = 0;
                    }
                vehicle.updateHitBox();
                }
            }
            this.repaint();
        });
        moveTimer.start();

        
        // ---------------------- Static objects ----------------------
        roadN = new Road(width*0.5, height*0.18, width*0.25, height*0.4, 1);
        roadE = new Road(width*0.82, height*0.5, width*0.4, height*0.25, 2);
        roadS = new Road(width*0.5, height*0.82, width*0.25, height*0.4, 1);
        roadW = new Road(width*0.18, height*0.5, width*0.4, height*0.25, 2);
        intersection = new Road(width*0.5, height*0.5, width*0.25, height*0.25, 0);
        // draw stop line only at end facing intersection.. idk man 
        roadN.setStopLines(false, true);
        roadE.setStopLines(true, false);
        roadS.setStopLines(true, false);
        roadW.setStopLines(false, true);

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
        pothole = new Pothole(8, 8);
        speedCamera = new SpeedCamera(610, 240);
        explosion = new Explosion(200, 550, this);  // pass panel for callbacks/repaint
        stats = new Statistics();
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

    // extend road and intersection to resized window 
    private void updateViewport(double left, double top, double right, double bottom) {
        double intersectionLeft = width * 0.375;
        double intersectionRight = width * 0.625;
        double intersectionTop = height * 0.375;
        double intersectionBottom = height * 0.625;
        double roadWidth = width * 0.25;
        double roadHeight = height * 0.25;

        // vertical roads
        roadN.setBounds(intersectionLeft, top, roadWidth, intersectionTop - top);
        roadS.setBounds(intersectionLeft, intersectionBottom, roadWidth, bottom - intersectionBottom);
        // horizontal roads
        roadW.setBounds(left, intersectionTop, intersectionLeft - left, roadHeight);
        roadE.setBounds(intersectionRight, intersectionTop, right - intersectionRight, roadHeight);
        // spawning and despawning
        vehicleSpawner.updateViewportBounds(left, top, right, bottom);
        stats.updateViewportBounds(width, height);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);            // paints JPanel stuff like the background
        // draw disposable graphics copy so swing one is not changed
        Graphics2D g2d = (Graphics2D) g.create();

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

        Graphics2D uncentredG2D = (Graphics2D) g2d.create();    // for components that should be placed relative to 0,0

        // view transform only; physics are in logical units
        g2d.translate(xOffset, yOffset);
        g2d.scale(scale, scale);
        uncentredG2D.scale(scale, scale);

        // preserve curves and images when assets are enlarged etc 
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        uncentredG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        uncentredG2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // draw components
        roadW.draw(g2d);
        roadS.draw(g2d);
        roadE.draw(g2d);
        roadN.draw(g2d);
        intersection.draw(g2d);
        pothole.draw(g2d);
        speedCamera.draw(g2d);
        trafficLight1.draw(g2d);
        trafficLight2.draw(g2d);
        trafficLight3.draw(g2d);
        trafficLight4.draw(g2d);
        explosion.draw(g2d);    // test explosion !!!!! remove this to not show explosion :(
        busStop.draw(g2d);

        for (EntityStore<Vehicle> sublist : vehicleSpawner.getVehicleList()) {
            for (Vehicle vehicle : sublist.getEntities()) {
                vehicle.draw(g2d);
            }
        }
        stats.draw(uncentredG2D);

        // release graphics copy
        g2d.dispose();

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

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
