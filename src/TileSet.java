import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//Austin Bahr
public class TileSet extends Tile {

    //size of Tile
    public static final int Tile_Size = 60;

    //grid size on left right axis
    public static final int GRID_WIDTH = 10;

    //grid size on up down axis
    public static final int GRID_HEIGHT = 10;

    //ship size definition
    public static final int[] BOAT_SIZES = {5,4,3,3,2};

    //visual TileState Grid
    private final TileState[][] states = new TileState[GRID_WIDTH][GRID_HEIGHT];

    //ship list
    private List<Ship> ships;

    //used for random ship placement
    private Random random;

    //hide or show ships for debug mode
    private boolean showShips;

    //end game condition
    private boolean allShipsDestroyed;


    //create a default setup for a tileset
    public TileSet(int x, int y) {
        super(x, y, Tile_Size *GRID_WIDTH, Tile_Size *GRID_HEIGHT);
        createStateGrid();
        ships = new ArrayList<>();
        random = new Random();
        showShips = false;
    }

    //draws the actual ship on the board
    public void Paint(Graphics g) {
        for(Ship ship : ships) {
            if(showShips || Board.debugEnabled || ship.isDestroyed()) {
                ship.Draw(g);
            }
        }
        drawStates(g);
        drawGrid(g);
    }

    //true if paints the ships of specified board
    public void setShowShips(boolean showShips) {
        this.showShips = showShips;
    }

    //resets the board
    public void reset() {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for(int y = 0; y < GRID_HEIGHT; y++) {
                states[x][y].reset();
            }
        }
        ships.clear();
        showShips = false;
        allShipsDestroyed = false;
    }

    //check if ships have been destroyed
    public boolean markPosition(Point posToMark) {
        states[posToMark.x][posToMark.y].PreemptiveMark();

        allShipsDestroyed = true;
        for(Ship ship : ships) {
            if(!ship.isDestroyed()) {
                allShipsDestroyed = false;
                break;
            }
        }
        return states[posToMark.x][posToMark.y].isShip();
    }

    //end game check
    public boolean areAllShipsDestroyed() {
        return allShipsDestroyed;
    }

    //check if state already marked
    public boolean isPositionMarked(Point posToTest) {
        return states[posToTest.x][posToTest.y].isMarked();
    }

    //gets the state at the current position
    public TileState getStateAtPosition(Point posToSelect) {
        return states[posToSelect.x][posToSelect.y];
    }

    //get mouse position inside the grid
    public Point getPositionInGrid(int mouseX, int mouseY) {
        if(!isPositionInside(new Point(mouseX,mouseY))) return new Point(-1,-1);

        return new Point((mouseX - position.x)/ Tile_Size, (mouseY - position.y)/ Tile_Size);
    }

    //tests if ship can be placed in the grid
    public boolean canPlaceShipAt(int gridX, int gridY, int segments, boolean sideways) {
        if(gridX < 0 || gridY < 0) return false;

        if(sideways) { // handle the case when horizontal
            if(gridY > GRID_HEIGHT || gridX + segments > GRID_WIDTH) return false;
            for(int x = 0; x < segments; x++) {
                if(states[gridX+x][gridY].isShip()) return false;
            }

        } else { // handle the case when vertical
            if(gridY + segments > GRID_HEIGHT || gridX > GRID_WIDTH) return false;
            for(int y = 0; y < segments; y++) {
                if(states[gridX][gridY+y].isShip()) return false;
            }
        }
        return true;
    }

    //draws a grid with black lines on top of teh current grid
    private void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        // Draw vertical lines
        int y2 = position.y;
        int y1 = position.y+height;
        for(int x = 0; x <= GRID_WIDTH; x++)
            g.drawLine(position.x+x * Tile_Size, y1, position.x+x * Tile_Size, y2);

        // Draw horizontal lines
        int x2 = position.x;
        int x1 = position.x+width;
        for(int y = 0; y <= GRID_HEIGHT; y++)
            g.drawLine(x1, position.y+y * Tile_Size, x2, position.y+y * Tile_Size);
    }

    //draws the states, states determine the worthiness of getting drawn
    private void drawStates(Graphics g) {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for(int y = 0; y < GRID_HEIGHT; y++) {
                states[x][y].paint(g);
            }
        }
    }

    //creates all the states
    private void createStateGrid() {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                states[x][y] = new TileState(position.x+x* Tile_Size, position.y + y* Tile_Size, Tile_Size, Tile_Size);
            }
        }
    }

    //use for AI, places ships automatically and randomly
    public void populateShips() {
        ships.clear();
        for(int i = 0; i < BOAT_SIZES.length; i++) {
            boolean sideways = random.nextBoolean();
            int gridX,gridY;
            do {
                gridX = random.nextInt(sideways?GRID_WIDTH-BOAT_SIZES[i]:GRID_WIDTH);
                gridY = random.nextInt(sideways?GRID_HEIGHT:GRID_HEIGHT-BOAT_SIZES[i]);
            } while(!canPlaceShipAt(gridX,gridY,BOAT_SIZES[i],sideways));

            placeShip(gridX, gridY, BOAT_SIZES[i], sideways);
        }
    }

    //place ship, assumes pre-checks have been made...
    public void placeShip(int gridX, int gridY, int segments, boolean sideways) {
        placeShip(new Ship(new Point(gridX, gridY), new Point(position.x+gridX* Tile_Size, position.y+gridY* Tile_Size), segments, sideways), gridX, gridY);
    }

    //place ship, assumes pre-checks have been made...
    public void placeShip(Ship ship, int gridX, int gridY) {
        ships.add(ship);
        if(ship.isSideways()) { // If the ship is horizontal
            for(int x = 0; x < ship.getSegments(); x++) {
                states[gridX+x][gridY].setAsShip(ships.get(ships.size()-1));
            }
        } else { // If the ship is vertical
            for(int y = 0; y < ship.getSegments(); y++) {
                states[gridX][gridY+y].setAsShip(ships.get(ships.size()-1));
            }
        }
    }
}
