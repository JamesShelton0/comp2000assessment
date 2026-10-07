// a bunch of tests, mostly for debugging and running controlled scenarios 

public class PotholeTest {
    public static void main(String[] args) throws InterruptedException {
        // guaranteed chance to test swallow 
        Pothole pothole = new Pothole(100, 100, 20, 20, 1, 1.0);
        Car victim = createCar(100, 100, 90);
        pothole.update(victim);

        require(pothole.isSwallowing(), "The overlapping vehicle should begin swallowing.");
        require(pothole.shouldBlock(victim), "The victim should remain stopped while swallowing.");

        // car behind must stop for swallow 
        Car follower = createCar(100, 50, 90);
        follower.setVelocity(10);
        require(pothole.shouldBlock(follower), "Following traffic should stop before the pothole.");

        // traffic outisde of affected lane continues normally
        Car adjacent = createCar(200, 50, 90);
        require(!pothole.shouldBlock(adjacent), "Adjacent traffic should not be blocked.");

        // swallow animation 
        Thread.sleep(750);
        require(pothole.getDisplayWidth() > 80,
            "The swallowing pothole should expand beyond its resting size.");

        // one swallow is finished, it shrinks and the vehicle disappears and traffic resume
        Thread.sleep(850);
        require(pothole.update(victim), "The completed swallow should remove its victim.");
        require(!pothole.isSwallowing(), "The swallow state should finish after its duration.");
        require(!pothole.shouldBlock(follower), "Following traffic should resume afterward.");
        require(Math.abs(pothole.getDisplayWidth() - 20) < 0.0001,
            "The pothole should return to its small resting size.");

        System.out.println("Pothole tests passed.");
    }

    private static Car createCar(double x, double y, float direction) {
        // initialise position and hitbox as live
        Car car = new Car(new Point(x, y), 30, 50);
        car.setPosition(x, y);
        car.setDirection(direction);
        car.updateHitBox();
        return car;
    }

    private static void require(boolean condition, String message) {
        // throw error 
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
