public class Bus extends Vehicle{

    private int x;
    private int y;

    private float velocity = 60; 
    private float decelerationRate = 5;

    private boolean stoppedAtBusStop = false;  //if the bus is sotop atm
    private boolean leavingBusStop = false;
    
    private int waitTime = 0; //how long has it waitted
    private int maxWaitTtime = 100; //low long it will wait


    //check if the bus is close to a bus stop
    void checkBusStop(BusStop stop) { 
        if(x >= stop.x - 10 && x <= stop.x){
           decelerate();
           
           if(velocity == 0){
            stoppedAtBusStop = true;
           }
        }
    }
     @Override
    void decelerate(){
        velocity -= decelerationRate;

        if(velocity < 0){
            velocity = 0;
        }

    }
    void waitAtBusStop() {
        if(stoppedAtBusStop){
            waitTime++;
            if(waitTime >= maxWaitTtime){
                stoppedAtBusStop = false;
                leavingBusStop = true;
                waitTime = 0;
        }
    }
}

void leavBusStop(){
    if(leavingBusStop){
        accelerate();
        
         if(velocity >= topSpeed){
        velocity = topSpeed;
        leavingBusStop = false;
    }
   
    }
}
         
          
 }
      
    
