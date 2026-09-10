import java.util.Random;
import java.util.ArrayList;
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
    private double BUS_W, BUS_H;
    private double CAR_W, CAR_H;
    private double CYCLIST_W, CYCLIST_H;
    private double MOTORBIKE_W, MOTORBIKE_H;
    private double TRUCK_W, TRUCK_H;

    private final int SPAWN_BUFFER;

    private int width, height; // Frame dimensions
    private int spawnChance;
    private Random rand;

     // now uses EntityStore to reject unrelated object types at compile time (generics yay!) 
    private List<EntityStore<Vehicle>> activeVehicles;
    private EntityStore<Vehicle> northSpawn, eastSpawn, southSpawn, westSpawn;

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
        SPAWN_BUFFER = (int) (width*0.15);
        NORTH_SP = new Point(width*0.57, 0-SPAWN_BUFFER);
        EAST_SP = new Point(width+SPAWN_BUFFER, height*0.57);
        SOUTH_SP = new Point(width*0.43, height+SPAWN_BUFFER);
        WEST_SP = new Point(0-SPAWN_BUFFER, height*0.43);
        TEST_SP = new Point(0,0);
        assignDimensions();
        rand = new Random();

        northSpawn = new EntityStore<>();
        eastSpawn = new EntityStore<>();
        southSpawn = new EntityStore<>();
        westSpawn = new EntityStore<>();

        activeVehicles = new ArrayList<>();
        activeVehicles.add(northSpawn);
        activeVehicles.add(eastSpawn);
        activeVehicles.add(southSpawn);
        activeVehicles.add(westSpawn);
    }

    public void spawn() {
        // north spawn
        if (rand.nextInt(100) < spawnChance) {
            int vehicleType = rand.nextInt(VCL_TYPE_AMOUNT);
            
            // prevents spawning when a vehicle is already in spawn zone
            int lastElement = activeVehicles.get(0).getEntities().size()-1;
            if (lastElement > 0 && activeVehicles.get(0).get(lastElement).getY() < 0) {
                System.out.println("VehicleSpawner: North spawn full - aborting.");
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
                    Car car = new Car(NORTH_SP, CAR_W, CAR_H);
                    car.setPosition(NORTH_SP);
                    car.setDirection((float) SOUTH);
                    activeVehicles.get(0).add(car);
                    break;

                case 2: // CYCLIST
                    // Cyclist cyclist = new Cyclist(CYCLIST_W, CYCLIST_H);  // arbitrary arguments
                    // cyclist.setPosition(NORTH_SP);
                    // cyclist.setDirection((float) SOUTH);
                    // activeVehicles.get(0).add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(EAST_SP, MOTORBIKE_W, MOTORBIKE_H);
                    motorbike.setPosition(NORTH_SP);
                    motorbike.setDirection((float) SOUTH);
                    activeVehicles.get(0).add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(NORTH_SP, TRUCK_W, TRUCK_H);
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
            if (lastElement > 0 && activeVehicles.get(1).get(lastElement).getX() > width) {
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
                    Car car = new Car(EAST_SP, CAR_W, CAR_H);
                    car.setPosition(EAST_SP);
                    car.setDirection((float) WEST);
                    activeVehicles.get(1).add(car);
                    break;

                case 2: // CYCLIST
                    // Cyclist cyclist = new Cyclist(CYCLIST_W, CYCLIST_H);  // arbitrary arguments
                    // cyclist.setPosition(EAST_SP);
                    // cyclist.setDirection((float) WEST);
                    // activeVehicles.get(1).add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(EAST_SP, MOTORBIKE_W, MOTORBIKE_H);
                    motorbike.setPosition(EAST_SP);
                    motorbike.setDirection((float) WEST);
                    activeVehicles.get(1).add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(EAST_SP, TRUCK_W, TRUCK_H);
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
            if (lastElement > 0 && activeVehicles.get(2).get(lastElement).getY() > height) {
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
                    Car car = new Car(SOUTH_SP, CAR_W, CAR_H);
                    car.setPosition(SOUTH_SP);
                    car.setDirection((float) NORTH);
                    activeVehicles.get(2).add(car);
                    break;

                case 2: // CYCLIST
                    // Cyclist cyclist = new Cyclist(CYCLIST_W, CYCLIST_H);  // arbitrary arguments
                    // cyclist.setPosition(SOUTH_SP);
                    // cyclist.setDirection((float) NORTH);
                    // activeVehicles.get(2).add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(SOUTH_SP, MOTORBIKE_W, MOTORBIKE_H);
                    motorbike.setPosition(SOUTH_SP);
                    motorbike.setDirection((float) NORTH);
                    activeVehicles.get(2).add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(SOUTH_SP, TRUCK_W, TRUCK_H);
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
            if (lastElement > 0 && activeVehicles.get(3).get(lastElement).getX() < 0) {
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
                    Car car = new Car(WEST_SP, CAR_W, CAR_H);
                    car.setPosition(WEST_SP);
                    car.setDirection((float) EAST);
                    activeVehicles.get(3).add(car);
                    break;

                case 2: // CYCLIST
                    // Cyclist cyclist = new Cyclist(CYCLIST_W, CYCLIST_H);  // arbitrary arguments
                    // cyclist.setPosition(WEST_SP);
                    // cyclist.setDirection((float) EAST);
                    // activeVehicles.get(3).add(cyclist);
                    break;
                
                case 3: // MOTORBIKE
                    Motorbike motorbike = new Motorbike(WEST_SP,  MOTORBIKE_W, MOTORBIKE_H);
                    motorbike.setPosition(WEST_SP);
                    motorbike.setDirection((float) EAST);
                    activeVehicles.get(3).add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(WEST_SP, TRUCK_W, TRUCK_H);
                    truck.setPosition(WEST_SP);
                    truck.setDirection((float) EAST);
                    activeVehicles.get(3).add(truck);
                    break;
                default:
            }
        }
    }

    public List<EntityStore<Vehicle>> getVehicles() {
        // return store's read-only typed view for Panel's movement and drawing loops
        return activeVehicles;
    }

    // run periodically to make vehicles outside frame dimensions eligible for garbage collection
    public void despawn() {
        // System.out.println("VehicleSpawner.despawn() called");
        Iterator<Vehicle> iteratorN = activeVehicles.get(0).modifyEntities().iterator();
        while (iteratorN.hasNext()) {
            Vehicle v = iteratorN.next();
            if (v.getPosition().getX() < 0-SPAWN_BUFFER
            || v.getPosition().getY() < 0-SPAWN_BUFFER
            || v.getPosition().getX() > width+SPAWN_BUFFER
            || v.getPosition().getY() > height+SPAWN_BUFFER) {
                // System.out.println("Vehicle despawned at " + v.getPosition());
                iteratorN.remove();
            }
        }

        Iterator<Vehicle> iteratorE = activeVehicles.get(1).modifyEntities().iterator();
        while (iteratorE.hasNext()) {
            Vehicle v = iteratorE.next();
            if (v.getPosition().getX() < 0-SPAWN_BUFFER
            || v.getPosition().getY() < 0-SPAWN_BUFFER
            || v.getPosition().getX() > width+SPAWN_BUFFER
            || v.getPosition().getY() > height+SPAWN_BUFFER) {
                // System.out.println("Vehicle despawned at " + v.getPosition());
                iteratorE.remove();
            }
        }

        Iterator<Vehicle> iteratorS = activeVehicles.get(2).modifyEntities().iterator();
        while (iteratorS.hasNext()) {
            Vehicle v = iteratorS.next();
            if (v.getPosition().getX() < 0-SPAWN_BUFFER
            || v.getPosition().getY() < 0-SPAWN_BUFFER
            || v.getPosition().getX() > width+SPAWN_BUFFER
            || v.getPosition().getY() > height+SPAWN_BUFFER) {
                // System.out.println("Vehicle despawned at " + v.getPosition());
                iteratorS.remove();
            }
        }

        Iterator<Vehicle> iteratorW = activeVehicles.get(3).modifyEntities().iterator();
        while (iteratorW.hasNext()) {
            Vehicle v = iteratorW.next();
            if (v.getPosition().getX() < 0-SPAWN_BUFFER
            || v.getPosition().getY() < 0-SPAWN_BUFFER
            || v.getPosition().getX() > width+SPAWN_BUFFER
            || v.getPosition().getY() > height+SPAWN_BUFFER) {
                // System.out.println("Vehicle despawned at " + v.getPosition());
                iteratorW.remove();
            }
        }
    }

    private void assignDimensions() {
        BUS_W = width*0.05;
        BUS_H = height*0.12;
        CAR_W = width*0.0375;
        CAR_H = height*0.0625;
        CYCLIST_W = width*0.05;
        CYCLIST_H = height*0.05;
        MOTORBIKE_W = (width*0.025);
        MOTORBIKE_H = (height*0.0375);
        TRUCK_W = width*0.1;
        TRUCK_H = height*0.08;
    }
}
