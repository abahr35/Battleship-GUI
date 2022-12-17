import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//Austin Bahr

// Logic class organizes the program
public class Logic implements KeyListener {

    public static void main(String[] args) {
        Logic game = new Logic();
    }

    private final Board Board;


    public Logic() {

        //Warning Message
        String warning = "Controls: \n Restart: Q\n Rotate: R\n Debug: D";
        JOptionPane.showMessageDialog(null, warning);

        //AI dialog
        String message = "Would you like player 2 to be AI?";
        String[] aiOption = new String[] {"Yes", "No"};
        var aiChoice = JOptionPane.showOptionDialog(null, message, "Confirm AI Usage", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, aiOption, aiOption[0]);

        JFrame frame = new JFrame("Battleship - Austin Bahr");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);


        Board = new Board(aiChoice);
        frame.getContentPane().add(Board);

        frame.addKeyListener(this);
        frame.pack();
        frame.setVisible(true);
    }


     // Called when the key is pressed down. Passes the key press on to the BoardGamePanel
    @Override
    public void keyPressed(KeyEvent e) {
        Board.InputHandler(e.getKeyCode()); //send key to the board for better handling
    }
    // Not used
    @Override
    public void keyTyped(KeyEvent e) {}
    // Not used
    @Override
    public void keyReleased(KeyEvent e) {}

}
