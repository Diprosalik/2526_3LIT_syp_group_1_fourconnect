package at.htlle.syp.fourconnect;

import java.util.Arrays;

/**
 * (c) Helmut Steineder
 * (c) Reifert Jesse, Jan Kriegl, Florian Klug, Fabio Hofstadler
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

    public static final int EXIT_CODE = 8; // Für "008". Wenn es "007" sein soll, einfach in 7 ändern.
    private boolean gameAborted = false;   // Flag, um den Abbruch zu tracken

    //Here's are basic move, making the lowest empty row
    //of a specific column have a Red
    public void dropRedCoin() {
        System.out.println("Drop a red disk at column (0–7) [or " + EXIT_CODE + " to quit]: ");
        int column = columnInputInterface.getColumn();

        // Überprüfung auf Abbruch-Code
        if (column == EXIT_CODE) {
            gameAborted = true;
            return;
        }

        for (int row = 0; row < ROWS; row++) {
            if (board[ROWS - row - 1][column] == ' ') {
                board[ROWS - row - 1][column] = 'R';
                break;
            }
        }
    }

    public void dropYellowCoin() {
        System.out.println("Drop a yellow disk at column (0–7) [or " + EXIT_CODE + " to quit]: ");
        int column = columnInputInterface.getColumn();

        // Überprüfung auf Abbruch-Code
        if (column == EXIT_CODE) {
            gameAborted = true;
            return;
        }

        for (int row = 0; row < ROWS; row++) {
            if (board[ROWS - row - 1][column] == ' ') {
                board[ROWS - row - 1][column] = 'Y';
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
        initBoard();
        boolean loop = true;
        int count = 0;
        printBoard();
        Character winner = null;
        gameAborted = false; // Zurücksetzen bei Spielstart

        while (loop) {
            if (isRedTurn()) dropRedCoin();
            else dropYellowCoin();

            // Falls James Bond das Spiel abgebrochen hat:
            if (gameAborted) {
                System.out.println("Game aborted by James Bond.");
                return null; // Keine Gewinner, Spiel bricht ab
            }

            switchCoin();
            count++;
            printBoard();

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
