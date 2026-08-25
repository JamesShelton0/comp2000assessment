import javax.swing.*;
import java.awt.*;


public class Frame{ 
    public static void main(String[] args){
        JFrame frame = new JFrame("World Frame"); 
        frame.setSize(800,800); 

        JPanel bground = new JPanel();
        bground.setBackground(Color.GREEN);
        bground.setLayout(null);

        Pothole pothole = new Pothole(800, 800);
        pothole.setBounds(0, 0, 800, 800);
        pothole.setOpaque(false);

        Truck truck = new Truck(800, 50);
        truck.setBounds(100, 100, 800, 800);
        truck.setOpaque(false);

        bground.add(pothole);
        bground.add(truck);

        frame.add(bground);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }



}


