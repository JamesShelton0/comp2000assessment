//import java.math.*;

public class Velocity {
    static public Point calPosition(double x, double y, double direction, float velocity){
        double xPosAddition = Math.cos(direction) * velocity;
        double yPosAddition = Math.sin(direction) * velocity;
        x += xPosAddition;
        y += yPosAddition;
        return new Point(x, y);
    }

    static public Point calPositionWithRadius(Point p, double direction, double radius, int xMultiplier, int yMultiplier){
        double newX = p.getX() + radius * Math.cos(direction);
        double newY = p.getY() + radius * Math.sin(direction);
        return new Point(newX, newY);
    }
}
