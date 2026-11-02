import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.*;

public class CollisionDetection {

    private static List<Map<Integer, EntityStore<Vehicle>>> turningVehiclesList;

    public static Vehicle[] findCollision(List<EntityStore<Vehicle>> vehicleArr) {
        // t-bone collision requires vehicles from diff lanes travelling at right angles
        for (int firstLane = 0; firstLane < vehicleArr.size() - 1; firstLane++) {
            for (int secondLane = firstLane + 1; secondLane < vehicleArr.size(); secondLane++) {
                for (Vehicle firstVehicle : vehicleArr.get(firstLane).getEntities()) {
                    for (Vehicle secondVehicle : vehicleArr.get(secondLane).getEntities()) {
                        if (isPerpendicular(firstVehicle, secondVehicle)
                                && firstVehicle.getHitBox().overlaps(secondVehicle.getHitBox())) {
                            // return first crash pair so Panel can create explosion YAY!!!
                            return new Vehicle[] { firstVehicle, secondVehicle };
                        }
                    }
                }
            }
        }
        return null;
    }

    private static boolean isPerpendicular(Vehicle firstVehicle, Vehicle secondVehicle) {
        // perpendicular cardinal directions differ by 90 or 270 degrees #math
        int directionDifference = Math.abs(
            (int) Math.round(firstVehicle.getDirection() - secondVehicle.getDirection())
        );
        return directionDifference == 90 || directionDifference == 270;
    }

    public static void detectHitboxCollision(List<EntityStore<Vehicle>> vehicleArr){
        for(int i = 0; i < vehicleArr.size(); i++){
            for(int j = 0; j < vehicleArr.get(i).size(); j++){
                for(int k = 0; k < vehicleArr.size(); k++){
                    for(int l = 0; l < vehicleArr.get(k).size(); l++){
                        if(vehicleArr.get(k).get(l).getHitBox().pointInHitBox(vehicleArr.get(i).get(j).getHitBox().getPointAhead())){
                            vehicleArr.get(i).get(j).setMovementBlocked(true);
                        }
                        else{
                            vehicleArr.get(i).get(j).setMovementBlocked(false);
                        }
                    }
                }
            }
        }
    }

    public static void detectTurningHitboxOverLap(){
        for(int i = 0; i < turningVehiclesList.size(); i++){
            for(int j = 0; j < 2; j++){
                for(int k = 0; k < turningVehiclesList.get(i).get(j).size() - 1; k++){
                    for(int l = 0; l < turningVehiclesList.size(); l++){
                        for(int m = 0; m < 2; m++){
                            for(int n = 1; n < turningVehiclesList.get(l).get(m).size(); n++){
                                //System.out.println("Vehicle 1 X: " + turningVehiclesList.get(i).get(j).get(k).getPosition().getX() + " Vehicle 1 Y: " + turningVehiclesList.get(i).get(j).get(k).getPosition().getY());
                                //System.out.println("Vehicle 2 X: " + turningVehiclesList.get(l).get(m).get(n).getPosition().getX() + " Vehicle 2 Y: " + turningVehiclesList.get(l).get(m).get(n).getPosition().getY());
                                if(turningVehiclesList.get(i).get(j).get(k).getHitBox().detectHitboxOverlap(turningVehiclesList.get(l).get(m).get(n).getHitBox())){
                                    //System.out.println();
                                    turningVehiclesList.get(i).get(j).get(k).setMovementBlocked(true);
                                    turningVehiclesList.get(l).get(m).get(n).setMovementBlocked(true);
                                }
                                else{
                                    turningVehiclesList.get(i).get(j).get(k).setMovementBlocked(false);
                                    turningVehiclesList.get(l).get(m).get(n).setMovementBlocked(false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void changeTurningVehicleToDiffEntityStore(List<EntityStore<Vehicle>> vehicleArr, HashMap<EntityStore<Vehicle>, ArrayList<EntityStore<Vehicle>>> mapLanes){
        turningVehiclesList = new ArrayList<Map<Integer, EntityStore<Vehicle>>>();

        for(int i = 0; i < vehicleArr.size(); i++){

            Map<Integer, EntityStore<Vehicle>> turningVehicles = vehicleArr.get(i).stream().collect(new TurningVehicleCollector<Vehicle>());
            turningVehiclesList.add(turningVehicles);

            for(int j = 0; j < turningVehicles.get(0).size(); j++){
                boolean flag = turningVehicles.get(0).get(j).getFlag();
                if(!flag){
                    turningVehicles.get(0).get(j).setFlag(true);
                    moveToCorrectEntityStore(vehicleArr.get(i), turningVehicles.get(0).get(j), mapLanes,0);
                }
            }
            for(int j = 0; j < turningVehicles.get(1).size(); j++){
                boolean flag = turningVehicles.get(1).get(j).getFlag();
                if(!flag){
                    turningVehicles.get(1).get(j).setFlag(true);
                    moveToCorrectEntityStore(vehicleArr.get(i), turningVehicles.get(1).get(j), mapLanes,1);
                }
            }
        }
    }

    public static void moveToCorrectEntityStore(EntityStore<Vehicle> arr, Vehicle v, HashMap<EntityStore<Vehicle>, ArrayList<EntityStore<Vehicle>>> mapLanes, int dir){
        ArrayList<EntityStore<Vehicle>> potentialNewLanes = mapLanes.get(arr);
        if((dir == 0 && potentialNewLanes.size() == 2) || (dir == 0 && potentialNewLanes.size() == 4 && v.getPosition().getY() < App.height*0.5)){
            potentialNewLanes.get(0).add(v);
            arr.remove(v);
        }else if((dir == 1 && potentialNewLanes.size() == 2) || (dir == 1 && potentialNewLanes.size() == 4 && v.getPosition().getY() < App.height*0.5)){
            potentialNewLanes.get(1).add(v);
            arr.remove(v);
        }else if(dir == 0 && potentialNewLanes.size() == 4){
            if(v.getPosition().getY() > App.height*0.5){
                potentialNewLanes.get(2).add(v);
            }
            else{
                potentialNewLanes.get(3).add(v);
            }
            arr.remove(v);
        }else if(dir == 1 && potentialNewLanes.size() == 4){
            if(v.getPosition().getY() > App.height*0.5){
                potentialNewLanes.get(3).add(v);
            }
            else{
                potentialNewLanes.get(2).add(v);
            }
            arr.remove(v);
        }
    }

    public static Map<Vehicle, Double> calculateAllowedMovementsTwo(List<EntityStore<Vehicle>> vehicleArr, Map<Vehicle, Double> desiredMovements){

        Map<Vehicle,Double> allowedMovements = vehicleArr.stream()
                                                         .flatMap(sublist -> sublist.stream())
                                                         .collect(Collectors.toMap(
                                                                v -> v,
                                                                v -> Math.max(0, desiredMovements.getOrDefault(v, v.velocity)),
                                                                (a, b) -> a,
                                                                IdentityHashMap::new)
                                                         );

        for(int i = 0; i < vehicleArr.size(); i++){
            for(int j = 0; j < vehicleArr.get(i).size(); j++){
                for(int k = 0; k < vehicleArr.size(); k++){
                    for(int l = 0; l < vehicleArr.get(k).size(); l++){
                        if(vehicleArr.get(i).get(j).getHitBox().detectHitboxOverlap(vehicleArr.get(k).get(l).getHitBox())){
                            allowedMovements.put(vehicleArr.get(i).get(j), 0.0);
                        }
                        else{
                            allowedMovements.put(vehicleArr.get(i).get(j), vehicleArr.get(i).get(j).getVelocity());
                        }
                    }
                }
            }
        }

        return allowedMovements;
    }


    public static Map<Vehicle, Double> calculateAllowedMovements(
            List<EntityStore<Vehicle>> vehicleArr,
            Map<Vehicle, Double> desiredMovements) {

        // start with relevant limits
        Map<Vehicle, Double> allowedMovements = vehicleArr.stream()
                            .flatMap(sublist -> sublist.stream())
                            .collect(Collectors.toMap(                                      // Using four parameter toMap()
                                v -> v,                                                         // 1. key
                                v -> Math.max(0, desiredMovements.getOrDefault(v, v.velocity)), // 2. value
                                (a, b) -> a,                                                    // 3. keep existing value if duplicate key
                                IdentityHashMap::new)                                           // 4. specify map type
                            );

        // each store is one directional lane, so only the nearest leader limits a follower
        for (EntityStore<Vehicle> sublist : vehicleArr) {
            ArrayList<Vehicle> lane = sublist.stream()
                                          .sorted((first, second) -> Double.compare(getTravelProgress(second), getTravelProgress(first)))
                                          .collect(Collectors.toCollection(ArrayList::new));

            // front-to-back order passes the lead vehicle's movement through the whole queue
            for (int followerIndex = 1; followerIndex < lane.size(); followerIndex++) {
                Vehicle follower = lane.get(followerIndex);
                Vehicle leader = lane.get(followerIndex - 1);
                double forwardGap = getForwardGapInSameLane(follower, leader);
                if (Double.isNaN(forwardGap)) {
                    continue;
                }

                double availableGap = forwardGap - getSafeFollowingDistance(follower, leader);
                double leaderMovement = allowedMovements.getOrDefault(leader, 0.0);
                double followingLimit = Math.max(0, availableGap + leaderMovement);
                double currentAllowance = allowedMovements.get(follower);
                allowedMovements.put(follower, Math.min(currentAllowance, followingLimit));
            }
        }

        return allowedMovements;
    }

    private static double getTravelProgress(Vehicle vehicle) {
        // convert position into distance travelled along the current direction
        double directionRadians = Math.toRadians(vehicle.getDirection());
        return vehicle.getPosition().getX() * Math.cos(directionRadians)
            + vehicle.getPosition().getY() * Math.sin(directionRadians);
    }


    private static double getForwardGapInSameLane(Vehicle follower, Vehicle leader) {
        if (Math.round(follower.getDirection()) != Math.round(leader.getDirection())) {
            return Double.NaN;
        }

        double directionRadians = Math.toRadians(follower.getDirection());
        double deltaX = leader.getPosition().getX() - follower.getPosition().getX();
        double deltaY = leader.getPosition().getY() - follower.getPosition().getY();
        double forwardGap = deltaX * Math.cos(directionRadians)
            + deltaY * Math.sin(directionRadians);
        if (forwardGap <= 0) {
            return Double.NaN;
        }

        // reject any adjacent lanes
        double lateralGap = Math.abs(
            -deltaX * Math.sin(directionRadians)
            + deltaY * Math.cos(directionRadians)
        );
        double laneOverlapDistance = follower.getHitBox().getLateralExtent()
            + leader.getHitBox().getLateralExtent();
        return lateralGap <= laneOverlapDistance ? forwardGap : Double.NaN;
    }

    private static double getSafeFollowingDistance(Vehicle follower, Vehicle leader) {
        // combine buffered front and rear extends with a gap
        return follower.getHitBox().getForwardExtent()
            + leader.getHitBox().getForwardExtent()
            + 2;
    }
}
