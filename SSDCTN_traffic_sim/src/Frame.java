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
        Truck truck = new Truck(800, 50);
        truck.setBounds(100, 100, 800, 800);
        truck.setOpaque(false);

        // motorbike
        Motorbike motorbike = new Motorbike();
        motorbike.setBounds(350,500,50,80);
        motorbike.setOpaque(false);

        // traffic light
        TrafficLight trafficLight = new TrafficLight();
        trafficLight.setBounds(600,100,50,);
        
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
        Timer timer = new Timer(4000, e -> {
            trafficLight.changeLight();
        });

        timer.start();
    }



}