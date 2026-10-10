import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

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
                Vehicle first = comparisonList.get(i);
                Vehicle second = comparisonList.get(j);

                // only rear vehicle in a shared lane recognises the vehicle ahead
                if (isFollowingTooClosely(first, second)) {
                    stopArr.add(first);
                }
                if (isFollowingTooClosely(second, first)) {
                    stopArr.add(second);
                }
            }
        }
        return stopArr;
    }

    public static Map<Vehicle, Double> calculateAllowedMovements(
            List<EntityStore<Vehicle>> vehicleArr,
            Map<Vehicle, Double> desiredMovements) {
        // start with relevant limits
        Map<Vehicle, Double> allowedMovements = new IdentityHashMap<>();
        for (EntityStore<Vehicle> sublist : vehicleArr) {
            for (Vehicle vehicle : sublist.getEntities()) {
                allowedMovements.put(
                    vehicle,
                    Math.max(0, desiredMovements.getOrDefault(vehicle, vehicle.velocity))
                );
            }
        }

        // each store is one directional lane, so only the nearest leader limits a follower
        for (EntityStore<Vehicle> sublist : vehicleArr) {
            ArrayList<Vehicle> lane = new ArrayList<>(sublist.getEntities());
            lane.sort((first, second) -> Double.compare(
                getTravelProgress(second),
                getTravelProgress(first)
            ));

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

    private static boolean isFollowingTooClosely(Vehicle follower, Vehicle leader) {
        // ignore vehicles that are behind or in another lane or travelling in another direction
        double forwardGap = getForwardGapInSameLane(follower, leader);
        if (Double.isNaN(forwardGap)) {
            return false;
        }

        // prevents overlap from fast vehicles
        double safeFollowingDistance = getSafeFollowingDistance(follower, leader);
        double leaderMovement = leader.isMovementBlocked() ? 0 : leader.velocity;
        double closingDistance = Math.max(0, follower.velocity - leaderMovement);
        return forwardGap <= safeFollowingDistance + closingDistance;
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
