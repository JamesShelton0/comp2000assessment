import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PoliceCar extends Vehicle {

    private static final int FLASH_MS = 250;   // how often the lights swap

  
    
    private static final ScheduledExecutorService LIGHT_TIMER =
        Executors.newSingleThreadScheduledExecutor(task -> {
            Thread thread = new Thread(task, "police-lights");
            thread.setDaemon(true);
            return thread;
        });

    private static volatile boolean leftSideOn = true;

    static {
        LIGHT_TIMER.scheduleAtFixedRate(
            () -> leftSideOn = !leftSideOn,
            0, FLASH_MS, TimeUnit.MILLISECONDS
        );
    }


    private record LightSegment(double x, Color colour, boolean y) {}

    private static final double WIDTH = 0.175;
    private static final Color RED = new Color(230, 20, 20);
    private static final Color BLUE = new Color(20, 60, 255);
    private static final Color OFF = new Color(60, 60, 60);

    private static final List<LightSegment> LIGHT_BAR = List.of(
        new LightSegment(0.15, RED, true),
        new LightSegment(0.325, RED, true),
        new LightSegment(0.5, BLUE, false),
        new LightSegment(0.675, BLUE, false)
    );


    public PoliceCar(Point p, double width, double height, double direction) {
        super.position = p;
        super.width = width;
        super.height = height;
        super.direction = direction;

        super.accelerationRate = 0.8;
        super.decelerationRate = -1;
        super.topSpeed = 8;

        super.pollutionRate = 50;
        super.bodyColour = Color.WHITE;
        super.hitBox = new HitBox(p, super.direction, width, height);
    }

    @Override
    public void draw(Graphics2D g2d) {
        Graphics2D g = (Graphics2D) g2d.create();

        g.rotate(
            Math.toRadians(direction + 90),
            x,
            y
        );

        double left = x - width / 2;
        double top = y - height / 2;
        double w = width;
        double h = height;


        g.setColor(bodyColour);
        g.fill(new RoundRectangle2D.Double(left, top, w, h, 8, 8));


   
        g.setColor(new Color(0, 40, 140));
        double check = w / 6;
        for (int i = 0; i < 6; i += 2) {
            g.fill(new Rectangle2D.Double(left, top + h * 0.3 + i * check, check, check));
            g.fill(new Rectangle2D.Double(left + w - check, top + h * 0.3 + (i + 1) * check, check, check));
        }


        g.setColor(Color.CYAN);
        g.fill(new Rectangle2D.Double(left + w / 5, top + h / 5, w / 5 * 3, h / 6));
        g.fill(new Rectangle2D.Double(left + w / 5, top + h / 5 * 3, w / 5 * 3, h / 6));


        double barTop = top + h * 0.42;
        double barH = h * 0.1;
        boolean leftOn = leftSideOn;

        g.setColor(leftOn ? new Color(255, 0, 0, 70) : new Color(0, 80, 255, 70));
        double glowX = leftOn ? left - w * 0.2 : left + w * 0.4;
        g.fillOval((int) glowX, (int) (barTop - h * 0.15), (int) (w * 0.8), (int) (h * 0.4));

        LIGHT_BAR.stream().forEach(segment -> {
            boolean lit = segment.y() == leftOn;
            g.setColor(lit ? segment.colour() : OFF);
            g.fill(new Rectangle2D.Double(
                left + w * segment.x(), barTop, w * WIDTH, barH));
        });

        g.setColor(Color.BLACK);
        g.fill(new Rectangle2D.Double(left - 2, top + 8, 4, 10));
        g.fill(new Rectangle2D.Double(left + w - 2, top + 8, 4, 10));
        g.fill(new Rectangle2D.Double(left - 2, top + h - 18, 4, 10));
        g.fill(new Rectangle2D.Double(left + w - 2, top + h - 18, 4, 10));

        g.dispose();
    }
}
