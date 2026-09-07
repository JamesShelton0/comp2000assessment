import java.util.Random;
import java.util.Iterator;
import java.util.List;

public class VehicleSpawner {
    private final Point NORTH_SP;
    private final Point EAST_SP;
    private final Point SOUTH_SP;
    private final Point WEST_SP;
    private final Point TEST_SP;

    private final double NORTH = 270;
    private final double EAST = 0;
    private final double SOUTH = 90;
    private final double WEST = 180;

    private final int VCL_TYPE_AMOUNT = 5;

    private final int SPAWN_BUFFER;

    private int width, height; // Frame dimensions
    private int spawnChance;
    private Random rand;
    private final EntityStore<Vehicle> activeVehicles; // now uses EntityStore to reject unrelated object types at compile time (generics yay!) 

    VehicleSpawner(int w, int h, int spawnChance) throws SimulationConfigurationException {
        // validate at startup so bad settings create error
        if (w <= 0 || h <= 0) {
            throw new SimulationConfigurationException("Simulation dimensions must be positive.");
        }
        if (spawnChance < 0 || spawnChance > 100) {
            throw new SimulationConfigurationException("Spawn chance must be between 0 and 100.");
        }
        this.width = w;
        this.height = h;
        this.spawnChance = spawnChance;
        NORTH_SP = new Point(width*0.57, 0);
        EAST_SP = new Point(width, height*0.57);
        SOUTH_SP = new Point(width*0.43, height);
        WEST_SP = new Point(0, height*0.43);
        TEST_SP = new Point(0,0);
        SPAWN_BUFFER = (int) (width*0.1);
        activeVehicles = new EntityStore<>(); // infers Vehicle from dec above
        rand = new Random();
    }

    public void spawn() {
        // north spawn
        if (rand.nextInt(100) < spawnChance) {
            int vehicleType = rand.nextInt(VCL_TYPE_AMOUNT);
            // System.out.println("north spawn succeeded. Vehicle type: "+vehicleType);
            switch (vehicleType) {
                case 0: // BUS
                    Bus bus = new Bus(NORTH_SP, 50, 100, SOUTH);   // arbitrary arguments
                    bus.setPosition(NORTH_SP);
                    bus.setDirection((float) SOUTH);
                    activeVehicles.add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(NORTH_SP, 30, 50);
                    car.setPosition(NORTH_SP);
                    car.setDirection((float) SOUTH);
                    activeVehicles.add(car);
                    break;

                case 2: // CYCLIST
                    // Cyclist cyclist = new Cyclist(width*0.05, width*0.05);  // arbitrary arguments
                    // cyclist.setPosition(NORTH_SP);
                    // cyclist.setDirection((float) SOUTH);
                    // activeVehicles.add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(EAST_SP, 20, 30);
                    motorbike.setPosition(NORTH_SP);
                    motorbike.setDirection((float) SOUTH);
                    activeVehicles.add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(NORTH_SP, 30, 200);
                    truck.setPosition(NORTH_SP);
                    truck.setDirection((float) SOUTH);
                    activeVehicles.add(truck);
                    break;
                default:
            }
        }

        // east spawn
        if (rand.nextInt(100) < spawnChance) {
            int vehicleType = rand.nextInt(VCL_TYPE_AMOUNT);
            // System.out.println("east spawn succeeded. Vehicle type: "+vehicleType);
            switch (vehicleType) {
                case 0: // BUS
                     Bus bus = new Bus(EAST_SP, 50, 100, WEST);   // arbitrary arguments
                     bus.setPosition(EAST_SP);
                     bus.setDirection((float) WEST);
                    activeVehicles.add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(EAST_SP, 30, 50);
                    car.setPosition(EAST_SP);
                    car.setDirection((float) WEST);
                    activeVehicles.add(car);
                    break;

                case 2: // CYCLIST
                    // Cyclist cyclist = new Cyclist(width*0.05, width*0.05);  // arbitrary arguments
                    // cyclist.setPosition(EAST_SP);
                    // cyclist.setDirection((float) WEST);
                    // activeVehicles.add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(EAST_SP, 20, 30);
                    motorbike.setPosition(EAST_SP);
                    motorbike.setDirection((float) WEST);
                    activeVehicles.add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(EAST_SP, 30, 200);
                    truck.setPosition(EAST_SP);
                    truck.setDirection((float) WEST);
                    activeVehicles.add(truck);
                    break;
                default:
            }
        }
        
        // south spawn
        if (rand.nextInt(100) < spawnChance) {
            int vehicleType = rand.nextInt(VCL_TYPE_AMOUNT);
            // System.out.println("south spawn succeeded. Vehicle type: "+vehicleType);
            switch (vehicleType) {
                case 0: // BUS
                    Bus bus = new Bus(SOUTH_SP, 50, 100, NORTH);   // arbitrary arguments
                    bus.setPosition(SOUTH_SP);
                    bus.setDirection((float) NORTH);
                    activeVehicles.add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(SOUTH_SP, 30, 50);
                    car.setPosition(SOUTH_SP);
                    car.setDirection((float) NORTH);
                    activeVehicles.add(car);
                    break;

                case 2: // CYCLIST
                    // Cyclist cyclist = new Cyclist(width*0.05, width*0.05);  // arbitrary arguments
                    // cyclist.setPosition(SOUTH_SP);
                    // cyclist.setDirection((float) NORTH);
                    // activeVehicles.add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(SOUTH_SP, 20, 30);
                    motorbike.setPosition(SOUTH_SP);
                    motorbike.setDirection((float) NORTH);
                    activeVehicles.add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(SOUTH_SP, 30, 200);
                    truck.setPosition(SOUTH_SP);
                    truck.setDirection((float) NORTH);
                    activeVehicles.add(truck);
                    break;
                default:
            }
        }        

        // west spawn
        if (rand.nextInt(100) < spawnChance) {
            int vehicleType = rand.nextInt(VCL_TYPE_AMOUNT);
            // System.out.println("west spawn succeeded. Vehicle type: "+vehicleType);
            switch (vehicleType) {
                case 0: // BUS
                    Bus bus = new Bus(WEST_SP, 50, 100, EAST);   // arbitrary arguments
                    bus.setPosition(WEST_SP);
                    bus.setDirection((float) EAST);
                    activeVehicles.add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(WEST_SP, 30, 50);
                    car.setPosition(WEST_SP);
                    car.setDirection((float) EAST);
                    activeVehicles.add(car);
                    break;

                case 2: // CYCLIST
                    // Cyclist cyclist = new Cyclist(width*0.05, width*0.05);  // arbitrary arguments
                    // cyclist.setPosition(WEST_SP);
                    // cyclist.setDirection((float) EAST);
                    // activeVehicles.add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(WEST_SP, 20, 30);
                    motorbike.setPosition(WEST_SP);
                    motorbike.setDirection((float) EAST);
                    activeVehicles.add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(WEST_SP, 30, 200);
                    truck.setPosition(WEST_SP);
                    truck.setDirection((float) EAST);
                    activeVehicles.add(truck);
                    break;
                default:
            }
        }
    }

    public List<Vehicle> getVehicles() {
        // return store's read-only typed view for Panel's movement and drawing loops
        return activeVehicles.getEntities();
    }

    // run periodically to make vehicles outside frame dimensions eligible for garbage collection
    public void despawn() {
        // System.out.println("VehicleSpawner.despawn() called");
        Iterator<Vehicle> iterator = activeVehicles.modifyEntities().iterator();
        while (iterator.hasNext()) {
            Vehicle v = iterator.next();
            if (v.getPosition().getX() < 0-SPAWN_BUFFER
            || v.getPosition().getY() < 0-SPAWN_BUFFER
            || v.getPosition().getX() > width+SPAWN_BUFFER
            || v.getPosition().getY() > height+SPAWN_BUFFER) {
                // System.out.println("Vehicle despawned at " + v.getPosition());
                iterator.remove();
            }
        }
    }
}
