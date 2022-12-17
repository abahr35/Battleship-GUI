import java.util.ArrayList;
import java.util.List;
//Austin Bahr
//Main class where the AI inherits from
public class BattleshipAI {
    protected TileSet playerGrid;

    //Moves that the AI can take
    protected List<Point> validMoves;


    //  Creates the basic setup for the AI by setting up references to the player's grid,
    //  and creates a list of all valid moves.
    public BattleshipAI(TileSet playerGrid) {
        this.playerGrid = playerGrid;
        createValidMoveList();
    }

    //returns a move that the AI selects
    //Overridden to increase difficulty
    public Point selectMove() {
        return Point.ZERO;
    }

    //Recreates the move list when restarting the game
    public void reset() {
        createValidMoveList();
    }

//  Creates the move list for the Ai
    private void createValidMoveList() {
        validMoves = new ArrayList<>();
        for(int x = 0; x < TileSet.GRID_WIDTH; x++) {
            for(int y = 0; y < TileSet.GRID_HEIGHT; y++) {
                validMoves.add(new Point(x,y));
            }
        }
    }
}
