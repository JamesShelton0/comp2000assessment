import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VehicleSpawner {
    // Frame dimensions
    private int width, height;

    // spawn points
    private Point NORTH_SP;
    private Point EAST_SP;
    private Point SOUTH_SP;
    private Point WEST_SP;
    private Point EASTTOO_SP;
    private Point WESTTOO_SP;
    private final Point TEST_SP;

    // directions of travel
    private final double NORTH = 270;
    private final double EAST = 0;
    private final double SOUTH = 90;
    private final double WEST = 180;

    // number of vehicle types and declarations for their dimensions
    private final int VCL_TYPE_AMOUNT = 5;
    private double BUS_W, BUS_H;
    private double CAR_W, CAR_H;
    private double CYCLIST_W, CYCLIST_H;
    private double MOTORBIKE_W, MOTORBIKE_H;
    private double TRUCK_W, TRUCK_H;

    // a buffer for spawning/despawning vehicles to ensure they are entirely off screen
    private final int SPAWN_BUFFER;

    // track the current visible bounds so spawning and despawning follow window edges
    private double viewportLeft, viewportTop, viewportRight, viewportBottom;

    // spawn variables
    private int spawnChance;
    private Random rand;

    // List active vehicles with sublists based on spawn point (now uses EntityStore to reject unrelated object types at compile time (generics yay!))
    private List<EntityStore<Vehicle>> activeVehicles;
    private EntityStore<Vehicle> northSpawn, eastSpawn, southSpawn, westSpawn, eastTooSpawn, westTooSpawn;

    VehicleSpawner(int spawnChance) throws SimulationConfigurationException {
        if (spawnChance < 0 || spawnChance > 100) {
            throw new SimulationConfigurationException("Spawn chance must be between 0 and 100.");
        }
        this.width = App.width;     // provides app's bounds for determining spawn positions
        this.height = App.height;
        this.spawnChance = spawnChance;     // specified as a percentage
        SPAWN_BUFFER = (int) ((width+height)/2 * 0.15);
        
        // initialise viewport and spawn points to original panel bounds 
        updateViewportBounds(0, 0, width, height);

        TEST_SP = new Point(0,0);    // test spawn point

        assignDimensions();
        rand = new Random();

        northSpawn = new EntityStore<>();
        eastSpawn = new EntityStore<>();
        southSpawn = new EntityStore<>();
        westSpawn = new EntityStore<>();
        eastTooSpawn = new EntityStore<>();
        westTooSpawn = new EntityStore<>();

        activeVehicles = new ArrayList<>();
        activeVehicles.add(northSpawn);
        activeVehicles.add(eastSpawn);
        activeVehicles.add(southSpawn);
        activeVehicles.add(westSpawn);
        activeVehicles.add(eastTooSpawn);
        activeVehicles.add(westTooSpawn);
    }

    public void spawn() {
        if (rand.nextInt(100) < spawnChance) spawnHelper(NORTH_SP, SOUTH, 0);
        if (rand.nextInt(100) < spawnChance) spawnHelper(EAST_SP, WEST, 1);
        if (rand.nextInt(100) < spawnChance) spawnHelper(SOUTH_SP, NORTH, 2);
        if (rand.nextInt(100) < spawnChance) spawnHelper(WEST_SP, EAST, 3);
        if (rand.nextInt(100) < spawnChance) spawnHelper(EASTTOO_SP, WEST, 4);
        if (rand.nextInt(100) < spawnChance) spawnHelper(WESTTOO_SP, EAST, 5);
    }

    private void spawnHelper(Point spawnPoint, double direction, int sublistIndex) {
        int vehicleType = rand.nextInt(VCL_TYPE_AMOUNT);
            
        // prevents spawning when a vehicle is already in spawn zone
        int lastElement = activeVehicles.get(0).getEntities().size()-1;
        if (lastElement >= 0 && activeVehicles.get(0).get(lastElement).getPosition().getY() < viewportTop) {
            // System.out.println("VehicleSpawner: North spawn full - aborting.");
            vehicleType = -1;   // uses default case (doesn't create vehicle)
        }

        // System.out.println("north spawn succeeded. Vehicle type: "+vehicleType);
        switch (vehicleType) {
            case 0: // BUS
                Bus bus = new Bus(spawnPoint, BUS_W, BUS_H, direction);
                bus.setPosition(spawnPoint);
                bus.setDirection((float) direction);
                activeVehicles.get(sublistIndex).add(bus);
                break;
            
            case 1: // CAR
                Car car = new Car(spawnPoint, CAR_W, CAR_H, direction);
                car.setPosition(spawnPoint);
                car.setDirection((float) direction);
                activeVehicles.get(sublistIndex).add(car);
                break;

            case 2:  //CYCLIST
                Cyclist cyclist = new Cyclist(spawnPoint, CYCLIST_W, CYCLIST_H, direction);  // arbitrary arguments
                cyclist.setPosition(spawnPoint);
                cyclist.setDirection((float) direction);
                activeVehicles.get(sublistIndex).add(cyclist);
                break;
            
            case 3: // MOTORBIKE
                // construct at the north spawn so the initial hitbox matches its position
                Motorbike motorbike = new Motorbike(spawnPoint, MOTORBIKE_W, MOTORBIKE_H, direction);
                motorbike.setPosition(spawnPoint);
                motorbike.setDirection((float) direction);
                activeVehicles.get(sublistIndex).add(motorbike);
                break;

            case 4: // TRUCK
                Truck truck = new Truck(spawnPoint, TRUCK_W, TRUCK_H, direction);
                truck.setPosition(spawnPoint);
                truck.setDirection((float) direction);
                activeVehicles.get(sublistIndex).add(truck);
                break;
            default:
        }
    }

    public List<EntityStore<Vehicle>> getVehicles() {
        // return store's read-only typed view for Panel's movement and drawing loops
        return activeVehicles;
    }

    // move spawn / despawn boundaries to the coords of resized panel 
    public void updateViewportBounds(double left, double top, double right, double bottom) {
        viewportLeft = left;
        viewportTop = top;
        viewportRight = right;
        viewportBottom = bottom;

        // replace points so active vehicles never share a moving spawn coord
        NORTH_SP = new Point(width * 0.57, viewportTop - SPAWN_BUFFER);
        EAST_SP = new Point(viewportRight + SPAWN_BUFFER, height * 0.32);
        EASTTOO_SP = new Point(viewportRight + SPAWN_BUFFER, height*0.82);
        SOUTH_SP = new Point(width * 0.43, viewportBottom + SPAWN_BUFFER);
        WEST_SP = new Point(viewportLeft - SPAWN_BUFFER, height * 0.18);
        WESTTOO_SP = new Point(viewportLeft - SPAWN_BUFFER, height*0.68);
    }

    // run periodically to delete (make eligible for garbage collection) vehicles out of the frame bounds
    public void despawn() {
        // System.out.println("VehicleSpawner.despawn() called");
        for (EntityStore<Vehicle> sublist : activeVehicles) 
            despawnHelper(sublist);
    }
    
    // takes the N/E/S/W activeVehicle sublists 
    private void despawnHelper(EntityStore<Vehicle> sublist) {
        Iterator<Vehicle> iterator = sublist.modifyEntities().iterator();
        while (iterator.hasNext()) {
            Vehicle v = iterator.next();
            // remove vehicles only after they have passed beyond current visible window and buffer
            if (v.getPosition().getX() < viewportLeft-SPAWN_BUFFER
            || v.getPosition().getY() < viewportTop-SPAWN_BUFFER
            || v.getPosition().getX() > viewportRight+SPAWN_BUFFER
            || v.getPosition().getY() > viewportBottom+SPAWN_BUFFER) {
                // System.out.println("Vehicle despawned at " + v.getPosition());
                iterator.remove();
            }
        }
    }

    private void assignDimensions() {
        BUS_W       = width * 0.05;
        BUS_H       = height* 0.12;
        CAR_W       = width * 0.0375;
        CAR_H       = height* 0.0625;
        CYCLIST_W   = width * 0.05;
        CYCLIST_H   = height* 0.05;
        MOTORBIKE_W = width * 0.025;
        MOTORBIKE_H = height * 0.0375;
        TRUCK_W     = width * 0.1;
        TRUCK_H     = height* 0.08;
    }
}
