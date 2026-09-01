import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class Explosion {
    private ImageIcon explosion;
    private ImageObserver observer;
    private int x, y;

    public Explosion(int x, int y, ImageObserver observer) {
        this.x = x;
        this.y = y;
        this.observer = observer;

        explosion = new ImageIcon(
            getClass().getResource("/resources/explody.gif")
        );
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(explosion.getImage(), x, y, 50, 50, observer); // draw in top left corner, then size 50x50, this = current object 
    }
}
