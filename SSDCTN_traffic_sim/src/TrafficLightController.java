import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

public class TrafficLightController {

    // each group receives the same update when its traffic direction changes
    private final List<TrafficLightObserver> northSouthLights = new ArrayList<>();
    private final List<TrafficLightObserver> eastWestLights = new ArrayList<>();

    private Timer timer;
    private int trafficPhase = 0;

    private final int GREEN = 0;
    private final int YELLOW = 1;
    private final int RED = 2;

    private final int GREEN_TIME = 20000; // 20 seconds I think these times work best, other times glitch out
    private final int YELLOW_TIME = 4500; // 4 seconds

    public TrafficLightController(
        TrafficLight trafficLight1,
        TrafficLight trafficLight2,
        TrafficLight trafficLight3,
        TrafficLight trafficLight4,
        TrafficLight trafficLight5,
        TrafficLight trafficLight6,
        TrafficLight trafficLight7,
        TrafficLight trafficLight8
    ) {

        // register the active lights as observers for travel direction
        northSouthLights.add(trafficLight2);
        northSouthLights.add(trafficLight3);
        eastWestLights.add(trafficLight1);
        eastWestLights.add(trafficLight4);

        // Starting state

        // East / West RED
        notifyObservers(eastWestLights, RED);
        trafficLight5.setLightState(RED); 
        trafficLight8.setLightState(RED);

        // North / South GREEN
        notifyObservers(northSouthLights, GREEN);
        trafficLight6.setLightState(GREEN);
        trafficLight7.setLightState(GREEN);

        timer = new Timer(GREEN_TIME, e -> changePhase());

        // Timer only fires once
        timer.setRepeats(false);
    }


    public void start() {
        // System.out.println("TrafficLightController.start() called");
        timer.start();
    }


    private void changePhase() {
        // System.out.println("TrafficLightController.changePhase() called");
        switch (trafficPhase) {

            // North/South GREEN -> YELLOW
            case 0:
                notifyObservers(northSouthLights, YELLOW);
                notifyObservers(eastWestLights, RED);

                trafficPhase = 1;

                startTimer(YELLOW_TIME);

                break;


            // North/South RED -> East/West GREEN
            case 1:
                notifyObservers(northSouthLights, RED);
                notifyObservers(eastWestLights, GREEN);

                trafficPhase = 2;

                startTimer(GREEN_TIME);

                break;


            // East/West GREEN -> YELLOW
            case 2:
                notifyObservers(eastWestLights, YELLOW);
                notifyObservers(northSouthLights, RED);

                trafficPhase = 3;

                startTimer(YELLOW_TIME);

                break;


            // East/West RED -> North/South GREEN
            case 3:
                notifyObservers(eastWestLights, RED);
                notifyObservers(northSouthLights, GREEN);

                trafficPhase = 0;

                startTimer(GREEN_TIME);

                break;
        }
    }


    private void startTimer(int delay) {

        timer.setInitialDelay(delay);
        timer.restart();
    }

    private void notifyObservers(List<TrafficLightObserver> lights, int state) {
        // send one state change to every light in the selected direction
        for (TrafficLightObserver light : lights) {
            light.updateLightState(state);
        }
    }
}
