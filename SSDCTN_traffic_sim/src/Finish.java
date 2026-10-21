public class Finish {
    Point finishPoint;
    int ID;
    private static final int FINISH_BUFFER = (int) ((App.width+App.height)/2 * 0.15);

    Finish(Point finishPoint, int ID){
        this.finishPoint = addFinishBufferToPoint(finishPoint);
        this.ID = ID;
    }

    private Point addFinishBufferToPoint(Point fp){
        double x = fp.getX()*App.width;
        double y = fp.getY()*App.height;

        if(fp.getX() == 0){
            x -= FINISH_BUFFER;
        }else if(fp.getX() == 1){
            x += FINISH_BUFFER;
        }

        if(fp.getY() == 0){
            y -= FINISH_BUFFER;
        }else if(fp.getY() == 1){
            y += FINISH_BUFFER;
        }
        return new Point(x, y);
    }

    public int getID(){
        return ID;
    }

    public Point getFinshPoint(){
        return finishPoint;
    }
}
