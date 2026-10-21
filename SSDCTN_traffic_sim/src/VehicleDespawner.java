import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class VehicleDespawner {
    ArrayList<Finish> finishArr;
    private final int SPAWN_BUFFER = (int) ((App.width+App.height)/2 * 0.15);

    //despawns the vehicles 
    public void despawnVehicle(List<EntityStore<Vehicle>> activeVehicles){
        for (EntityStore<Vehicle> sublist : activeVehicles) 
            despawnHelper(sublist);
    }

    private void despawnHelper(EntityStore<Vehicle> sublist) {
        Iterator<Vehicle> iterator = sublist.modifyEntities().iterator();
        while (iterator.hasNext()) {
            Vehicle v = iterator.next();
            if (v.getPosition().getX() < 0-SPAWN_BUFFER
            || v.getPosition().getY() < 0-SPAWN_BUFFER
            || v.getPosition().getX() > App.width+SPAWN_BUFFER
            || v.getPosition().getY() > App.height+SPAWN_BUFFER) {
                // System.out.println("Vehicle despawned at " + v.getPosition());
                iterator.remove();
            }
        }
    }


}
