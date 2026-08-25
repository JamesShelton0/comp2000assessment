import java.util.ArrayList;

public class HitBox {
    float xAddon;
    float yAddon;
    double radius;

    ArrayList<Point> arr = new ArrayList<>();
    HitBox(Point p, int Direction, float width, float length){
        xAddon = width/2;
        yAddon = length/2;
        Point p1 = new Point(p.getX() - xAddon, p.getY() + yAddon);
        Point p2 = new Point(p.getX() + xAddon, p.getY() + yAddon);
        Point p3 = new Point(p.getX() + xAddon, p.getY() - yAddon);
        Point p4 = new Point(p.getX() - xAddon, p.getY() - yAddon);
        arr.add(p1);
        arr.add(p2);
        arr.add(p3);
        arr.add(p4);
    }

    public void updateHitbox(int direction){
        arr.set(0, Velocity.calPosiitonWithRadius(arr.get(0), 0, -xAddon, yAddon));
        arr.set(1, Velocity.calPosiitonWithRadius(arr.get(1), 0, xAddon, yAddon));
        arr.set(2, Velocity.calPosiitonWithRadius(arr.get(2), 0, xAddon, -yAddon));
        arr.set(3, Velocity.calPosiitonWithRadius(arr.get(3), 0, -xAddon, -yAddon));
    }

    public boolean detectHitboxOverlap(HitBox other){
        for(int i  = 0; i < other.getPointArr().size(); i ++){
            if(pointInHitBox(other.getPointArr().get(i))){
                return true;
            }
        }
        return false;
    }

    public boolean pointInHitBox(Point p){
        if(true){

        }
        return true;
    }

    public ArrayList<Point> getPointArr(){
        return arr;
    }
}
