import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VehicleSpawnerTwo {
    private final Start NORTH_SP;
    private final Start EAST_SP;
    private final Start SOUTH_SP;
    private final Start WEST_SP;
    private final Start EASTTOO_SP;
    private final Start WESTTOO_SP;

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

    private int width, height; // Frame dimensions
    private int spawnChance;
    private Random rand;

    // now uses EntityStore to reject unrelated object types at compile time (generics yay!)
    private List<EntityStore<Vehicle>> activeVehicles;
    private EntityStore<Vehicle> northSpawn, eastSpawn, southSpawn, westSpawn, eastTooSpawn, westTooSpawn;
    private Graph localGraph;

    VehicleSpawnerTwo(int w, int h, int spawnChance, Graph g)throws SimulationConfigurationException{
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

        NORTH_SP = g.getVertex(1).getStart();
        EAST_SP = g.getVertex(2).getStart();
        SOUTH_SP = g.getVertex(3).getStart();
        WEST_SP = g.getVertex(4).getStart();
        EASTTOO_SP = g.getVertex(5).getStart();
        WESTTOO_SP = g.getVertex(6).getStart();

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

        localGraph = g;
    }

    public void spawn(){
        if (rand.nextInt(100) < spawnChance) {
            spawnHelper(NORTH_SP, SOUTH, 0);
        }if(rand.nextInt(100) < spawnChance){
            spawnHelper(EAST_SP, WEST, 1);
        }if(rand.nextInt(100) < spawnChance){
            spawnHelper(SOUTH_SP, NORTH, 2);
        }if(rand.nextInt(100) < spawnChance){
            spawnHelper(WEST_SP, EAST, 3);
        }if(rand.nextInt(100) < spawnChance){
            spawnHelper(EASTTOO_SP, WEST, 4);
        }if(rand.nextInt(100) < spawnChance){
            spawnHelper(WESTTOO_SP, EAST, 5);
        }

    }

    private void spawnHelper(Start start, double direction, int entityStoreNum){
        int vehicleType = rand.nextInt(VCL_TYPE_AMOUNT);
            
        // prevents spawning when a vehicle is already in spawn zone
        int lastElement = activeVehicles.get(entityStoreNum).getEntities().size()-1;
        if (lastElement >= 0 && activeVehicles.get(entityStoreNum).get(lastElement).getPosition().getY() < 0) {
            // System.out.println("VehicleSpawner: North spawn full - aborting.");
            vehicleType = -1;   // uses default case (doesn't create vehicle)
        }

        //creates a random finish for the vehicle
        Vertex finish = localGraph.getRandomFinish(localGraph.getVertex(start.getID()));

        // System.out.println("north spawn succeeded. Vehicle type: "+vehicleType);
        switch (vehicleType) {
            case 0: // BUS
                Bus bus = new Bus(start.getStartPoint(), BUS_W, BUS_H, direction);
                bus.createRoute(start.getID(), finish.getID(), localGraph);
                activeVehicles.get(entityStoreNum).add(bus);
                break;
            
            case 1: // CAR
                Car car = new Car(start.getStartPoint(), CAR_W, CAR_H, direction);
                car.createRoute(start.getID(), finish.getID(), localGraph);
                activeVehicles.get(entityStoreNum).add(car);
                break;

            case 2:  //CYCLIST
                Cyclist cyclist = new Cyclist(start.getStartPoint(), CYCLIST_W, CYCLIST_H, direction);  // arbitrary arguments
                cyclist.createRoute(start.getID(), finish.getID(), localGraph);
                activeVehicles.get(entityStoreNum).add(cyclist);
                break;
            
            case 3: // MOTORBIKE  // construct at the north spawn so the initial hitbox matches its position
                Motorbike motorbike = new Motorbike(start.getStartPoint(), MOTORBIKE_W, MOTORBIKE_H, direction);
                motorbike.createRoute(start.getID(), finish.getID(), localGraph);
                activeVehicles.get(entityStoreNum).add(motorbike);
                break;

            case 4: // TRUCK
                Truck truck = new Truck(start.getStartPoint(), TRUCK_W, TRUCK_H, direction);
                truck.createRoute(start.getID(), finish.getID(), localGraph);
                activeVehicles.get(entityStoreNum).add(truck);
                break;
            default:
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
        MOTORBIKE_H = height* 0.0375;
        TRUCK_W     = width * 0.1;
        TRUCK_H     = height* 0.08;
    }
}
