import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//Austin Bahr

//refactored class to use mouse, initially used keyboard, but I had issues with game state switching
// I also decided to refactor because I was extremely disorganized. I pulled a lot of methods I created
// from the last edition to here. I also did mor research on awt and swing to see how graphics can be shown
// boardGamePanel where the board is created and holds gameState, two grids, and a game status panel
public class Board extends JPanel implements MouseListener, MouseMotionListener {

    int leaveGameKey = KeyEvent.VK_ESCAPE;
    int restartGameKey = KeyEvent.VK_Q;
    int rotateShipKey = KeyEvent.VK_R;
    int debugKey = KeyEvent.VK_D;

    public enum GameState {
        PlacementP1, //ship placement
        PlacementP2, //ship placement
        AttackP1, //p1 can attack
        AttackP2, //p2 can attack
        AttackAI,
        GameOver //game ends based on win condition
    }


    // Reference to the status panel to pass text messages to show what is happening.
    //player 1
    private final StatusPanel statusPanelP1;
    //player2
    private final StatusPanel statusPanelP2;

    // The player 2 grid for the player to attack.
    private final TileSet player2;

    // The player's grid for the computer to attack.
    private final TileSet player1;

    //main AI control
    private final BattleshipAI aiController;

    //temp ship that is shown before placement
    private Ship placingShipP1;
    private Ship placingShipP2;

    //temp ship coordinates
    private Point tempPlacingPosition;

    //ship index
    private int placingShipIndexP1;
    private int placingShipIndexP2;

    //gamestate
    private GameState gameState;

    //shows computer board for grading
    public static boolean debugEnabled;

    public Board(int aiChoice) {


        int boardGap = 50;
        player2 = new TileSet(0, 0);// change to a player controlled board if possible. p2 is on left
        player1 = new TileSet(player2.getWidth() + boardGap, 0); //second board separated by 50 pixels horizontally. p1 is on right
        setBackground(new Color(0, 255, 244)); //water color
        setPreferredSize(new Dimension(player1.getWidth() + player1.getPosition().x, player2.getHeight() + 50)); //this designs the board
        addMouseListener(this);
        addMouseMotionListener(this);


        if (aiChoice == 0) aiController = new EasyAI(player2); //if player 2 is AI
        else if (aiChoice == 1) { //if p2 is a player
            aiController = null;
        } else aiController = new EasyAI(player2);
        statusPanelP1 = new StatusPanel(new Point(player1.getWidth() + boardGap, player2.getHeight() + 1), player1.getWidth() + 1, 49);
        statusPanelP2 = new StatusPanel(new Point(0, player1.getHeight() + 1), player2.getWidth() + 1, 49);

        resetGame(); //starts game loop
    }


    // Draws the grids and text panel
    public void paint(Graphics g) { //override paint class
        super.paint(g);
        player2.Paint(g);
        player1.Paint(g);
        if (gameState == GameState.PlacementP1) {
            placingShipP1.Draw(g);
        } else if (gameState == GameState.PlacementP2) {
            placingShipP2.Draw(g);
        }
        statusPanelP1.paint(g);
        statusPanelP2.paint(g);
    }

    //catches the input set from the Logic Class
    public void InputHandler(int keyCode) {
        if (keyCode == leaveGameKey) { //leaves the application
            System.exit(1);
        } else if (keyCode == restartGameKey) { //restart
            resetGame();
        } else if (gameState == GameState.PlacementP1 && keyCode == rotateShipKey) { //rotate ship
            placingShipP1.toggleSideways();
            updateShipPlacement(tempPlacingPosition);
        } else if (keyCode == debugKey) { //show computer board
            debugEnabled = !debugEnabled;
        }
        repaint();
    }


    // allows restart of the game
    public void resetGame() {
        player2.reset();
        player1.reset();
        statusPanelP2.reset();
        statusPanelP1.reset();

        // Player can see their own ships by default
        player1.setShowShips(true);

        tempPlacingPosition = new Point(0, 0);
        placingShipP1 = new Ship(new Point(0, 0), new Point(player1.getPosition().x, player1.getPosition().y), TileSet.BOAT_SIZES[0], true);
        placingShipIndexP1 = 0;
        updateShipPlacement(tempPlacingPosition);


        if (aiController != null) {  // if ai = true

            aiController.reset();
            player2.populateShips();
        }

        debugEnabled = false;
        statusPanelP1.reset();
        gameState = GameState.PlacementP1;
    }


    //mouse position to test update the ship being placed during PlacingShip state.
    //ship placed by calling placeShip().
    private void tryPlaceShip(Point mousePosition) {
        Point targetPosition = new Point(0, 0);
        if (gameState == GameState.PlacementP1) {
            targetPosition = player1.getPositionInGrid(mousePosition.x, mousePosition.y);
            updateShipPlacement(targetPosition);
            if (player1.canPlaceShipAt(targetPosition.x, targetPosition.y, TileSet.BOAT_SIZES[placingShipIndexP1], placingShipP1.isSideways())) {
                placeShip(targetPosition);
                System.out.println(targetPosition);
            }
        }
        targetPosition = new Point(0,0);
        if (gameState == GameState.PlacementP2) {
          targetPosition = player2.getPositionInGrid(mousePosition.x, mousePosition.y);
            updateShipPlacement(targetPosition);
            if (player2.canPlaceShipAt(targetPosition.x, targetPosition.y, TileSet.BOAT_SIZES[placingShipIndexP2], placingShipP2.isSideways())) {
                placeShip(targetPosition);
                System.out.println(targetPosition);
            }
        }

    }

    //store ship in players grid
    //when player has a full grid then switch to next state depending on AI or Player
    private void placeShip(Point targetPosition) {
        if (gameState == GameState.PlacementP1) {
            placingShipP1.setShipPlacementColor(Ship.ShipPlacementState.Placed);
            player1.placeShip(placingShipP1, tempPlacingPosition.x, tempPlacingPosition.y);
            placingShipIndexP1++;

            // If there are still ships to place
            if (placingShipIndexP1 < TileSet.BOAT_SIZES.length) { //if less than max ship amount
                placingShipP1 = new Ship(new Point(targetPosition.x, targetPosition.y), new Point(player1.getPosition().x + targetPosition.x * TileSet.Tile_Size, player1.getPosition().y + targetPosition.y * TileSet.Tile_Size), TileSet.BOAT_SIZES[placingShipIndexP1], true);
                updateShipPlacement(tempPlacingPosition); //update the placement of the ship

            }else if (aiController != null) {
                    statusPanelP2.setTopLine("AI's Board");
                    statusPanelP2.setBottomLine("Press D for Debug mode!");
                    gameState = GameState.AttackAI;
                    doAITurn();
            }
            else {

                gameState = GameState.PlacementP2;
                statusPanelP1.setTopLine("Player 2's Turn");
                statusPanelP1.setBottomLine("Please Wait!");
                statusPanelP2.reset();
                gameState = GameState.PlacementP2;
                tempPlacingPosition = new Point(0, 0);
                placingShipIndexP2 = 0;
                player1.setShowShips(false);
                player2.setShowShips(true);
                placingShipP2 = new Ship(new Point(0, 0), new Point(player2.getPosition().x, player2.getPosition().y), TileSet.BOAT_SIZES[0], true);

            }

        }else if (gameState == GameState.PlacementP2) { //when it is p2 then place their ships
            placingShipP2.setShipPlacementColor(Ship.ShipPlacementState.Placed);
            player2.placeShip(placingShipP2, tempPlacingPosition.x, tempPlacingPosition.y);
            placingShipIndexP2++;
            if (placingShipIndexP2 < TileSet.BOAT_SIZES.length) {
                placingShipP2 = new Ship(new Point(targetPosition.x, targetPosition.y), new Point(player2.getPosition().x + targetPosition.x * TileSet.Tile_Size, player2.getPosition().y + targetPosition.y * TileSet.Tile_Size), TileSet.BOAT_SIZES[placingShipIndexP2], true);
                updateShipPlacement(tempPlacingPosition);
            } else {
                gameState = GameState.AttackP1;
                player1.setShowShips(false);
                player2.setShowShips(false);
                statusPanelP1.setTopLine("Attack Player 2's Board");
                statusPanelP1.setBottomLine("Press D for Debug mode!");
                statusPanelP2.reset();
                statusPanelP2.setTopLine("");
                statusPanelP2.setBottomLine("Press D for Debug mode!");
            }
        }
    }

    //shoot at computer board and take turns with AI
    //end game when no ships exist
    private void tryFireAtComputer(Point mousePosition) {
        Point targetPosition = player2.getPositionInGrid(mousePosition.x,mousePosition.y);
        // Ignore if position was already clicked
        if(!player2.isPositionMarked(targetPosition)) {
            doPlayerTurn(targetPosition);
            // Only do the AI turn if the game didn't end from the player's turn.
            if(!player2.areAllShipsDestroyed()) {
                doAITurn();
            }
        }
    }

    //shoot at player board and take turns with other player
    //end game when no ships exist for one player
    private void tryFireAtPlayer(Point mousePosition){
        Point targetPosition;
        if (gameState == GameState.AttackP1){ //player1
            targetPosition = player2.getPositionInGrid(mousePosition.x, mousePosition.y);
            // Ignore if already clicked
            if(!player2.isPositionMarked(targetPosition)) {
                doPlayerTurn(targetPosition);
                // Only do the turn if the game didn't end from the player's turn.
                if (!player2.areAllShipsDestroyed()) {
                    gameState = GameState.AttackP2;
                    player1.setShowShips(false);
                }
            }
        } else if(gameState == GameState.AttackP2){ //player 2
            targetPosition = player1.getPositionInGrid(mousePosition.x, mousePosition.y);
            // Ignore if already clicked
            if(!player1.isPositionMarked(targetPosition)) {
                doPlayerTurn(targetPosition);
                // Only do the turn if the game didn't end from the player's turn.
                if (!player1.areAllShipsDestroyed()) {
                    gameState = GameState.AttackP1;
                    player2.setShowShips(false);
                }
            }
        }
    }

    //place player attack, show if it hit, then check for win state
    private void doPlayerTurn(Point targetPosition) {
        if (gameState == GameState.AttackP1){
            boolean hit = player2.markPosition(targetPosition);
            String hitMiss = hit ? "Hit" : "Missed";
            String destroyed = "";
            if(hit && player2.getStateAtPosition(targetPosition).getAssociatedShip().isDestroyed()) {
                destroyed = " Ship Destroyed!";
            }

            statusPanelP1.setTopLine("Player 1: " + hitMiss + " " + targetPosition + destroyed);
            if(player2.areAllShipsDestroyed()) {
                // Player wins!
                gameState = GameState.GameOver;
                statusPanelP1.showGameOver(true);
                statusPanelP2.showGameOver(false);
            }

        }else if (gameState == GameState.AttackP2){
            boolean hit = player1.markPosition(targetPosition);
            String hitMiss = hit ? "Hit" : "Missed";
            String destroyed = "";
            if(hit && player1.getStateAtPosition(targetPosition).getAssociatedShip().isDestroyed()) {
                destroyed = " Ship Destroyed!";
            }

            statusPanelP2.setTopLine("Player 2: " + hitMiss + " " + targetPosition + destroyed);
            if(player1.areAllShipsDestroyed()) {
                // Player wins!
                gameState = GameState.GameOver;
                statusPanelP1.showGameOver(true);
                statusPanelP2.showGameOver(false);
            }
        } else if (gameState == GameState.AttackAI) {
            boolean hit = player2.markPosition(targetPosition);
            String hitMiss = hit ? "Hit" : "Missed";
            String destroyed = "";
            if(hit && player2.getStateAtPosition(targetPosition).getAssociatedShip().isDestroyed()) {
                destroyed = " Ship Destroyed!";
            }

            statusPanelP1.setTopLine("Player 1: " + hitMiss + " " + targetPosition + destroyed);
            if(player2.areAllShipsDestroyed()) {
                // Player wins!
                gameState = GameState.GameOver;
                statusPanelP1.showGameOver(true);
                statusPanelP2.showGameOver(false);
            }
        }
    }

    //AI selecting an attack and display it
    private void doAITurn() {
        Point aiMove = aiController.selectMove();
        boolean hit = player1.markPosition(aiMove);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if(hit && player1.getStateAtPosition(aiMove).getAssociatedShip().isDestroyed()) {
            destroyed = " Ship Destroyed!";
        }

        statusPanelP1.setBottomLine("Computer " + hitMiss + " " + aiMove + destroyed);
        if(player1.areAllShipsDestroyed()) { //if all player ships are gone then computer wins
            gameState = GameState.GameOver;
            statusPanelP1.showGameOver(false);
        }
    }

    //update ship place location
    private void tryMovePlacingShip(Point mousePosition) {
        if (gameState == GameState.PlacementP1) {
            if (player1.isPositionInside(mousePosition)) {
                Point targetPos = player1.getPositionInGrid(mousePosition.x, mousePosition.y);
                updateShipPlacement(targetPos);
            }
        }
        if (gameState == GameState.PlacementP2) {
            if (player2.isPositionInside(mousePosition)) {
                Point targetPos = player2.getPositionInGrid(mousePosition.x, mousePosition.y);
                updateShipPlacement(targetPos);
            }
        }
    }

    //ship bounds check
    private void updateShipPlacement(Point targetPos) {
        // Constrain to fit inside the grid

        if (gameState == GameState.PlacementP1) {
            if (placingShipP1.isSideways()) {
                targetPos.x = Math.min(targetPos.x, TileSet.GRID_WIDTH - TileSet.BOAT_SIZES[placingShipIndexP1]);
            } else {
                targetPos.y = Math.min(targetPos.y, TileSet.GRID_HEIGHT - TileSet.BOAT_SIZES[placingShipIndexP1]);
            }
            // Update drawing position to use the new target position
            placingShipP1.setDrawPosition(new Point(targetPos), new Point(player1.getPosition().x + targetPos.x * TileSet.Tile_Size, player1.getPosition().y + targetPos.y * TileSet.Tile_Size));
            // Store the grid position for other testing cases
            tempPlacingPosition = targetPos;
            // Change the color of the ship based on whether it could be placed at the current location.
            if (player1.canPlaceShipAt(tempPlacingPosition.x, tempPlacingPosition.y, TileSet.BOAT_SIZES[placingShipIndexP1], placingShipP1.isSideways())) {
                placingShipP1.setShipPlacementColor(Ship.ShipPlacementState.Valid);
            } else {
                placingShipP1.setShipPlacementColor(Ship.ShipPlacementState.Invalid);
            }
        }
        if (gameState == GameState.PlacementP2){
            if (placingShipP2.isSideways()) {
                targetPos.x = Math.min(targetPos.x, TileSet.GRID_WIDTH - TileSet.BOAT_SIZES[placingShipIndexP2]);
            } else {
                targetPos.y = Math.min(targetPos.y, TileSet.GRID_HEIGHT - TileSet.BOAT_SIZES[placingShipIndexP2]);
            }
            // Update drawing position to use the new target position
            placingShipP2.setDrawPosition(new Point(targetPos), new Point(player2.getPosition().x + targetPos.x * TileSet.Tile_Size, player2.getPosition().y + targetPos.y * TileSet.Tile_Size));
            // Store the grid position for other testing cases
            tempPlacingPosition = targetPos;
            // Change the color of the ship based on whether it could be placed at the current location.
            if (player2.canPlaceShipAt(tempPlacingPosition.x, tempPlacingPosition.y, TileSet.BOAT_SIZES[placingShipIndexP2], placingShipP2.isSideways())) {
                placingShipP2.setShipPlacementColor(Ship.ShipPlacementState.Valid);
            } else {
                placingShipP2.setShipPlacementColor(Ship.ShipPlacementState.Invalid);
            }
        }
    }


    //when mouse is released then get position on the grid
    @Override
    public void mouseReleased(MouseEvent e) {
        Point mousePosition = new Point(e.getX(), e.getY());
        if(gameState == GameState.PlacementP1 && player1.isPositionInside(mousePosition)) {
            tryPlaceShip(mousePosition);//if in correct board place ship
        } else if (gameState == GameState.PlacementP2 && player2.isPositionInside(mousePosition)) {
            tryPlaceShip(mousePosition);//if in correct board
        } else if(gameState == GameState.AttackP1 && player2.isPositionInside(mousePosition)) {
            tryFireAtPlayer(mousePosition);
        } else if (gameState == GameState.AttackP2 && player1.isPositionInside(mousePosition)) {
            tryFireAtPlayer(mousePosition);
        } else if(gameState == GameState.AttackAI && player2.isPositionInside(mousePosition)){
            tryFireAtComputer(mousePosition);
        }
        repaint();
    }

    // mouse movement when in placement
    @Override
    public void mouseMoved(MouseEvent e) {
        if(gameState != GameState.PlacementP1 && gameState != GameState.PlacementP2) {
            return;
        }
        tryMovePlacingShip(new Point(e.getX(), e.getY()));
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
}
