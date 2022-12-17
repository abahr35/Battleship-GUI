//Austin Bahr
//used for holding info on the Tiles
//extended to TileState, TileSet, StatusPanel
public class Tile {

    protected Point position;

    protected int width;

    protected int height;

    public Tile(Point position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public Tile(int x, int y, int width, int height) {
        this(new Point(x,y),width,height);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    //gets the location of the top left of the rectangle
    public Point getPosition() {
        return position;
    }

    //checks for target position in the Tile
    public boolean isPositionInside(Point targetPosition) {
        return targetPosition.x >= position.x && targetPosition.y >= position.y
                && targetPosition.x < position.x + width && targetPosition.y < position.y + height;
    }
}
