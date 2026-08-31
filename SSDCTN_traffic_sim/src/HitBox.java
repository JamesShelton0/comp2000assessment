import java.util.ArrayList;

public class HitBox {
    float xAddon;
    float yAddon;
    double radius;
    double direction;
    ArrayList<Point> pointArr = new ArrayList<>();
    ArrayList<Edge> edgeArr = new ArrayList<>();
    Point centrePoint;

    HitBox(Point p, int Direction, float width, float height){
        xAddon = width/2;
        yAddon = height/2;
        getRadius();
        centrePoint = p;

        Point p1 = new Point(p.getX() - xAddon, p.getY() + yAddon);
        Point p2 = new Point(p.getX() + xAddon, p.getY() + yAddon);
        Point p3 = new Point(p.getX() + xAddon, p.getY() - yAddon);
        Point p4 = new Point(p.getX() - xAddon, p.getY() - yAddon);
        pointArr.add(p1);
        pointArr.add(p2);
        pointArr.add(p3);
        pointArr.add(p4);

        Edge e1 = new Edge(p1, p2);
        Edge e2 = new Edge(p2, p3);
        Edge e3 = new Edge(p3, p4);
        Edge e4 = new Edge(p4, p1);
        edgeArr.add(e1);
        edgeArr.add(e2);
        edgeArr.add(e3);
        edgeArr.add(e4);        
    }

    public void updateHitbox(Point p, double direction){
        this.direction = direction;
        pointArr.set(0, Velocity.calPositionWithRadius(p, direction, radius, -1, 1));
        pointArr.set(1, Velocity.calPositionWithRadius(p, direction, radius, 1, 1));
        pointArr.set(2, Velocity.calPositionWithRadius(p, direction, radius, 1, -1));
        pointArr.set(3, Velocity.calPositionWithRadius(p, direction, radius, -1, -1));
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
        if(direction % 90 != 0){
            for(int i = 0; i < 4; i++){
                if(edgeArr.get(i).pointHasPassedEdge(p)){
                    return true;
                }
            }
        }
        if(direction % 90 == 0 && direction % 180 != 0){
            double xDistToPoint = Math.sqrt((centrePoint.getX() - p.getX()) * (centrePoint.getX() - p.getX()));
            double yDistToPoint = Math.sqrt((centrePoint.getY() - p.getY()) * (centrePoint.getY() - p.getY()));
            if(xDistToPoint < yAddon || yDistToPoint < xAddon){
                return true;
            }
        }
        else if(direction % 90 == 0 && direction % 180 == 0){
            double xDistToPoint = Math.sqrt((centrePoint.getX() - p.getX()) * (centrePoint.getX() - p.getX()));
            double yDistToPoint = Math.sqrt((centrePoint.getY() - p.getY()) * (centrePoint.getY() - p.getY()));
            if(xDistToPoint < xAddon || yDistToPoint < yAddon){
                return true;
            }
        }
        return false;
    }

    //public 

    public ArrayList<Point> getPointArr(){
        return pointArr;
    }

    private void getRadius(){
        radius = Math.sqrt(xAddon*xAddon + yAddon*yAddon);
    }
}
