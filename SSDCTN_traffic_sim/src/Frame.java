import javax.swing.*;
import java.awt.*;


public class Frame{ 
    public static void main(String[] args){
        JFrame frame = new JFrame("World Frame"); 
        frame.setSize(800,800); 

        JPanel p = new JPanel(); 
        p.setBackground(Color.green); 

        p.setLayout(new BorderLayout());

        Pothole pothole = new Pothole(800, 800);

        p.add(pothole,BorderLayout.CENTER);
       
        
    }



}


