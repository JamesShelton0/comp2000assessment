import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.WeakHashMap;

public class Pothole {
    // keep swallow visible
    private static final long SWALLOW_DURATION_MS = 1500;
    // so swallow does not consume the whole screen
    private static final double MAX_SWALLOW_SCALE = 6.0;

    // store centre so pothole is positioned right
    private final double x;
    private final double y;
    private double width;
    private double height;

    // formation and swallow chance
    private final int vehiclesBeforeFormation;
    private final double swallowChance;
    private final Random random;

    // hashmaps remember counted vehicles and prevent repeat swallow rolls for the same vehicle
    private final Set<Vehicle> countedVehicles =
        Collections.newSetFromMap(new WeakHashMap<Vehicle, Boolean>());
    private final Set<Vehicle> encounteredVehicles =
        Collections.newSetFromMap(new WeakHashMap<Vehicle, Boolean>());

    private boolean active;
    private boolean swallowed;
    private int vehiclePassCount;
    // track vehicle victim so that approaching vehicles stop
    private Vehicle vehicleBeingSwallowed;
    private long swallowingStartedAt;
    private long swallowingEndsAt;

    public Pothole(double x, double y, double width, double height,
                   int vehiclesBeforeFormation, double swallowChance) {
        // throw errors in case etc
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Pothole dimensions must be positive.");
        }
        if (vehiclesBeforeFormation <= 0) {
            throw new IllegalArgumentException("Vehicles before formation must be positive.");
        }
        if (swallowChance < 0 || swallowChance > 1) {
            throw new IllegalArgumentException("Pothole swallow chance must be between 0 and 1.");
        }

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.vehiclesBeforeFormation = vehiclesBeforeFormation;
        this.swallowChance = swallowChance;
        this.random = new Random();
    }

    // records traffic, form pothole and reports swallowing
    public boolean update(Vehicle vehicle) {
        // remove victim only after swallow is complete
        if (vehicle == vehicleBeingSwallowed
                && System.currentTimeMillis() >= swallowingEndsAt) {
            vehicleBeingSwallowed = null;
            return true;
        }

        // count vehicle once it reaches a point
        if (!active && hasPassedFormationPoint(vehicle) && countedVehicles.add(vehicle)) {
            vehiclePassCount++;
            if (vehiclePassCount >= vehiclesBeforeFormation) {
                active = true;
            }
        }

        // each passing vehicle only gets one chance at being swallowed
        if (active && !swallowed && vehicleBeingSwallowed == null
                && overlaps(vehicle) && encounteredVehicles.add(vehicle)
                && random.nextDouble() < swallowChance) {
            startSwallowing(vehicle);
        }

        return false;
    }

    public boolean shouldBlock(Vehicle vehicle) {
        // nothing is obstructed when swallowing is not being done
        if (vehicleBeingSwallowed == null) {
            return false;
        }
        if (vehicle == vehicleBeingSwallowed) {
            return true;
        }

        if (Math.round(vehicle.getDirection())
                != Math.round(vehicleBeingSwallowed.getDirection())) {
            return false;
        }

        double directionRadians = Math.toRadians(vehicle.getDirection());
        double deltaX = x - vehicle.getPosition().getX();
        double deltaY = y - vehicle.getPosition().getY();
        double forwardGap = deltaX * Math.cos(directionRadians)
            + deltaY * Math.sin(directionRadians);
        if (forwardGap < 0) {
            return false;
        }

        // keep adjacent lanes moving and stop vehicles before they reach pothole
        double lateralGap = Math.abs(
            -deltaX * Math.sin(directionRadians)
            + deltaY * Math.cos(directionRadians)
        );
        double laneOverlapDistance = getMaximumSwallowWidth() / 2
            + vehicle.getHitBox().getLateralExtent();
        if (lateralGap > laneOverlapDistance) {
            return false;
        }

        // include next movement step so fast vehicles cant jump across the pothole
        double blockingDistance = getMaximumSwallowHeight() / 2
            + vehicle.getHitBox().getForwardExtent()
            + vehicle.velocity
            + 2;
        return forwardGap <= blockingDistance;
    }

    public boolean isSwallowing() {
        // for tests can ignore mostly
        return vehicleBeingSwallowed != null;
    }

    private boolean hasPassedFormationPoint(Vehicle vehicle) {
        Point position = vehicle.getPosition();
        int direction = (int) Math.round(vehicle.getDirection());

        // requires vehicle to be in lane before checking how far along it is
        if (direction == 90 || direction == 270) {
            double laneTolerance = (width + vehicle.width) / 2;
            if (Math.abs(position.getX() - x) > laneTolerance) {
                return false;
            }
            return direction == 90 ? position.getY() >= y : position.getY() <= y;
        }

        // keep same behaviour for horizontal lane
        double laneTolerance = (height + vehicle.width) / 2;
        if (Math.abs(position.getY() - y) > laneTolerance) {
            return false;
        }
        return direction == 0 ? position.getX() >= x : position.getX() <= x;
    }

    private boolean overlaps(Vehicle vehicle) {
        Point position = vehicle.getPosition();

        return Math.abs(position.getX() - x) <= (width + vehicle.width) / 2
            && Math.abs(position.getY() - y) <= (height + vehicle.height) / 2;
    }

    private void startSwallowing(Vehicle vehicle) {
        // keep resting dimensions unchanged
        swallowed = true;
        vehicleBeingSwallowed = vehicle;
        swallowingStartedAt = System.currentTimeMillis();
        swallowingEndsAt = swallowingStartedAt + SWALLOW_DURATION_MS;
    }

    private double getCurrentSwallowScale() {
        // yay maths to make a smooth curve expansion
        if (vehicleBeingSwallowed == null) {
            return 1.0;
        }
        double progress = (double) (System.currentTimeMillis() - swallowingStartedAt)
            / SWALLOW_DURATION_MS;
        progress = Math.max(0, Math.min(1, progress));
        return 1 + (MAX_SWALLOW_SCALE - 1) * Math.sin(Math.PI * progress);
    }

    double getDisplayWidth() {
        // share animated swallow width
        return width * getCurrentSwallowScale();
    }

    private double getMaximumSwallowWidth() {
        // reserve enough horizontal space for animations largest frame
        return width * MAX_SWALLOW_SCALE;
    }

    private double getMaximumSwallowHeight() {
        // reserve enough vertical space for animations largest frame
        return height * MAX_SWALLOW_SCALE;
    }

    protected void draw(Graphics2D g2d){
        // dont show pothole if not enough vehicles have passed
        if (!active) {
            return;
        }

        // calculate animation time once so width and height use the same frame
        double scale = getCurrentSwallowScale();
        double displayWidth = width * scale;
        double displayHeight = height * scale;
        Ellipse2D.Double shape = new Ellipse2D.Double(
            x - displayWidth / 2,
            y - displayHeight / 2,
            displayWidth,
            displayHeight
        );
        g2d.setColor(Color.BLACK);
        g2d.fill(shape);
    }
}
