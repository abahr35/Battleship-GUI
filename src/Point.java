//Austin Bahr
//class similar to Java.awt.Point and Vector3 from Unity
public class Point {

    public static final Point DOWN = new Point(0,1);
    public static final Point UP = new Point(0,-1);

    public static final Point LEFT = new Point(-1,0);

    public static final Point RIGHT = new Point(1,0);
    public static final Point ZERO = new Point(0,0);

    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //new position
    public Point(Point newPoint) {
        this.x = newPoint.x;
        this.y = newPoint.y;
    }

    //update current position
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //add points to current position
    public void add(Point otherPosition) {
        this.x += otherPosition.x;
        this.y += otherPosition.y;
    }


    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
