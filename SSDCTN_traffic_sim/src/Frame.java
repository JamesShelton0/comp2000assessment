import javax.swing.*;
import java.awt.*;
import javax.swing.JComponent;

public class Frame{

    public static void main(String[] args){
        JFrame frame = new Frame("World Frame");
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        frame.setVisible(true);
    }
}


