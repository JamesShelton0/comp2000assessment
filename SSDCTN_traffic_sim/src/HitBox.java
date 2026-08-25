import java.util.ArrayList;

public class HitBox {
    float xAddon;
    float yAddon;
    double radius;

    ArrayList<Point> arr = new ArrayList<>();
    HitBox(Point p, int Direction, float width, float length){
        xAddon = width/2;
        yAddon = length/2;
        hitBoxPointRadius();
        Point p1 = new Point(p.getX() - xAddon, p.getY() + yAddon);
        Point p2 = new Point(p.getX() + xAddon, p.getY() + yAddon);
        Point p3 = new Point(p.getX() + xAddon, p.getY() - yAddon);
        Point p4 = new Point(p.getX() - xAddon, p.getY() - yAddon);
        arr.add(p1);
        arr.add(p2);
        arr.add(p3);
        arr.add(p4);
    }

    public void updateHitbox(){
        for(int i = 0; i < arr.size(); i++){
            arr.set(i, null);
        }
    }

    public void hitBoxPointRadius(){
        radius = Math.sqrt(xAddon*xAddon + yAddon*yAddon);
    }
}
