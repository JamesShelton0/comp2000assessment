import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class Bus extends Vehicle {

    private boolean stoppedAtBusStop = false;  //if the bus is stopped atm
    private boolean leavingBusStop = false;
    
    private int waitTime = 0; //how long has it waited
    private int maxWaitTime = 100; //how long it will wait

    Point p;

    Bus(Point p, double width, double height, double direction) {
        this.p = p;
        super.width = width;
        super.height = height;
        super.hitBox = new HitBox(p, direction, width, height);
        velocity = 60;
        topSpeed = 60;
        decelerationRate = -5;
        accelerationRate = 5;   
    }

    @Override
    public void draw(Graphics2D g2d) {

        Graphics2D g = (Graphics2D) g2d.create();

        // Rotate bus depending on travel direction
        // Bus is naturally drawn facing NORTH
        g.rotate(
            Math.toRadians(direction + 90),
            x,
            y
        );

        double left = x - width / 2;
        double top = y - height / 2;

        // Bus body
        RoundRectangle2D.Double busBody = new RoundRectangle2D.Double(
            left,
            top,
            width,
            height,
            10,
            10
        );
        g.setColor(Color.YELLOW);
        g.fill(busBody);

        // Front windscreen
        Rectangle2D.Double frontWindow = new Rectangle2D.Double(
            left + 5,
            top + 5,
            width - 10,
            height / 8
        );
        g.setColor(Color.CYAN);
        g.fill(frontWindow);

        // Front wheels
        g.setColor(Color.BLACK);
        g.fillRect(
            (int) left - 3,
            (int) (top + height / 5),
            6,
            12
        );
        g.fillRect(
            (int) (left + width - 3),
            (int) (top + height / 5),
            6,
            12
        );

        // Rear wheels
        g.fillRect(
            (int) left - 3,
            (int) (top + height - height / 4),
            6,
            12
        );
        g.fillRect(
            (int) (left + width - 3),
            (int) (top + height - height / 4),
            6,
            12
        );

        g.dispose();
    }


    //check if the bus is close to a bus stop
    void checkBusStop(BusStop stop) { 
        // getX case-sensitive, rvm semicolon and add {} because otherwise accelerate will run all the time 
        if(!stoppedAtBusStop && !leavingBusStop && x >= stop.getX() - 10 &&x <=stop.getX()) {
            accelerate(decelerationRate);
        }

        if(velocity==0) {
            stoppedAtBusStop = true;
        }
    }


    void waitAtBusStop() {
        if(stoppedAtBusStop){
            waitTime++;
            if(waitTime >= maxWaitTime){
                stoppedAtBusStop = false;
                leavingBusStop = true;
                waitTime = 0;
            }
        }
    }

    
    void leaveBusStop() {
        if(leavingBusStop){
            accelerate(accelerationRate);
            
            if(velocity >= topSpeed){
                velocity = topSpeed;
                leavingBusStop = false;
            }
        }
    }


}
      
    
