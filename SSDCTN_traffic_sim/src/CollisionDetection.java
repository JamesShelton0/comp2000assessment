public class CollisionDetection {

    // checks if one object is infront of the other and could face a collision 
    static void checkWithinRadius(Vehicle a, Vehicle b){
        if(b.hitBox.pointInHitBox(a.hitBox.getPointAhead())){

        }
        //return false;
    }
}
