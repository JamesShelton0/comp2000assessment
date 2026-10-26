import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class Explosion {
    private static final int SIZE = 80;
    private static final int BLOCK_RADIUS = 100;
    private static final long DURATION = 1500;

    private final ImageIcon explosion;
    private final ImageObserver observer;
    private final int x, y;
    private final long startTime;

    public Explosion(int x, int y, ImageObserver observer) {
        this.x = x;
        this.y = y;
        this.observer = observer;
        this.startTime = System.currentTimeMillis();

        explosion = new ImageIcon(
            getClass().getResource("/resources/explody.gif")
        );
    }

    public void draw(Graphics2D g2d) {
        // x and y are the collision centre, so offset the image to centre it
        g2d.drawImage(explosion.getImage(), x - SIZE / 2, y - SIZE / 2, SIZE, SIZE, observer);
    }

    public boolean isFinished() {
        // panel removes explosion once display time is over 
        return System.currentTimeMillis() - startTime >= DURATION;
    }

    public boolean blocks(Point point) {
        // compare squared distances to avoid unnecessary math
        double distanceX = point.getX() - x;
        double distanceY = point.getY() - y;
        return distanceX * distanceX + distanceY * distanceY <= BLOCK_RADIUS * BLOCK_RADIUS;
    }
}
