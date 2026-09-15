import java.util.ArrayList;
import java.util.List;

public class CollisionDetection {

    // checks if one object is infront of the other and could face a collision 
    static boolean checkWithinRadius(Vehicle a, Vehicle b){
        try{
            if(a.getHitBox().detectHitboxOverlap(b.getHitBox())){
                return true;
            }
        }
        catch(NullPointerException e){
            System.out.println("EXCEPTION: One of the vehicle objects has a null hitbox");
        }
        return false;
    }

    static boolean checkWithinRadius(Vehicle v, StopLine s){
        if(s.getEdge().pointIsOnFlatEdge(v.hitBox.getPointAhead(), v.getDirection())){
            return true;
        }
        return false;
    }

    public static ArrayList<Vehicle> checkVehicleCollisions(List<EntityStore<Vehicle>> vehicleArr){
        
        ArrayList<Vehicle> comparisonList = new ArrayList<>();
        for (EntityStore<Vehicle> sublist : vehicleArr) {
            for (Vehicle v : sublist.getEntities()) {
                comparisonList.add(v);
            }
        }

        ArrayList<Vehicle> stopArr = new ArrayList<>();
        for(int i = 0; i < comparisonList.size() - 1; i++){
            for(int j = i + 1; j < comparisonList.size(); j++){
                if(checkWithinRadius(comparisonList.get(i), comparisonList.get(j))){
                    stopArr.add(comparisonList.get(i));
                }
                if(checkWithinRadius(comparisonList.get(j), comparisonList.get(i))){
                    stopArr.add(comparisonList.get(j));
                }
            }
        }
        return stopArr;
    }

    public static ArrayList<Vehicle> checkVehicleAtStopLine(ArrayList<StopLine> stopLineArr, List<EntityStore<Vehicle>> vehicleArr){
        ArrayList<Vehicle> stopArr = new ArrayList<>();
        for (EntityStore<Vehicle> sublist : vehicleArr) {
            for(int i = 0; i < sublist.getEntities().size(); i++){
                for(int j = 0; j < sublist.getEntities().size(); j++){

                }
            }
        }
        return stopArr;
    }
}
