import javax.swing.Timer;

public class TrafficLightController {

    private TrafficLight trafficLight1;
    private TrafficLight trafficLight2;
    private TrafficLight trafficLight3;
    private TrafficLight trafficLight4;

    private Timer timer;
    private int trafficPhase = 0;

    private final int GREEN = 0;
    private final int YELLOW = 1;
    private final int RED = 2;

    public TrafficLightController(
        TrafficLight trafficLight1,
        TrafficLight trafficLight2,
        TrafficLight trafficLight3,
        TrafficLight trafficLight4
    ) {

        this.trafficLight1 = trafficLight1;
        this.trafficLight2 = trafficLight2;
        this.trafficLight3 = trafficLight3;
        this.trafficLight4 = trafficLight4;

        // Starting state

        // East / West RED
        trafficLight1.setLightState(RED);
        trafficLight4.setLightState(RED);

        // North / South GREEN
        trafficLight2.setLightState(GREEN);
        trafficLight3.setLightState(GREEN);

        timer = new Timer(5000, e -> changePhase());
    }


    public void start() {
        timer.start();
    }


    private void changePhase() {

        switch (trafficPhase) {

            // North/South GREEN -> YELLOW
            case 0:

                trafficLight2.setLightState(YELLOW);
                trafficLight3.setLightState(YELLOW);

                trafficLight1.setLightState(RED);
                trafficLight4.setLightState(RED);

                trafficPhase = 1;

                timer.setDelay(2000);

                break;


            // North/South RED -> East/West GREEN
            case 1:

                trafficLight2.setLightState(RED);
                trafficLight3.setLightState(RED);

                trafficLight1.setLightState(GREEN);
                trafficLight4.setLightState(GREEN);

                trafficPhase = 2;

                timer.setDelay(5000);

                break;


            // East/West GREEN -> YELLOW
            case 2:

                trafficLight1.setLightState(YELLOW);
                trafficLight4.setLightState(YELLOW);

                trafficLight2.setLightState(RED);
                trafficLight3.setLightState(RED);

                trafficPhase = 3;

                timer.setDelay(2000);

                break;


            // East/West RED -> North/South GREEN
            case 3:

                trafficLight1.setLightState(RED);
                trafficLight4.setLightState(RED);

                trafficLight2.setLightState(GREEN);
                trafficLight3.setLightState(GREEN);

                trafficPhase = 0;

                timer.setDelay(5000);

                break;
        }
    }
}