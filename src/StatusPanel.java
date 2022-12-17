import java.awt.*;
//Austin Bahr
//Text Panel for each player
public class StatusPanel extends Tile {

    private final static Font font = new Font("LCD", Font.BOLD, 20);

    private final static String shipPlacementLine1 = "Place your Ships Above!";

    private final static String shipPlacementLine2 = "Press R to rotate.";

    private final static String gameOverLossLine = "Game Over! You Lose!";

    private final static String gameOverWinLine = "You won! Good Job";

    private final static String gameOverBottomLine = "Press Q to restart.";

    //can be called to display custom Messages
    private String topLine;

    //can be called to display custom Messages
    private String bottomLine;

    //sets up panel for use
    public StatusPanel(Point position, int width, int height) {
        super(position, width, height);
        reset();
    }

    //resets the text to normal
    public void reset() {
        topLine = shipPlacementLine1;
        bottomLine = shipPlacementLine2;
    }

    //sets status to game over condition :(
    public void showGameOver(boolean playerWon) {
        topLine = (playerWon) ? gameOverWinLine : gameOverLossLine;
        bottomLine = gameOverBottomLine;
    }

    //edits top line
    public void setTopLine(String message) {
        topLine = message;
    }

    //edits bottom line
    public void setBottomLine(String message) {
        bottomLine = message;
    }

    //draws background and centers text
    public void paint(Graphics g) {

        g.setColor(new Color(2, 24, 37, 197)); //trying to emulate a digital screen
        g.fillRoundRect(position.x, position.y, width, height, 25, 25);
        g.setColor(Color.BLACK);
        g.drawRoundRect(position.x, position.y, width , height,25, 25);
        g.setColor(Color.BLACK);
        g.setFont(font);


        //gets the actual length in pixels for text
        int strWidth = g.getFontMetrics().stringWidth(topLine);
        g.drawString(topLine, position.x+width/2-strWidth/2, position.y+20);
        //gets the actual length in pixels for text
        strWidth = g.getFontMetrics().stringWidth(bottomLine);
        g.drawString(bottomLine, position.x+width/2-strWidth/2, position.y+40);
    }
}
