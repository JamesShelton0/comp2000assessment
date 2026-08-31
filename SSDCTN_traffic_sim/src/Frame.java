import javax.swing.*;
import java.awt.*;
public class Frame { 
    public static void main(String[] args){
        JFrame frame = new JFrame("World Frame"); 
        frame.setSize(800,800); 

        JPanel bground = new JPanel();
        bground.setBackground(new Color(63, 155, 11));
        bground.setLayout(null);

        // pothole
        Pothole pothole = new Pothole(800, 800);
        pothole.setBounds(0, 0, 800, 800);
        pothole.setOpaque(false);

        // truck
        Truck truck = new Truck(70, 30);
        truck.setPosition(835, 400); // starts off the screen on the right
        truck.setVelocity(1);
        truck.setDirection((float)Math.PI); // right to left

        // motorbike
        Motorbike motorbike = new Motorbike(50, 80);
        motorbike.setPosition(375, 540);
        motorbike.setVelocity(3);
        motorbike.setDirection((float)(3 * Math.PI / 2));        
        motorbike.setOpaque(false);

        // traffic light
        TrafficLight trafficLight = new TrafficLight();
        trafficLight.setBounds(600,100,50,50);
        
        // adding components 
        bground.add(pothole);
        bground.add(truck);
        bground.add(motorbike);
        bground.add(trafficLight);

        // explosion
        Explosion explosion = new Explosion(300, 300);
        bground.add(explosion);

        frame.add(bground);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // change traffic light every X seconds
    //    Timer timer = new Timer(4000, e -> {
         //   trafficLight.changeLight();           I want to borrow this one sec
     //   });

    //    timer.start();
  //  }

        Timer timer = new Timer(16, e-> {

            truck.move();
            motorbike.move();
            bground.repaint();

        });

        timer.start();



        }


        


}
