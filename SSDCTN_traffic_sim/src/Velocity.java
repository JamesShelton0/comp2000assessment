//import java.math.*;

public class Velocity {
    static public Point calPosition(double x, double y, double direction, double velocity) {
        direction = direction * Math.PI/180;
        double xPosAddition = Math.cos(direction) * velocity;
        double yPosAddition = Math.sin(direction) * velocity;
        x += xPosAddition;
        y += yPosAddition;
        return new Point(x, y);
    }

    static public Point calPositionWithRadius(Point p, double direction, double radius, int xMultiplier, int yMultiplier, double xAddon, double yAddon){
        double newX = 0.0;
        double newY = 0.0;
        if(direction % 180 != 0 && direction % 90.0 == 0){
            newX = p.getX() + yAddon * xMultiplier;
            newY = p.getY() + xAddon * yMultiplier;
        }
        else if(direction % 180 == 0){
            newX = p.getX() + xAddon * xMultiplier;
            newY = p.getY() + yAddon * yMultiplier;
        }
        return new Point(newX, newY);
    }

    static public Point calPositionWithRadius(Point p, double direction, double radius){
        double dir = direction * Math.PI/180;
        double newX = 0.0;
        double newY = 0.0;
        newX = p.getX() + Math.cos(dir) * radius;
        newY = p.getY() + Math.sin(dir) * radius;
        return new Point(newX, newY);
    }
}
