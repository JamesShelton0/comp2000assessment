public class Bus extends Vehicle {

    // private int x;       [declared in Vehicle]
    // private int y;       [declared in Vehicle]

    // private float velocity = 60;         [declared in vehicle, initialise in constructor]
    // private float decelerationRate = -5; [declared in vehicle, initialise in constructor]
    // private int accelerationRate = 5;    [declared in vehicle, initialise in constructor]

    private boolean stoppedAtBusStop = false;  //if the bus is stopped atm
    private boolean leavingBusStop = false;
    
    private int waitTime = 0; //how long has it waited
    private int maxWaitTime = 100; //how long it will wait

    Bus() {
        super.velocity = 60;
        super.decelerationRate = -5;
        super.accelerationRate = 5;   // just set a random value so that your leaveBusStop() didn't break. Change if u want of course!
    }

    //check if the bus is close to a bus stop
    void checkBusStop(BusStop stop) { 
        if(x >= stop.x - 10 && x <= stop.x){
           accelerate(decelerationRate);
           
           if(velocity == 0){
            stoppedAtBusStop = true;
           }
        }
    }

    void waitAtBusStop() {
        if(stoppedAtBusStop){
            waitTime++;
            if(waitTime >= maxWaitTime){
                stoppedAtBusStop = false;
                leavingBusStop = true;
                waitTime = 0;
            }
        }
    }

    void leaveBusStop(){
        if(leavingBusStop){
            accelerate(accelerationRate);
        
            if(velocity >= topSpeed){
                velocity = topSpeed;
                leavingBusStop = false;
            }
        }
    }
}
      
    
