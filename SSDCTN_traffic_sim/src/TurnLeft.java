public class TurnLeft implements VehicleState{

    Vehicle vehicle;
    public TurnLeft(Vehicle vehicle){
        this.vehicle = vehicle;
    }

    public void detectStraight(){

        return;
    }

    public void detectTurn(){
        return;
    }

    public void move(Turn turn){
        float radius = turn.getRadius(); 
        float finishAngle = turn.getFinishAngle();
        Point position = vehicle.getPosition();
        double direction = vehicle.getDirection();
        direction -= 1/radius;
        direction = (direction < 0) ? (direction + 360) : direction;
        direction = (direction > finishAngle) ? finishAngle : direction;
        position.setX(position.getX() - radius * Math.cos(1/radius));
        position.setY(position.getY() + radius * Math.sin(1/radius));
        if(direction == finishAngle){
            vehicle.setState(vehicle.getMoveForwadState());
        }
    }
}
