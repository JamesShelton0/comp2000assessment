import java.util.ArrayList;

public class VehicleDespawner {
    ArrayList<Finish> finishArr;

    private final Finish NORTH_FP;
    private final Finish EAST_FP;
    private final Finish SOUTH_FP;
    private final Finish WEST_FP;
    private final Finish EASTTOO_FP;
    private final Finish WESTTOO_FP;


    VehicleDespawner(int startID, ArrayList<Finish> finishArr, Graph g){
        NORTH_FP = g.getVertex(1).getFinish();
        EAST_FP = g.getVertex(1).getFinish();
        SOUTH_FP = g.getVertex(1).getFinish();
        WEST_FP = g.getVertex(1).getFinish();
        EASTTOO_FP = g.getVertex(1).getFinish();
        WESTTOO_FP = g.getVertex(1).getFinish();
    }

    //despawns the vehicles 
    public void despawnVehicle(){

    }


}
