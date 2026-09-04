import java.util.Random;
import java.util.ArrayList;
import java.lang.Math;

public class VehicleSpawner {
    private final Point NORTH_SP;
    private final Point EAST_SP;
    private final Point SOUTH_SP;
    private final Point WEST_SP;

    private final double NORTH = 3 * Math.PI / 2;
    private final double EAST = 0;
    private final double SOUTH = Math.PI / 2;
    private final double WEST = Math.PI;

    private final int VCL_TYPE_AMOUNT = 5;

    private int width, height; // Frame dimensions
    private int spawnChance;
    private Random rand;
    private ArrayList<Vehicle> activeVehicles;

    VehicleSpawner(int w, int h, int spawnChance) {
        this.width = w;
        this.height = h;
        this.spawnChance = spawnChance;
        NORTH_SP = new Point(width*0.54, 0);
        EAST_SP = new Point(width, height*0.53);
        SOUTH_SP = new Point(width*0.42, height);
        WEST_SP = new Point(0, height*0.42);
        activeVehicles = new ArrayList<Vehicle>();
        rand = new Random();
    }

    public void spawn() {
        // north spawn
        if (rand.nextInt(100) < spawnChance) {
            int vehicleType = rand.nextInt(VCL_TYPE_AMOUNT);
            // System.out.println("north spawn succeeded. Vehicle type: "+vehicleType);
            switch (vehicleType) {
                case 0: // BUS
                    // Bus bus = new Bus(width*0.1, height*0.1);   // arbitrary arguments
                    // bus.setPosition(NORTH_SP);
                    // bus.setDirection((float) SOUTH);
                    // activeVehicles.add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(30, 50);
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
                    Motorbike motorbike = new Motorbike(width*0.1, height*0.06667);
                    motorbike.setPosition(NORTH_SP);
                    motorbike.setDirection((float) SOUTH);
                    activeVehicles.add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(width*0.1, height*0.08);
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
                    // Bus bus = new Bus(width*0.1, height*0.1);   // arbitrary arguments
                    // bus.setPosition(EAST_SP);
                    // bus.setDirection((float) WEST);
                    // activeVehicles.add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(30, 50);
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
                    Motorbike motorbike = new Motorbike(width*0.1, height*0.06667);
                    motorbike.setPosition(EAST_SP);
                    motorbike.setDirection((float) WEST);
                    activeVehicles.add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(width*0.1, height*0.08);
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
                    // Bus bus = new Bus(width*0.1, height*0.1);   // arbitrary arguments
                    // bus.setPosition(SOUTH_SP);
                    // bus.setDirection((float) NORTH);
                    // activeVehicles.add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(30, 50);
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
                    Motorbike motorbike = new Motorbike(width*0.1, height*0.06667);
                    motorbike.setPosition(SOUTH_SP);
                    motorbike.setDirection((float) NORTH);
                    activeVehicles.add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(width*0.1, height*0.08);
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
                    // Bus bus = new Bus(width*0.1, height*0.1);   // arbitrary arguments
                    // bus.setPosition(WEST_SP);
                    // bus.setDirection((float) EAST);
                    // activeVehicles.add(bus);
                    break;
                
                case 1: // CAR
                    Car car = new Car(30, 50);
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
                    Motorbike motorbike = new Motorbike(width*0.1, height*0.06667);
                    motorbike.setPosition(WEST_SP);
                    motorbike.setDirection((float) EAST);
                    activeVehicles.add(motorbike);
                    break;

                case 4: // TRUCK
                    Truck truck = new Truck(width*0.1, height*0.08);
                    truck.setPosition(WEST_SP);
                    truck.setDirection((float) EAST);
                    activeVehicles.add(truck);
                    break;
                default:
            }
        }
    }

    public ArrayList<Vehicle> getVehicles() {
        return activeVehicles;
    }
}
