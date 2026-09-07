import java.util.ArrayList;

public class HitBox {
    double xAddon;
    double yAddon;
    double radius;
    Point pointAhead;
    double direction;
    boolean isActive;
    ArrayList<Point> pointArr = new ArrayList<>();
    ArrayList<Edge> edgeArr = new ArrayList<>();
    Point centrePoint;

    HitBox(Point p, double Direction, double width, double height){
        xAddon = height/2;
        yAddon = width/2;
        getRadius();
        direction = Direction;
        centrePoint = p;
        pointAhead = Velocity.calPositionWithRadius(centrePoint, direction, yAddon + 5);

        Point p1 = new Point(p.getX() + xAddon, p.getY() + yAddon);
        Point p2 = new Point(p.getX() + xAddon, p.getY() - yAddon);
        Point p3 = new Point(p.getX() - xAddon, p.getY() - yAddon);
        Point p4 = new Point(p.getX() - xAddon, p.getY() + yAddon);
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
        centrePoint = p;
        pointArr.set(0, Velocity.calPositionWithRadius(p, direction, radius, -1, 1, xAddon, yAddon));
        pointArr.set(1, Velocity.calPositionWithRadius(p, direction, radius, 1, 1, xAddon, yAddon));
        pointArr.set(2, Velocity.calPositionWithRadius(p, direction, radius, 1, -1, xAddon, yAddon));
        pointArr.set(3, Velocity.calPositionWithRadius(p, direction, radius, -1, -1, xAddon, yAddon));
        edgeArr.set(0, new Edge(pointArr.get(0), pointArr.get(1)));
        edgeArr.set(1, new Edge(pointArr.get(1), pointArr.get(2)));
        edgeArr.set(2, new Edge(pointArr.get(2), pointArr.get(3)));
        edgeArr.set(3, new Edge(pointArr.get(3), pointArr.get(0)));
        pointAhead = Velocity.calPositionWithRadius(centrePoint, direction, xAddon + 2);
    }

    public boolean detectHitboxOverlap(HitBox other){
        double thisLowerMostPoint = getLowerMostPoint();
        double thisUpperMostPoint = getUpperMostPoint();
        double thisLeftMostPoint = getLeftMostPoint();
        double thisRightMostPont = getRightMostPoint();
        double otherLowerMostPoint = other.getLowerMostPoint();
        double otherUpperMostPoint = other.getUpperMostPoint();
        double otherLeftMostPoint = other.getLeftMostPoint();
        double otherRightMostPoint = other.getRightMostPoint();
        if(direction == 270.0){
            if(pointAhead.getY() <= otherUpperMostPoint && thisUpperMostPoint > otherLowerMostPoint && ((thisLeftMostPoint > otherLeftMostPoint && thisLeftMostPoint < otherRightMostPoint) || (thisRightMostPont < otherRightMostPoint && thisRightMostPont > otherLeftMostPoint) || (centrePoint.getX() > otherLeftMostPoint && centrePoint.getX() < otherRightMostPoint))){
                return true;
            }
        }

        else if(direction == 0.0){
            if(pointAhead.getX() >= otherLeftMostPoint && thisLeftMostPoint < otherRightMostPoint && ((thisUpperMostPoint > otherLowerMostPoint && thisUpperMostPoint < otherUpperMostPoint) || (thisLowerMostPoint < otherUpperMostPoint && thisLowerMostPoint > otherLowerMostPoint) || (centrePoint.getY() > otherLowerMostPoint && centrePoint.getY() < otherUpperMostPoint))){
                //System.out.println("left true");
                return true;
            }
        }

        if(direction == 90.0){
            if(pointAhead.getY() >= otherLowerMostPoint && thisLowerMostPoint < otherUpperMostPoint && ((thisLeftMostPoint > otherLeftMostPoint && thisLeftMostPoint < otherRightMostPoint) || (thisRightMostPont < otherRightMostPoint && thisRightMostPont > otherLeftMostPoint) || (centrePoint.getX() > otherLeftMostPoint && centrePoint.getX() < otherRightMostPoint))){
                //System.out.println("upper true");
                return true;
            }
        }

        else if(direction == 180.0){
            if(pointAhead.getX() <= otherRightMostPoint && thisRightMostPont > otherLeftMostPoint && ((thisUpperMostPoint > otherLowerMostPoint && thisUpperMostPoint < otherUpperMostPoint) || (thisLowerMostPoint < otherUpperMostPoint && thisLowerMostPoint > otherLowerMostPoint) || (centrePoint.getY() > otherLowerMostPoint && centrePoint.getY() < otherUpperMostPoint))){
                //System.out.println("right true");
                return true;
            }
        }

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
        return false;
    }

    public Double getLeftMostPoint(){
        Double leftMostPoint = null;
        for(Point p : pointArr){
            if(leftMostPoint == null || p.getX() < leftMostPoint){
                leftMostPoint = p.getX();
            }
        }
        return leftMostPoint;
    }

    public Double getRightMostPoint(){
        Double rightMostPoint = 0.0;
        for(Point p : pointArr){
            if(rightMostPoint == null || p.getX() > rightMostPoint){
                rightMostPoint = p.getX();
            }
        }
        return rightMostPoint;
    }

    public Double getUpperMostPoint(){
        Double upperMostPoint = null;
        for(Point p : pointArr){
            if(upperMostPoint == null || p.getY() > upperMostPoint){
                upperMostPoint = p.getY();
            }
        }
        return upperMostPoint;
    }

    public Double getLowerMostPoint(){
        Double lowerMostPoint = null;
        for(Point p : pointArr){
            if(lowerMostPoint == null || p.getY() < lowerMostPoint){
                lowerMostPoint = p.getY();
            }
        }
        return lowerMostPoint;
    }

    public Point getPointAhead(){
        return pointAhead;
    }
    //public 

    public ArrayList<Point> getPointArr(){
        return pointArr;
    }

    private void getRadius(){
        radius = Math.sqrt(xAddon*xAddon + yAddon*yAddon);
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
    }

    public boolean getActive(){
        return isActive;
    }

    public double getDirection(){
        return direction;
    }
}
