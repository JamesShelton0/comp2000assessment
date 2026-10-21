public class Turn {
    private int turnID;
    private Point rotationPoint;
    private int direction;
    private final static int LEFT = 0;
    private final static int RIGHT = 1;
    private final static int STRAIGHT = 2;

    private float radius;

    private float startAngle;
    private float finishAngle;

    private final static double NORTH = 270.0;
    private final static double EAST = 0.0;
    private final static double SOUTH = 90.0;
    private final static double WEST = 180.0;

    Turn(int turnID, Point rotationPoint, float radius, int direction, float startAngle){
        this.turnID = turnID;

        this.rotationPoint = setTurnPoint(rotationPoint);
        this.radius = radius;
        this.startAngle = startAngle;

        switch (direction) {
            case LEFT:
                this.direction = LEFT;
                this.finishAngle = this.startAngle + 90;
                break;
            case RIGHT:
                this.direction = RIGHT;
                this.finishAngle = this.startAngle - 90;
                break;
            case STRAIGHT:
                this.direction = STRAIGHT;
                this.finishAngle = this.startAngle;
                break;
            default:
                this.direction = STRAIGHT;
                this.finishAngle = this.startAngle;
                break;
        }   
        
        if(this.finishAngle >= 360){this.finishAngle -= 360;}
        else if(this.finishAngle < 0){this.finishAngle += 360;}
    }

    private Point setTurnPoint(Point rp){
        double x = rp.getX()*App.width;
        double y = rp.getY()*App.height;
        return new Point(x, y);
    } 

    public boolean vehicleOverLapTurn(Vehicle vehicle){
        if(direction == LEFT){
            return vehicleOverlapLeftTurn(vehicle.getPosition());
        }
        else if(direction == RIGHT){
            return vehicleOverlapRightTurn(vehicle.getPosition());
        }
        return false;
    }

    private boolean vehicleOverlapLeftTurn(Point vehiclePoint){
        if(startAngle == NORTH){
            checkOverlapNORTHLeftTurn(vehiclePoint);
        }
        else if(startAngle == EAST){
            checkOverlapEASTLeftTurn(vehiclePoint);
        }
        else if(startAngle == SOUTH){
            checkOverlapSOUTHLeftTurn(vehiclePoint);
        }
        else if(startAngle == WEST){
            checkOverlapWESTLeftTurn(vehiclePoint);
        }
        return false;
    }

    private boolean vehicleOverlapRightTurn(Point vehiclePoint){
        if(startAngle == NORTH){
            checkOverlapNORTHRightTurn(vehiclePoint);
        }
        else if(startAngle == EAST){
            checkOverlapEASTRightTurn(vehiclePoint);
        }
        else if(startAngle == SOUTH){
            checkOverlapSOUTHRightTurn(vehiclePoint);
        }
        else if(startAngle == WEST){
            checkOverlapWESTRightTurn(vehiclePoint);
        }
        return false;
    }

    private boolean checkOverlapNORTHLeftTurn(Point vehiclePoint){
        if(vehiclePoint.getY() == rotationPoint.getY() && vehiclePoint.getX() >= rotationPoint.getX() && vehiclePoint.getX() <= rotationPoint.getX() + radius){
            return true;
        }
        return false;
    }

    private boolean checkOverlapEASTLeftTurn(Point vehiclePoint){
        if(vehiclePoint.getX() == rotationPoint.getX() && vehiclePoint.getY() >= rotationPoint.getY() && vehiclePoint.getY() <= rotationPoint.getY() + radius){
            return true;
        }
        return false;
    }

    private boolean checkOverlapSOUTHLeftTurn(Point vehiclePoint){
        if(vehiclePoint.getY() == rotationPoint.getY() && vehiclePoint.getX() <= rotationPoint.getX() && vehiclePoint.getX() >= rotationPoint.getX() - radius){
            return true;
        }
        return false;
    }

    private boolean checkOverlapWESTLeftTurn(Point vehiclePoint){
        if(vehiclePoint.getX() == rotationPoint.getX() && vehiclePoint.getY() <= rotationPoint.getY() && vehiclePoint.getY() >= rotationPoint.getY() - radius){
            return true;
        }
        return false;
    }

    private boolean checkOverlapNORTHRightTurn(Point vehiclePoint){
        if(vehiclePoint.getY() == rotationPoint.getY() && vehiclePoint.getX() <= rotationPoint.getX() && vehiclePoint.getX() >= rotationPoint.getX() - radius){
            return true;
        }
        return false;
    }

    private boolean checkOverlapEASTRightTurn(Point vehiclePoint){
        if(vehiclePoint.getX() == rotationPoint.getX() && vehiclePoint.getY() <= rotationPoint.getY() && vehiclePoint.getY() >= rotationPoint.getY() - radius){
            return true;
        }
        return false;
    }

    private boolean checkOverlapSOUTHRightTurn(Point vehiclePoint){
        if(vehiclePoint.getY() == rotationPoint.getY() && vehiclePoint.getX() >= rotationPoint.getX() && vehiclePoint.getX() <= rotationPoint.getX() + radius){
            return true;
        }
        return false;
    }

    private boolean checkOverlapWESTRightTurn(Point vehiclePoint){
        if(vehiclePoint.getX() == rotationPoint.getX() && vehiclePoint.getY() >= rotationPoint.getY() && vehiclePoint.getY() <= rotationPoint.getY() + radius){
            return true;
        }
        return false;
    }

    public float getRadius(){
        return radius;
    }

    public float getFinishAngle(){
        return finishAngle;
    }

    public int getID(){
        return turnID;
    }
}
