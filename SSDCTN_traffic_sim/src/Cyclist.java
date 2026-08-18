public class Cyclist {
    private float velocity;
    private float length;
    private float width;
    private float x, y;
    private float acceleration;
    private float decceleration;

    public void turn(int radius){

    }

    public void accelerate(){
        velocity += acceleration;
    }

    public void deccelerate(){
        velocity -= decceleration;
    }
}
