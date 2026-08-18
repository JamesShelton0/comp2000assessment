import javax.swing.*;
import java.awt.*;
import javax.swing.JComponent;


public class Frame{
    public static void main(String[] args){
        JFrame frame = new JFrame("World Frame");
        frame.setSize(800,800);
        frame.setVisible(true);
        JPanel p = new JPanel();
        frame.add(p);
        p.setBackground(Color.green);
        frame.show();


    }



}


