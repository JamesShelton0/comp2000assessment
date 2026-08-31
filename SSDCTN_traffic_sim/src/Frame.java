import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame { 
    Panel panel;

    Frame(int width, int height) {
        panel = new Panel(width, height);
        this.setTitle("Traffic Sim");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.setResizable(false);
        // ImageIcon logo = new ImageIcon(logoPath);
        // this.setIconImage(logo.getImage());
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}

class Panel extends JPanel {
    Truck truck;
    Pothole pothole;
    Explosion explosion;
    Motorbike motorbike;
    TrafficLight trafficLight;
    Car car;

    Panel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(63, 155, 11));

        // change traffic light every 4s
        Timer lightTimer = new Timer(4000, e -> { trafficLight.changeLight(); });
        lightTimer.start();

        car = new Car(30, 50);
        car.setPosition(300, 400);
        car.setVelocity(2);
        car.setDirection((float)(3 * Math.PI / 2));

        // truck
        truck = new Truck(width/100*10, height/100*8);
        truck.setPosition(835, 400); // starts off the screen on the right
        truck.setVelocity(1);
        truck.setDirection((float)Math.PI); // right to left

        // pothole
        pothole = new Pothole(5, 5);

       // explosion (pass panel for callbacks/repaint)
        //explosion = new Explosion(300, 300, this);

        // motorbike
        motorbike = new Motorbike(80, 120);
        motorbike.setPosition(375, 540);
        motorbike.setVelocity(3);
        motorbike.setDirection(1);

        trafficLight = new TrafficLight();

        // movement & repaint timer (~60fps)
        Timer moveTimer = new Timer(16, e -> {
            truck.move();
            motorbike.move();
            car.move();
            this.repaint();
        });
        moveTimer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);            // paints JPanel stuff like the background

        Graphics2D g2d = (Graphics2D) g;    // for our 2D graphics components

        // draw components
        truck.draw(g2d);
        pothole.draw(g2d);
        //explosion.draw(g2d);    
        motorbike.draw(g2d);
        trafficLight.draw(g2d);
        car.draw(g2d);
    }
}

