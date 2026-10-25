import java.awt.*;
import java.awt.geom.*;

public class Road extends Path { 
    // only accepts vehicles in its bounds
    int speedLimit;
    // int laneCount;
    // int laneMarkings;
    int mode;   // 0 = intersection, 1 = vertical road, 2 = horizontal road
    // control two longitudinal ends independently so window stop lines are hidden
    private boolean drawStartStopLine = true;
    private boolean drawEndStopLine = true;
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

    // allow outer road arms to follow edges of window 
    public void setBounds(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // select which road ends need stop lines; start = top / left, end = bottom / right 
    public void setStopLines(boolean drawStart, boolean drawEnd) {
        drawStartStopLine = drawStart;
        drawEndStopLine = drawEnd;
    }


    public void draw(Graphics2D g2d) {
        Rectangle2D.Double asphalt = new Rectangle2D.Double(x, y, width, height);
        g2d.setColor(asphaltColor);
        g2d.fill(asphalt);

        // implement state design pattern instead of this
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
        // base stop-line thickness on road width 
        double stopLineThickness = width * 0.05;
        Rectangle2D.Double stopLine1 = new Rectangle2D.Double(x, y, width*0.47, stopLineThickness);
        Rectangle2D.Double stopLine2 = new Rectangle2D.Double(x+width-(width*0.47), y+height-stopLineThickness, width*0.47, stopLineThickness);
        Rectangle2D.Double middleLine1 = new Rectangle2D.Double(x+(width*0.47), y, width*0.028, height);
        Rectangle2D.Double middleLine2 = new Rectangle2D.Double(x+(width*0.53), y, width*0.028, height);

        g2d.setColor(lineColor);
        // draw only intersection facing stop lines
        if (drawStartStopLine) g2d.fill(stopLine1);
        if (drawEndStopLine) g2d.fill(stopLine2);
        g2d.fill(middleLine1);
        g2d.fill(middleLine2);
    }

    private void drawHorizontal(Graphics2D g2d) {
        // base stop-line thickness on road height 
        double stopLineThickness = height * 0.05;
        Rectangle2D.Double stopLine1 = new Rectangle2D.Double(x+width-stopLineThickness, y, stopLineThickness, height*0.47);
        Rectangle2D.Double stopLine2 = new Rectangle2D.Double(x, y+height-(height*0.47), stopLineThickness, height*0.47);
        Rectangle2D.Double middleLine1 = new Rectangle2D.Double(x, y+(height*0.47), width, height*0.028);
        Rectangle2D.Double middleLine2 = new Rectangle2D.Double(x, y+(height*0.53), width, height*0.028);

        g2d.setColor(lineColor);
        // draw only intersection facing stop lines
        if (drawEndStopLine) g2d.fill(stopLine1);
        if (drawStartStopLine) g2d.fill(stopLine2);
        g2d.fill(middleLine1);
        g2d.fill(middleLine2);
    }
}
