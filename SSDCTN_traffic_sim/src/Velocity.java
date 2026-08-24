//import java.math.*;

public class Velocity {
    static public Point calPosition(double x, double y, float direction, float velocity){
        double xPosAddition = Math.cos(direction) * velocity;
        double yPosAddition = Math.sin(direction) * velocity;
        x += xPosAddition;
        y += yPosAddition;
        return new Point(x, y);
    }

    static public Point calPosition(double x, double y, float direction, float velocity, float acceleration){
        velocity += acceleration;
        double xPosAddition = Math.cos(direction) * velocity;
        double yPosAddition = Math.sin(direction) * velocity;
        x += xPosAddition;
        y += yPosAddition;
        return new Point(x, y);
    }

    static public Point calPosiitonWithRadius(Point p, double x, double y, float direction, double radius){
        double newX;          
        return null;
    }
}
