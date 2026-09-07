import java.awt.*;
import java.awt.geom.*;

public class Road extends Path { 
    // only accepts vehicles in its bounds
    int speedLimit;
    // int laneCount;
    // int laneMarkings;
    int mode;   // 0 = intersection, 1 = vertical road, 2 = horizontal road
    Color lineColor = new Color(200, 200, 200);
    Color asphaltColor = new Color(80, 80, 80);

    Road() {}

    Road(double xMid, double yMid, double width, double height, int mode) {
        super.x = xMid - (width/2);
        super.y = yMid  - (height/2);
        super.width = width;
        super.height = height;
        this.mode = mode;
    }


    public void draw(Graphics2D g2d) {
        Rectangle2D.Double asphalt = new Rectangle2D.Double(x, y, width, height);
        g2d.setColor(asphaltColor);
        g2d.fill(asphalt);

        switch (mode) {
            case 0:
                drawIntersection(g2d);
                break;

            case 1:
                drawVertical(g2d);
                break;
        
            case 2:
                drawHorizontal(g2d);
                break;

            default:
                break;
        }
    }

    
    private void drawIntersection(Graphics2D g2d) {
        // if there's anything we want in the actual intersection
    }

    private void drawVertical(Graphics2D g2d) {
        Rectangle2D.Double stopLine1 = new Rectangle2D.Double(x, y, width*0.47, height*0.03);
        Rectangle2D.Double stopLine2 = new Rectangle2D.Double(x+width-(width*0.47), y+height-(height*0.03), width*0.47, height*0.03);
        Rectangle2D.Double middleLine1 = new Rectangle2D.Double(x+(width*0.47), y, width*0.028, height);
        Rectangle2D.Double middleLine2 = new Rectangle2D.Double(x+(width*0.53), y, width*0.028, height);

        g2d.setColor(lineColor);
        g2d.fill(stopLine1);
        g2d.fill(stopLine2);
        g2d.fill(middleLine1);
        g2d.fill(middleLine2);
    }

    private void drawHorizontal(Graphics2D g2d) {
        Rectangle2D.Double stopLine1 = new Rectangle2D.Double(x+width-(width*0.03), y, width*0.03, height*0.47);
        Rectangle2D.Double stopLine2 = new Rectangle2D.Double(x, y+height-(height*0.47), width*0.03, height*0.47);
        Rectangle2D.Double middleLine1 = new Rectangle2D.Double(x, y+(height*0.47), width, height*0.028);
        Rectangle2D.Double middleLine2 = new Rectangle2D.Double(x, y+(height*0.53), width, height*0.028);

        g2d.setColor(lineColor);
        g2d.fill(stopLine1);
        g2d.fill(stopLine2);
        g2d.fill(middleLine1);
        g2d.fill(middleLine2);
    }
}
