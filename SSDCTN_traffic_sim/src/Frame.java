import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
    Road roadN, roadE, roadS, roadW, intersection;
    Car car;
    ArrayList<Vehicle> vehicleArr = new ArrayList<>();

    Panel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(63, 155, 11));

        // ---------------------- Timers ----------------------
        Timer lightTimer = new Timer(2800, e -> { trafficLight.changeLight(); });   // light changes every 4s
        lightTimer.start();

        // Vehicle movement & repaint timer (~60fps)
        Timer moveTimer = new Timer(16, e -> {
            truck.move();
            motorbike.move();
            car.move();
            this.repaint();
            if(trafficLight.getLightState() == 2){
                car.setVelocity(0);
                truck.setVelocity(0);
            } else {
                car.setVelocity(1);
                truck.setVelocity(1

                );
            }
        });
        moveTimer.start();


        // ---------------------- Vehicles ----------------------
        // car
        car = new Car(30, 50);
        car.setPosition(340, height);
        car.setDirection((float)(3 * Math.PI / 2));
        

        // truck
        truck = new Truck(width/100*10, height/100*8);
        truck.setPosition(width, 435); // starts off the screen on the right
        truck.setVelocity(1);
        truck.setDirection((float)Math.PI); // right to left

        // motorbike
        motorbike = new Motorbike((width/100) * 10, (height/100) * 6.6667); // 10% of frame width, 6.6667% of frame height
        motorbike.setPosition(375, 540);
        motorbike.setVelocity(3);
        motorbike.setDirection((float)(3 * Math.PI / 2));


        /* test code */
        vehicleArr.add(car);
        vehicleArr.add(truck);
        vehicleArr.add(motorbike);
        /* end test code */


        // ---------------------- Static objects ----------------------
        roadN = new Road(width*0.5, height*0.18, width*0.25, height*0.4, 1);
        roadE = new Road(width*0.82, height*0.5, width*0.4, height*0.25, 2);
        roadS = new Road(width*0.5, height*0.82, width*0.25, height*0.4, 1);
        roadW = new Road(width*0.18, height*0.5, width*0.4, height*0.25, 2);
        intersection = new Road(width*0.5, height*0.5, width*0.25, height*0.25, 0);

        trafficLight = new TrafficLight(350, 200);
        pothole = new Pothole(5, 5);
        explosion = new Explosion(200, 550, this);  // pass panel for callbacks/repaint
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);            // paints JPanel stuff like the background

        Graphics2D g2d = (Graphics2D) g;    // for our 2D graphics components

        // draw components
        roadN.draw(g2d);
        roadE.draw(g2d);
        roadS.draw(g2d);
        roadW.draw(g2d);
        intersection.draw(g2d);
        truck.draw(g2d);
        pothole.draw(g2d);
        explosion.draw(g2d);    // test explosion !!!!! remove this to not show explosion :(
        motorbike.draw(g2d);
        trafficLight.draw(g2d);
        car.draw(g2d);
    }
}

