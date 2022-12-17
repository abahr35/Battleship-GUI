import java.awt.*;
//Austin Bahr
//change color based on state of the tile
//inherited from Tile
public class TileState extends Tile {
    private final Color HIT_COLOR = new Color(255, 21, 21, 180);
    private final Color MISS_COLOR = new Color(255, 255, 255, 255);
    private final int PADDING = 3;

    //show when painted
    private boolean showMarker;

    //when ship is in tile then uses hit color but if null then uses Miss
    private Ship shipAtMarker;


    //creates this with Tile,= position, and size
    public TileState(int x, int y, int width, int height) {
        super(x, y, width, height);
        reset();
    }

    //reset for use in restart
    public void reset() {
        shipAtMarker = null;
        showMarker = false;
    }

    //shows mark when other sections are destroyed
    public void PreemptiveMark() {
        if(!showMarker && isShip()) {
            shipAtMarker.destroySection();
        }
        showMarker = true;
    }

    //return if its marked
    public boolean isMarked() {
        return showMarker;
    }

    //set state to contain a ship
    public void setAsShip(Ship ship) {
        this.shipAtMarker = ship;
    }

    //return if tile has ship
    public boolean isShip() {
        return shipAtMarker != null;
    }

    //return type of ship
    public Ship getAssociatedShip() {
        return shipAtMarker;
    }

    //draw rectangle in square if contains ship
    public void paint(Graphics g) {
        if(!showMarker) return;

        g.setColor(isShip() ? HIT_COLOR : MISS_COLOR);
        g.fillRect(position.x+PADDING+1, position.y+PADDING+1, width-PADDING*2, height-PADDING*2);
    }
}
