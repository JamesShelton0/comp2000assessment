public class Motorbike extends Vehicle {

    Motorbike() {
        super.accelerationRate = 8;
        super.decelerationRate = -5;
    }

    // hi good idea. added accelerate(float) to Vehicle and set up constructor to initialise super attributes :)

    // @Override    // overrides vehicle class allowing individual deceleration rate if we arent putting this in vehicle
    // void decelerate() {
    //     velocity -= decelerationRate;

    //     if (velocity < 0) { // prevents negative
    //         velocity =0;
    //     }
    // }
}
