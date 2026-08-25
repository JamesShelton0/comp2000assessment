public class Car extends Vehicle {
    boolean hasSunroof;

    Car() {
        super.accelerationRate = 8;
        super.decelerationRate = 10;
        super.pollutionRate = 50;     // arbitrary
    }
}
