import java.awt.Graphics2D;
import java.awt.Color;

public class BusStop {
    private double  x, y;
    
   
    BusStop(double x, double y){
        this.x=x;
        this.y=y;
    }
    
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


     public void draw(Graphics2D g2d){
        //pole
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect((int) x + 15, (int) y + 30, 5, 50);

        //sign
        g2d.setColor(Color.BLUE);
        g2d.fillRect((int) x, (int) y, 35, 30);

        //text
        g2d.setColor(Color.WHITE);
        g2d.drawString("BUS", (int) x + 2, (int) y + 12);
        g2d.drawString("STOP", (int) x + 1, (int) y + 25);


        

        
    }
}