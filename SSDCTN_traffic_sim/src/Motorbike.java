public class Motorbike extends Vehicle {
   
    private float accelerationRate = 8;
    private float decelerationRate = 5;

    @Override // overrides vehicle class allowing individual deceleration rate if we arent putting this in vehicle
    void decelerate() {
        velocity -= decelerationRate;

        if (velocity < 0) { // prevents negative
            velocity =0;
        }
    }
}
