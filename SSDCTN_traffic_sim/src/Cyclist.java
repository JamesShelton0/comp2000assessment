import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import java.awt.Graphics2D;

public class Cyclist extends Vehicle {
 
    Cyclist(double width, double height) {
        super.width = width;
        super.height = height;
        // super.accelerationRate = 
        // super.decelerationRate = 
    }

    @Override 
    public void draw(Graphics2D g2d) {
        Graphics2D g = (Graphics2D) g2d.create();

        g.rotate(
            Math.toRadians(direction + 90),
            x,
            y
        );

        double bikeW = width * 0.15;
        RoundRectangle2D.Double bike = new RoundRectangle2D.Double(
            x - bikeW / 2, y - height / 2, bikeW, height, bikeW, bikeW
        );
        g.setColor(Color.BLACK);
        g.fill(bike);

        double bodyW = width * 0.6;
        double bodyH = height * 0.35;
        Ellipse2D.Double body = new Ellipse2D.Double(
            x - bodyW / 2, y - bodyH / 2, bodyW, bodyH
        );
        g.setColor(Color.BLUE);
        g.fill(body);

        
        double helmetSize = width * 0.3;
        Ellipse2D.Double helmet = new Ellipse2D.Double(
            x - helmetSize / 2, y - helmetSize * 0.7, helmetSize, helmetSize
        );
        g.setColor(Color.WHITE);
        g.fill(helmet);

        g.dispose();
    }

}