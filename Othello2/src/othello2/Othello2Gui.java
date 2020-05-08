package othello2;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Othello2Gui extends JFrame
{
    private JPanel contentPane;
    protected JButton[] fields = new JButton[64];
    private List<JButton> tokensToFlip = new ArrayList<>();
    private Color turn = Color.BLACK;
    Scoreboard score = new Scoreboard();
    private int size = 8;
    private boolean viableOption = false;
    JLabel lblBlkScore;
    JLabel lblWhtScore;

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    Othello2Gui frame = new Othello2Gui();
                    frame.setVisible(true);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame. !!! GUI Constructor !!!
     */
    public Othello2Gui()
    {
        setTitle("Othello");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 842, 665);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Create labels to keep track of scores
        JPanel scoreArea = createScoreArea();
        contentPane.add(scoreArea, BorderLayout.NORTH);

        JPanel board = createBoard();

        // Add all buttons to panel
        contentPane.add(board, BorderLayout.CENTER);
    }

    private JPanel createBoard()
    {
        // Create the board and add buttons
        JPanel board = new JPanel();
        board.setBackground(Color.DARK_GRAY);
        board.setLayout(new GridLayout(8, 8, 5, 5));
        for (int i = 0; i < fields.length; i++)
        {
            JButton button = new JButton(String.valueOf(i));
            fields[i] = button;
            button.setBackground(Color.GREEN);
            board.add(button);
        }

        setStartingTiles();

        // Set up the EventHandler and ActionListener
        EventHandler eventHandler = new EventHandler();
        for (int i = 0; i < fields.length; i++)
        {
            fields[i].setFont(new Font("Tahoma", Font.PLAIN, 26));
            fields[i].addActionListener(eventHandler);
        }

        return board;
    }

    private void setStartingTiles()
    {
        fields[27].setBackground(Color.WHITE);
        fields[28].setBackground(Color.BLACK);
        fields[35].setBackground(Color.BLACK);
        fields[36].setBackground(Color.WHITE);

        fields[27].setEnabled(false);
        fields[28].setEnabled(false);
        fields[35].setEnabled(false);
        fields[36].setEnabled(false);

        //set the labels of the number of tokens on the board
        lblBlkScore.setText(Integer.toString(score.blackTokenCount(fields)));
        lblWhtScore.setText(Integer.toString(score.whiteTokenCount(fields)));
    }

    private JPanel createScoreArea()
    {
        JPanel scoreArea = new JPanel();
        JLabel lblScores = new JLabel("Scores::");
        lblScores.setFont(new Font("Tahoma", Font.PLAIN, 30));
        scoreArea.add(lblScores);

        Component horizontalStrut = Box.createHorizontalStrut(40);
        scoreArea.add(horizontalStrut);

        JLabel lblWhite = new JLabel("White: ");
        lblWhite.setFont(new Font("Tahoma", Font.PLAIN, 30));
        scoreArea.add(lblWhite);


        lblWhtScore = new JLabel("2");
        lblWhtScore.setFont(new Font("Tahoma", Font.PLAIN, 30));
        scoreArea.add(lblWhtScore);

        Component horizontalStrut1 = Box.createHorizontalStrut(20);
        scoreArea.add(horizontalStrut1);

        JLabel lblBlack = new JLabel("Black: ");
        lblBlack.setFont(new Font("Tahoma", Font.PLAIN, 30));
        scoreArea.add(lblBlack);

        lblBlkScore = new JLabel("2");
        lblBlkScore.setFont(new Font("Tahoma", Font.PLAIN, 30));
        scoreArea.add(lblBlkScore);

        return scoreArea;
    }

    private class EventHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            // Find out which button triggered event
            JButton button = (JButton) e.getSource();

                                // TODO - DELETE ME AFTER DEBUGGING
                                for (JButton button2 : tokensToFlip)
                                {System.out.print(button2.getText().toString() + " "); }
                                System.out.println("\n");

            checkSurroundingSpaces(button);

            //Once we've checked the surrounding spaces, we need to know if any tokens were flipped.  If even 1 was, we
            // know it was a valid space.  Add the token for the color that we currently are.
            if(viableOption) {
                button.setBackground(turn);  //We only want to set the color if the space was valid.
                button.setEnabled(false);
                //Reset things for next turn
                switchTurn();
                System.out.println("Current turn : " + turn.toString());
                viableOption = false;
            }
        }

        private void checkSurroundingSpaces(JButton button)
        {
            int north = -8;
            int northEast = -7;
            int east = 1;
            int southEast = 9;
            int south = 8;
            int southWest = 7;
            int west = -1;
            int northWest = -9;

            int[] directionalDifferences = {north, northEast, east, southEast, south, southWest, west, northWest};

            for(int direction : directionalDifferences) {
                System.out.println("Current direction is : " + direction);
                tokensToFlip.add(button);
                checkDirection(button, direction);
                tokensToFlip.clear();
            }
        }

        private void checkDirection(JButton button, int indexDiffByDirection)
        {
            int indexOfNewSpace = Integer.parseInt(button.getText()) + indexDiffByDirection;

            if (!isValidSpaceByIndex(indexOfNewSpace))
            {
                return;
            }
            while ((fields[indexOfNewSpace].getBackground() != turn) && (fields[indexOfNewSpace].getBackground() != Color.GREEN))
            {
                //Make sure the new space does not fall off the board grid.
                if(isValidSpaceByIndex(indexOfNewSpace)) {
                    //Check that the button represented by that index has a color opposite of the turn (i.e. The
                    //background color cannot = turn's color and it cannot be green)
                    if((fields[indexOfNewSpace].getBackground() != turn) && (fields[indexOfNewSpace].getBackground() != Color.GREEN)) {
                        tokensToFlip.add(fields[indexOfNewSpace]);
                    }

                    int futureIndex = indexOfNewSpace + indexDiffByDirection;
                    if(futureIndex >= 0 && futureIndex <= fields.length) {
                        indexOfNewSpace += indexDiffByDirection; //Update the index again to get the next space above.
                    } else  {
                        tokensToFlip.clear();
                        break;
                    }

                } else {
                    System.out.println(fields[indexOfNewSpace] + "is off the board");
                }
            }

            /*
            Because we finished the while loop, we know that we hit either an empty square or we hit a square the same color as turn.
            //If final square was the same color as us, flip over all the tiles in the list to the same color as turn.  Then clear the
            list, EXCEPT for index 0 which has our starting position.
            */
            if(fields[indexOfNewSpace].getBackground() == turn  && tokensToFlip.size() > 1) {
                flipTokens(tokensToFlip);
                viableOption = true;
            }

            //set the labels of the number of tokens on the board
            lblBlkScore.setText(Integer.toString(score.blackTokenCount(fields)));
            lblWhtScore.setText(Integer.toString(score.whiteTokenCount(fields)));
        }

        private boolean isValidSpaceByIndex(int index)
        {
            if (index >= 0 && index <= 63) {
                return true;
            } else {
                System.out.println("The new space (by index) is invalid");
                return false;
            }
        }

        private void flipTokens(List<JButton> tokensToFlip)
        {
            //Iterate through the list.  Turn the tokens to the color whose turn it is.
            for(JButton button : tokensToFlip) {
                button.setBackground(turn);
            }
        }

        private void switchTurn()
        {
            if (turn == Color.BLACK)
            {
                turn = Color.WHITE;
            } else
            {
                turn = Color.BLACK;
            }
        }

    } // End of EventHandler Class

} // -------------------- End of Othello GUI class --------------------
