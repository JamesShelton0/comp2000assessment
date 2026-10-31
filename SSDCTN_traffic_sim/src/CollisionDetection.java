import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.*;

public class CollisionDetection {

    // stores a rear-end collision detected during movement calculation
    private static Vehicle[] rearEndCollision = null;

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
        // detect rear-end collisions caused by speeding cars
        for (EntityStore<Vehicle> sublist : vehicleArr) {
            ArrayList<Vehicle> lane = new ArrayList<>(sublist.getEntities());

            for(Vehicle follower : lane) {
                if (!(follower instanceof SpeedingCar)){
                    continue;
                }
                for (Vehicle leader : lane) {
                    if (follower == leader) {
                        continue;
                    }
                    double forwardGap = getForwardGapInSameLane(follower, leader);

                    if (!Double.isNaN(forwardGap)
                           && follower.getHitBox().overlaps(leader.getHitBox())) {
                        return new Vehicle[] {follower, leader};
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

                // speeding cars do not keep the normal safe following distance
                double requiredDistance;

               if(follower instanceof SpeedingCar) {
                    requiredDistance = getCollisionDistance(follower, leader);
                }else{
                    requiredDistance = getSafeFollowingDistance(follower, leader);
                }
                double availableGap = forwardGap - requiredDistance;
                double leaderMovement = allowedMovements.getOrDefault(leader, 0.0);
                double followingLimit = Math.max(0, availableGap + leaderMovement);
                double currentAllowance = allowedMovements.get(follower);

                // detect when a speeding car would hit the vehicle ahead
                if (follower instanceof SpeedingCar
                    && currentAllowance > followingLimit
                    && rearEndCollision == null) {
                        rearEndCollision = new Vehicle[] {follower,leader};
                    }
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
    // calculate the distance at which two vehicless touch
    private static double getCollisionDistance(Vehicle follower, Vehicle leader){
        return follower.getHitBox().getForwardExtent()
        + leader.getHitBox().getForwardExtent();
    }
    private static double getSafeFollowingDistance(Vehicle follower, Vehicle leader) {
        // combine buffered front and rear extends with a gap
        return follower.getHitBox().getForwardExtent()
            + leader.getHitBox().getForwardExtent()
            + 2;
    }
    // returns the rear-end collision and clears it for the next frame
    public static Vehicle[] getRearEndCollision(){
        Vehicle[] collision = rearEndCollision;
        rearEndCollision = null;
        return collision;
    }
}
