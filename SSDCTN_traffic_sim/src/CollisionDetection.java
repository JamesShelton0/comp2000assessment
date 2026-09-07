import java.util.ArrayList;
import java.util.List;

public class CollisionDetection {

    // checks if one object is infront of the other and could face a collision 
    static boolean checkWithinRadius(Vehicle a, Vehicle b){
        if(a.getHitBox().detectHitboxOverlap(b.getHitBox())){
            //System.out.println("overlap!!!");
            return true;
        }
        return false;
    }

    static boolean checkWithinRadius(Vehicle v, StopLine s){
        if(s.getEdge().pointIsOnFlatEdge(v.hitBox.getPointAhead(), v.getDirection())){
            return true;
        }
        return false;
    }

    public static ArrayList<Vehicle> checkVehicleCollisions(List<Vehicle> vehicleArr){
        ArrayList<Vehicle> stopArr = new ArrayList<>();
        for(int i = 0; i < vehicleArr.size() - 1; i++){
            for(int j = i + 1; j < vehicleArr.size(); j++){
                if(checkWithinRadius(vehicleArr.get(i), vehicleArr.get(j))){
                    stopArr.add(vehicleArr.get(i));
                }
                if(checkWithinRadius(vehicleArr.get(j), vehicleArr.get(i))){
                    stopArr.add(vehicleArr.get(j));
                }
            }
        }
        return stopArr;
    }

    public static ArrayList<Vehicle> checkVehicleAtStopLine(ArrayList<StopLine> stopLineArr, List<Vehicle> vehicleArr){
        ArrayList<Vehicle> stopArr = new ArrayList<>();
        for(int i = 0; i < stopLineArr.size(); i++){
            for(int j = 0; j < vehicleArr.size(); j++){

            }
        }
        return stopArr;
    }
}
