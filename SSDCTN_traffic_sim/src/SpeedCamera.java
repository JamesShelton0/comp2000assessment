public class SpeedCamera {
   private double x, y;
    private int speedLimit = 80;

SpeedCamera(double x, double y){
    this.x = x;
    this.y =y;
}
boolean checkSpeed(Vehicle vehicle){
    if(vehicle.x>= x-10 && vehicle.x <= x+10){ //if the vehicle is btw -10,+10 unitss from the camera

    if(vehicle.velocity > speedLimit){
        //add action whne speeding is detected
        return true;
    }
}
    return false;
}
}
