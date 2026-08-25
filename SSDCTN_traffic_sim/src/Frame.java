import javax.swing.*;
import java.awt.*;


public class Frame{ 
    public static void main(String[] args){
        JFrame frame = new JFrame("World Frame"); 
        frame.setSize(800,800); 

        JPanel bground = new JPanel(); 
        bground.setBackground(Color.green); 

        bground.setLayout(new BorderLayout());

        Pothole pothole = new Pothole(800, 800);

        bground.add(pothole,BorderLayout.CENTER);

        frame.add(bground);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);    
    }



}


