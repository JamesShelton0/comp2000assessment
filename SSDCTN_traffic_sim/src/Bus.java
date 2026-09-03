public class Bus extends Vehicle {

    private boolean stoppedAtBusStop = false;  //if the bus is stopped atm
    private boolean leavingBusStop = false;
    
    private int waitTime = 0; //how long has it waited
    private int maxWaitTime = 100; //how long it will wait

    Bus(double width, double height) {
        super.width = width;
        super.height = height;
        velocity = 60;
        topSpeed = 60;
        decelerationRate = -5;
        accelerationRate = 5;   
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
      
    
