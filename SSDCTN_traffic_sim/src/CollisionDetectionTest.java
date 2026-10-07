// a bunch of tests, mostly for debugging and running controlled scenarios 

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class CollisionDetectionTest {
    public static void main(String[] args) {
        // run focused regression checks for the traffic deadlocks
        testOnlyRearVehicleStops();
        testSeparatedVehiclesContinue();
        testPerpendicularVehiclesDoNotBlockEachOther();
        testMotorbikeHitboxBufferIsRespected();
        testNextMovementStepCannotCauseOverlap();
        System.out.println("Collision detection tests passed.");
    }

    private static void testOnlyRearVehicleStops() {
        // two close vehicles in one lane should only block the rear vehicle
        Car rear = createCar(100, 100, 90);
        Car front = createCar(100, 150, 90);
        EntityStore<Vehicle> lane = createLane(rear, front);

        ArrayList<Vehicle> stopped = CollisionDetection.checkVehicleCollisions(List.of(lane));
        require(stopped.size() == 1 && stopped.contains(rear),
            "Only the rear vehicle should stop in same-direction traffic.");
    }

    private static void testSeparatedVehiclesContinue() {
        // vehicles outside the following distance shouldnt block either vehicle
        Car rear = createCar(100, 100, 90);
        Car front = createCar(100, 300, 90);
        EntityStore<Vehicle> lane = createLane(rear, front);

        ArrayList<Vehicle> stopped = CollisionDetection.checkVehicleCollisions(List.of(lane));
        require(stopped.isEmpty(), "Separated vehicles should continue moving.");
    }

    private static void testPerpendicularVehiclesDoNotBlockEachOther() {
        // traffic lights control crossing directions
        Car southbound = createCar(100, 100, 90);
        Car eastbound = createCar(100, 100, 0);
        EntityStore<Vehicle> lane = createLane(southbound, eastbound);

        ArrayList<Vehicle> stopped = CollisionDetection.checkVehicleCollisions(List.of(lane));
        require(stopped.isEmpty(), "Perpendicular vehicles should not deadlock each other.");
    }

    private static void testMotorbikeHitboxBufferIsRespected() {
        // motorbikes use a larger hitbox than base dimensions 
        Car rear = createCar(100, 100, 90);
        Motorbike front = new Motorbike(new Point(100, 160), 20, 30);
        front.setPosition(100, 160);
        front.setDirection(90);
        front.updateHitBox();
        EntityStore<Vehicle> lane = createLane(rear, front);

        ArrayList<Vehicle> stopped = CollisionDetection.checkVehicleCollisions(List.of(lane));
        require(stopped.size() == 1 && stopped.contains(rear),
            "The rear vehicle should stop before reaching the motorbike's enlarged hitbox.");
    }

    private static void testNextMovementStepCannotCauseOverlap() {
        // fast vehicle must stop before hitting something 
        Car rear = createCar(100, 100, 90);
        rear.setVelocity(10);
        Car stoppedFront = createCar(100, 160, 90);
        stoppedFront.setVelocity(0);
        EntityStore<Vehicle> lane = createLane(rear, stoppedFront);

        ArrayList<Vehicle> stopped = CollisionDetection.checkVehicleCollisions(List.of(lane));
        require(stopped.size() == 1 && stopped.contains(rear),
            "A vehicle should stop before its next movement step enters another hitbox.");
    }

    private static Car createCar(double x, double y, float direction) {
        // initialise both component and hitbox pos for each vehicle 
        Car car = new Car(new Point(x, y), 30, 50);
        car.setPosition(x, y);
        car.setDirection(direction);
        car.updateHitBox();
        return car;
    }

    private static EntityStore<Vehicle> createLane(Vehicle first, Vehicle second) {
        // build same typed store structure as live
        EntityStore<Vehicle> lane = new EntityStore<>();
        lane.add(first);
        lane.add(second);
        return lane;
    }

    private static void require(boolean condition, String message) {
        // throw error 
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
