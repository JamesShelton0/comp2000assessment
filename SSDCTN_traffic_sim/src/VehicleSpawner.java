import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VehicleSpawner {
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

    private int width, height; // Frame dimensions

    // track the current visible bounds so spawning and despawning follow window edges
    private double viewportLeft, viewportTop, viewportRight, viewportBottom;

    // spawn variables
    private int spawnChance;
    private Random rand;

    // List active vehicles with sublists based on spawn point (now uses EntityStore to reject unrelated object types at compile time (generics yay!))
    private List<EntityStore<Vehicle>> activeVehicles;
    private EntityStore<Vehicle> northSpawn, eastSpawn, southSpawn, westSpawn, eastTooSpawn, westTooSpawn;

    VehicleSpawner(int w, int h, int spawnChance) throws SimulationConfigurationException {
        // validate at startup so bad settings create error
        if (w <= 0 || h <= 0) {
            throw new SimulationConfigurationException("Simulation dimensions must be positive.");
        }
        if (spawnChance < 0 || spawnChance > 100) {
            throw new SimulationConfigurationException("Spawn chance must be between 0 and 100.");
        }
        this.width = w;     // provides panel bounds for determining spawn positions
        this.height = h;
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
        // north spawn
        if (rand.nextInt(100) < spawnChance) {
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
                    Bus bus = new Bus(NORTH_SP, BUS_W, BUS_H, SOUTH);
                    bus.setPosition(NORTH_SP);
                    bus.setDirection((float) SOUTH);
                    activeVehicles.get(0).add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(NORTH_SP, CAR_W, CAR_H, SOUTH);
                    car.setPosition(NORTH_SP);
                    car.setDirection((float) SOUTH);
                    activeVehicles.get(0).add(car);
                    break;

                case 2:  //CYCLIST
                    Cyclist cyclist = new Cyclist(NORTH_SP, CYCLIST_W, CYCLIST_H, SOUTH);  // arbitrary arguments
                    cyclist.setPosition(NORTH_SP);
                    cyclist.setDirection((float) SOUTH);
                    activeVehicles.get(0).add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    // construct at the north spawn so the initial hitbox matches its position
                    Motorbike motorbike = new Motorbike(NORTH_SP, MOTORBIKE_W, MOTORBIKE_H, SOUTH);
                    motorbike.setPosition(NORTH_SP);
                    motorbike.setDirection((float) SOUTH);
                    activeVehicles.get(0).add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(NORTH_SP, TRUCK_W, TRUCK_H, SOUTH);
                    truck.setPosition(NORTH_SP);
                    truck.setDirection((float) SOUTH);
                    activeVehicles.get(0).add(truck);
                    break;
                default:
            }
        }

        // east spawn
        if (rand.nextInt(100) < spawnChance) {
            int vehicleType = rand.nextInt(VCL_TYPE_AMOUNT);
            
            // prevents spawning when a vehicle is already in spawn zone
            int lastElement = activeVehicles.get(1).getEntities().size()-1;
            if (lastElement >= 0 && activeVehicles.get(1).get(lastElement).getPosition().getX() > viewportRight) {
                // System.out.println("VehicleSpawner: East spawn full - aborting.");
                vehicleType = -1;   // uses default case (doesn't create vehicle)
            }
            
            // System.out.println("east spawn succeeded. Vehicle type: "+vehicleType);
            switch (vehicleType) {
                case 0: // BUS
                     Bus bus = new Bus(EAST_SP, BUS_W, BUS_H, WEST);
                     bus.setPosition(EAST_SP);
                     bus.setDirection((float) WEST);
                    activeVehicles.get(1).add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(EAST_SP, CAR_W, CAR_H, WEST);
                    car.setPosition(EAST_SP);
                    car.setDirection((float) WEST);
                    activeVehicles.get(1).add(car);
                    break;

                case 2: // CYCLIST
                    Cyclist cyclist = new Cyclist(EAST_SP, CYCLIST_W, CYCLIST_H, WEST);  // arbitrary arguments
                    cyclist.setPosition(EAST_SP);
                    cyclist.setDirection((float) WEST);
                    activeVehicles.get(1).add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(EAST_SP, MOTORBIKE_W, MOTORBIKE_H, WEST);
                    motorbike.setPosition(EAST_SP);
                    motorbike.setDirection((float) WEST);
                    activeVehicles.get(1).add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(EAST_SP, TRUCK_W, TRUCK_H, WEST);
                    truck.setPosition(EAST_SP);
                    truck.setDirection((float) WEST);
                    activeVehicles.get(1).add(truck);
                    break;
                default:
            }
        }
        
        // south spawn
        if (rand.nextInt(100) < spawnChance) {
            int vehicleType = rand.nextInt(VCL_TYPE_AMOUNT);

            // prevents spawning when a vehicle is already in spawn zone
            int lastElement = activeVehicles.get(2).getEntities().size()-1;
            if (lastElement >= 0 && activeVehicles.get(2).get(lastElement).getPosition().getY() > viewportBottom) {
                // System.out.println("VehicleSpawner: South spawn full - aborting.");
                vehicleType = -1;   // uses default case (doesn't create vehicle)
            }
            
            // System.out.println("south spawn succeeded. Vehicle type: "+vehicleType);
            switch (vehicleType) {
                case 0: // BUS
                    Bus bus = new Bus(SOUTH_SP, BUS_W, BUS_H, NORTH);
                    bus.setPosition(SOUTH_SP);
                    bus.setDirection((float) NORTH);
                    activeVehicles.get(2).add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(SOUTH_SP, CAR_W, CAR_H, NORTH);
                    car.setPosition(SOUTH_SP);
                    car.setDirection((float) NORTH);
                    activeVehicles.get(2).add(car);
                    break;

                case 2: // CYCLIST
                    Cyclist cyclist = new Cyclist(SOUTH_SP, CYCLIST_W, CYCLIST_H, NORTH);
                    cyclist.setPosition(SOUTH_SP);
                    cyclist.setDirection((float) NORTH);
                    activeVehicles.get(2).add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(SOUTH_SP, MOTORBIKE_W, MOTORBIKE_H, NORTH);
                    motorbike.setPosition(SOUTH_SP);
                    motorbike.setDirection((float) NORTH);
                    activeVehicles.get(2).add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(SOUTH_SP, TRUCK_W, TRUCK_H, NORTH);
                    truck.setPosition(SOUTH_SP);
                    truck.setDirection((float) NORTH);
                    activeVehicles.get(2).add(truck);
                    break;
                default:
            }
        }        

        // west spawn
        if (rand.nextInt(100) < spawnChance) {
            int vehicleType = rand.nextInt(VCL_TYPE_AMOUNT);

            // prevents spawning when a vehicle is already in spawn zone
            int lastElement = activeVehicles.get(3).getEntities().size()-1;
            if (lastElement >= 0 && activeVehicles.get(3).get(lastElement).getPosition().getX() < viewportLeft) {
                // System.out.println("VehicleSpawner: West spawn full - aborting.");
                vehicleType = -1;   // uses default case (doesn't create vehicle)
            }

            // System.out.println("west spawn succeeded. Vehicle type: "+vehicleType);
            switch (vehicleType) {
                case 0: // BUS
                    Bus bus = new Bus(WEST_SP, BUS_W, BUS_H, EAST);
                    bus.setPosition(WEST_SP);
                    bus.setDirection((float) EAST);
                    activeVehicles.get(3).add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(WEST_SP, CAR_W, CAR_H, EAST);
                    car.setPosition(WEST_SP);
                    car.setDirection((float) EAST);
                    activeVehicles.get(3).add(car);
                    break;

                case 2: // CYCLIST
                    Cyclist cyclist = new Cyclist(WEST_SP, CYCLIST_W, CYCLIST_H, EAST);
                    cyclist.setPosition(WEST_SP);
                    cyclist.setDirection((float) EAST);
                    activeVehicles.get(3).add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(WEST_SP,  MOTORBIKE_W, MOTORBIKE_H, EAST);
                    motorbike.setPosition(WEST_SP);
                    motorbike.setDirection((float) EAST);
                    activeVehicles.get(3).add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(WEST_SP, TRUCK_W, TRUCK_H, EAST);
                    truck.setPosition(WEST_SP);
                    truck.setDirection((float) EAST);
                    activeVehicles.get(3).add(truck);
                    break;
                default:
            }
            switch (vehicleType) {
                case 0: // BUS
                    Bus bus = new Bus(WESTTOO_SP, BUS_W, BUS_H, EAST);
                    bus.setPosition(WESTTOO_SP);
                    bus.setDirection((float) EAST);
                    activeVehicles.get(5).add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(WESTTOO_SP, CAR_W, CAR_H, EAST);
                    car.setPosition(WESTTOO_SP);
                    car.setDirection((float) EAST);
                    activeVehicles.get(5).add(car);
                    break;

                case 2: // CYCLIST
                    Cyclist cyclist = new Cyclist(WESTTOO_SP, CYCLIST_W, CYCLIST_H, EAST);
                    cyclist.setPosition(WESTTOO_SP);
                    cyclist.setDirection((float) EAST);
                    activeVehicles.get(5).add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(WESTTOO_SP,  MOTORBIKE_W, MOTORBIKE_H, EAST);
                    motorbike.setPosition(WESTTOO_SP);
                    motorbike.setDirection((float) EAST);
                    activeVehicles.get(5).add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(WESTTOO_SP, TRUCK_W, TRUCK_H, EAST);
                    truck.setPosition(WESTTOO_SP);
                    truck.setDirection((float) EAST);
                    activeVehicles.get(5).add(truck);
                    break;
                default:
            }

            switch (vehicleType) {
                case 0: // BUS
                    Bus bus = new Bus(EASTTOO_SP, BUS_W, BUS_H, WEST);
                    bus.setPosition(EASTTOO_SP);
                    bus.setDirection((float) WEST);
                    activeVehicles.get(5).add(bus);
                    break;

                case 1: // CAR
                    Car car = new Car(EASTTOO_SP, CAR_W, CAR_H, WEST);
                    car.setPosition(EASTTOO_SP);
                    car.setDirection((float) WEST);
                    activeVehicles.get(5).add(car);
                    break;

                case 2: // CYCLIST
                    Cyclist cyclist = new Cyclist(EASTTOO_SP, CYCLIST_W, CYCLIST_H, WEST);
                    cyclist.setPosition(EASTTOO_SP);
                    cyclist.setDirection((float) WEST);
                    activeVehicles.get(5).add(cyclist);
                    break;

                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(EASTTOO_SP,  MOTORBIKE_W, MOTORBIKE_H, WEST);
                    motorbike.setPosition(EASTTOO_SP);
                    motorbike.setDirection((float) WEST);
                    activeVehicles.get(5).add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(EASTTOO_SP, TRUCK_W, TRUCK_H, WEST);
                    truck.setPosition(EASTTOO_SP);
                    truck.setDirection((float) WEST);
                    activeVehicles.get(5).add(truck);
                    break;
                default:
            }
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
