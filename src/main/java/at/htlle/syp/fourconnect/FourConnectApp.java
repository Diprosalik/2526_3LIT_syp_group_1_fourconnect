package at.htlle.syp.fourconnect;

import java.util.Arrays;

/**
 * (c) Helmut Steineder
 */
public class FourConnectApp {
    public static final int ROWS = 7;
    public static final int COLUMNS = 7;
    static final String BASELINE = "---------------";

    private boolean isRedTurn;
    private char[][] board = new char[ROWS][COLUMNS];

    private ColumnInputInterface columnInputInterface;

    public char[][] getBoard() {
        return board;
    }

    public void setColumnReader(ColumnInputInterface columnInputInterface) {
        this.columnInputInterface = columnInputInterface;
    }
    // We need to initialze the board first
    public void initBoard()
    {
        isRedTurn = true;
        // Let's run thru each lines
        for (int row = 0; row < ROWS; row++) {
            Arrays.fill(board[row], ' ');
        }
    }

    //Yes, we even need to make a new method for visually
    //printing our board, but at least it's not hard to do
    public void printBoard()
    {
        for (int row =0;row<ROWS;row++)
        {
            printRow(board[row]);
        }
        System.out.println(BASELINE);
        System.out.println();
    }

    private void printRow(char[] row)
    {
        StringBuilder rowBuffer = new StringBuilder("|");
        for (int column = 0; column < COLUMNS; column++) {
            rowBuffer.append(row[column]);
            rowBuffer.append("|");
        }
        System.out.println(rowBuffer);
    }

    //Here's are basic move, making the lowest empty row
    //of a specific column have a Red
    public void dropRedCoin()
    {
        //We need to have the user tell us what column he wants
        //to drop a red into
        System.out.println("Drop a red disk at column (0–7): ");

        int column = columnInputInterface.getColumn();

        //Now that we know our column, we have to loop over each row from the bottom to the top
        //till we find the first empty space, drop, and then finish (i.e., break) the move
        for (int row = 0; row < ROWS; row++) {
            if  (board[ROWS-row-1][column] == ' ') {
                board[ROWS-row-1][column] = 'R';
                break;
            }
        }
    }

    //Same as the above step, just yellow
    public void dropYellowCoin()
    {
        System.out.println("Drop a yellow disk at column (0–6): ");
        int column = columnInputInterface.getColumn();

        for (int row = 0; row < ROWS; row++) {
            if  (board[ROWS-row-1][column] == ' ') {
                board[ROWS-row-1][column] = 'Y';
                break;
            }
        }
    }


    //Here's where it gets hard.
    //That's because there are basically four patterns
    //of Reds or Yellows that can win the game
    //One pattern is a horizontal line of four Rs or Ys,
    //another is a vertical line, another is a left-up to right-down
    //diagonal line, and the last is left-down to right-up diagonal,
    //We thus need to code for each type of line
    //and the various places where the line can be
    public Character checkWinner()
    {
        //Time to look for the first type of winning line,
        //a horizontal line
        // Let's loop thru the rows and find potential winners
        for (int row =0;row<ROWS;row++) {
            // we need four coins in a row, hence we count up to COLUMNS - 4
            for (int column = 0; column < COLUMNS - 4; column++) {
                if (board[row][column] != ' '
                        && board[row][column + 1] == board[row][column]
                        && board[row][column + 2] == board[row][column]
                        && board[row][column + 3] == board[row][column])
                    return board[row][column];
            }
        }

        //For a vertical line, let's first loop over each column
        for (int column=0;column<COLUMNS;column++)
        {
            for (int row =0;row<ROWS-3;row++)
            {
                if(board[row][column] != ' '
                        && board[row+1][column] == board[row][column]
                        && board[row+2][column] == board[row][column]
                        && board[row+3][column] == board[row][column])
                    return board[row][column];
            }
        }

        // Now check diagonal line from left down to right
        // We just need to check first 3 rows and first 4 cols
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < COLUMNS-3; column++) {
                if(board[row][column] != ' '
                        && board[row+1][column+1] == board[row][column]
                        && board[row+2][column+2] == board[row][column]
                        && board[row+3][column+3] == board[row][column])
                    return board[row][column];
            }
        }

        // Last but not least diagonal line from left up to right
        // We just need to check first 3 rows and first 4 cols
        for (int row = ROWS-1; row > ROWS-3; row--) {
            for (int column = 0; column < COLUMNS-3; column++) {
                if(board[row][column] != ' '
                        && board[row-1][column+1] == board[row][column]
                        && board[row-2][column+2] == board[row][column]
                        && board[row-3][column+3] == board[row][column])
                    return board[row][column];
            }
        }
        return null;
    }

    //The easy part: using the provided methods
    public static void main (String[] args)
    {
        FourConnectApp app = new FourConnectApp();
        app.setColumnReader(new ColumnInputScanner());
        app.runGame();
    }

    private boolean isRedTurn() {
        return isRedTurn;
    }

    private void switchCoin() {
        isRedTurn = !isRedTurn;
    }

    public Character runGame() {
        // initial phase
        //Time to create the board
        initBoard();

        //Time to make a condition for our game to keep on
        //playing
        boolean loop = true;
        //We need something to keep track of whose turn it is
        int count = 0;

        printBoard();

        Character winner = null;

        // run the game
        while(loop)
        {
            //Let's say that Red gets the first turn and thus
            //every other turn
            if (isRedTurn()) dropRedCoin();
            else dropYellowCoin();

            switchCoin();

            count++;//We want to keep track of the turns

            printBoard();

            //Let's check for a winner during every
            //turn made and say who it is
            winner = checkWinner();
            loop = false;

            if (winner == null) {
                if (count < ROWS * COLUMNS) {
                    loop = true;
                } else {
                    System.out.println("It's a draw!");
                }
            } else if ('R' == winner)
                System.out.println("The red player won.");
            else if ('Y' == winner)
                System.out.println("The yellow player won.");
        }
        return winner;
    }
}
