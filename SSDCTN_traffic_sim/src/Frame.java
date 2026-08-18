import javax.swing.*;
import java.awt.*;
import javax.swing.JComponent;


public class Frame{ // This creates the frame and colours it green 
    public static void main(String[] args){
        JFrame frame = new JFrame("World Frame"); // creates a new Frame
        frame.setSize(800,800); // control the size of the frame
        frame.setVisible(true); // must be set to true
        JPanel p = new JPanel(); // creates a new panel object
        frame.add(p); // connects the frame to the panel
        p.setBackground(Color.green); // sets background colour - lowercase and american
        frame.show(); // without this the green does not initalise, idk why the show is crossed out 
    }



}


