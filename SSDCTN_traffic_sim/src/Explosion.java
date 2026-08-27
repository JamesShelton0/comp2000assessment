import javax.swing.*;
import java.awt.*;

public class Explosion extends JComponent {
    private ImageIcon explosion;

    public Explosion(int x, int y) {
        explosion = new ImageIcon(
            getClass().getResource("/resources/explody.gif")
        );

        setBounds(x, y, 50, 50); // set pos and size
    }

    @Override 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(explosion.getImage(), 0, 0, 50, 50, this); // draw in top left corner, then size 50x50, this = current object 
    }
}
