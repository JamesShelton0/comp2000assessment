import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.*;

public class CollisionDetection {

    // checks if one object is infront of the other and could face a collision 
    static boolean checkWithinRadius(Vehicle a, Vehicle b){
        try {
            if (a.getHitBox().detectHitboxOverlap(b.getHitBox())) {
                return true;
            }
        } catch(NullPointerException e){
            System.out.println("EXCEPTION: One of the vehicle objects has a null hitbox");
        }
        return false;
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

        // restore collision checks between different spawn-point stores, which the lane-only Map pass omitted
        for (int firstStoreIndex = 0; firstStoreIndex < vehicleArr.size() - 1; firstStoreIndex++) {
            EntityStore<Vehicle> firstStore = vehicleArr.get(firstStoreIndex);
            for (int secondStoreIndex = firstStoreIndex + 1;
                    secondStoreIndex < vehicleArr.size(); secondStoreIndex++) {
                EntityStore<Vehicle> secondStore = vehicleArr.get(secondStoreIndex);
                for (Vehicle first : firstStore.getEntities()) {
                    for (Vehicle second : secondStore.getEntities()) {
                        // stop both vehicles once either directional hitbox check detects impact
                        if (checkWithinRadius(first, second) || checkWithinRadius(second, first)) {
                            allowedMovements.put(first, 0.0);
                            allowedMovements.put(second, 0.0);
                        }
                    }
                }
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
