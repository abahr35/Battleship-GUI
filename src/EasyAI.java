import java.util.Collections;
//Austin Bahr
//todo harder AI and selection before playing
public class EasyAI extends BattleshipAI{
    //easy is just random at picking. Improve by creating another AI class to be smarter
    //randomize moves using collections.shuffle
    public EasyAI(TileSet playerGrid) {
        super(playerGrid);
        Collections.shuffle(validMoves);
    }

    //reset by randomizing moves again
    @Override
    public void reset() {
        super.reset();
        Collections.shuffle(validMoves);
    }

    //moves based of the top of the List and plays it
    //randomly placed
    @Override
    public Point selectMove() {
        Point nextMove = validMoves.get(0);
        validMoves.remove(0);
        return nextMove;
    }
}
