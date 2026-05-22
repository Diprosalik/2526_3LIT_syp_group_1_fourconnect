package at.htlle.syp.fourconnect;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * System test for four connect app
 */
public class FourConnectAppTest
{
    private FourConnectApp app;

    public void setup(String testData) {
        int[] columns = new int[testData.length()];
        for (int i = 0; i < testData.length(); i++) {
            columns[i] = Character.getNumericValue(testData.charAt(i));
        }

        app = new FourConnectApp();
        app.setColumnReader(new ColumnInputTestReader(columns));
    }
    @AfterEach
    public void teardown() {
    }

    private void printPreample(String preample) {
        System.out.println("___________________________________________________________________________");
        System.out.println("    " + preample);
        System.out.println();
    }

    @Disabled
    @Test
    public void runJamesBond()
    {
        printPreample("James Bond");

        String testData = "007";


        setup(testData);

        app.runGame();

        assertTrue( true );
    }

    @Test
    public void redWinsInRow()
    {
        printPreample("Red wins in a Row");

        String testData = "00112233";

        setup(testData);

        Character winner = app.runGame();

        assertEquals('R', winner);
        char[][] board = app.getBoard();
        assertEquals('R', board[5][0]);
        assertEquals('R', board[5][1]);
        assertEquals('R', board[5][2]);
        assertEquals('R', board[5][3]);
    }

    @Test
    public void redWinsInColumn()
    {
        printPreample("Red wins in a Column");

        String testData = "0101010";

        setup(testData);

        Character winner = app.runGame();

        assertEquals('R', winner);
        char[][] board = app.getBoard();
        assertEquals('R', board[5][0]);
        assertEquals('R', board[4][0]);
        assertEquals('R', board[3][0]);
        assertEquals('R', board[2][0]);
    }

    @Test
    public void redWinsInLeftToRightDown()
    {
        printPreample("Red wins diagonal left to right down");

        String testData = "00010112203";

        setup(testData);

        Character winner = app.runGame();

        assertEquals('R', winner);
        char[][] board = app.getBoard();
        // R at (5,0), (4,1), (3,2), (2,3) - check code logic
        // Wait, checkWinner logic for diagonal:
        // board[row][column] == board[row+1][column+1] == board[row+2][column+2] == board[row+3][column+3]
        // This is left-TOP to right-BOTTOM if we think of row 0 as top.
        // Let's verify where they land.
        // 0: R(5,0), Y(4,0), R(3,0), Y(2,0)
        // 1: Y(5,1), R(4,1), Y(3,1)
        // 2: R(5,2), Y(4,2)
        // 3: R(5,3)
        // Diagonal R at: (2,0), (3,1), (4,2), (5,3).
        // checkWinner: row=2, col=0 -> board[2][0], board[3][1], board[4][2], board[5][3]. Matches!

        assertEquals('R', board[2][0]);
        assertEquals('R', board[3][1]);
        assertEquals('R', board[4][2]);
        assertEquals('R', board[5][3]);
    }

    @Test
    public void redWinsInLeftUpToRight()
    {
        printPreample("Red wins diagonal left up to right");

        String testData = "01122323303";

        setup(testData);

        Character winner = app.runGame();

        assertEquals('R', winner);
        char[][] board = app.getBoard();
        // R at: (5,0), (4,1), (3,2), (2,3).
        // checkWinner diagonal left up to right:
        // row=5: board[5][0], board[4][1], board[3][2], board[2][3]. Matches!

        assertEquals('R', board[5][0]);
        assertEquals('R', board[4][1]);
        assertEquals('R', board[3][2]);
        assertEquals('R', board[2][3]);
    }

    @Test
    public void yellowWinsInRow()
    {
        printPreample("Yellow wins in a Row");

        String testData = "60011223";

        setup(testData);

        Character winner = app.runGame();

        assertEquals('Y', winner);
        char[][] board = app.getBoard();
        assertEquals('Y', board[5][0]);
        assertEquals('Y', board[5][1]);
        assertEquals('Y', board[5][2]);
        assertEquals('Y', board[5][3]);
    }

    @Test
    public void yellowWinsInColumn()
    {
        printPreample("Yellow wins in a Column");

        String testData = "01212121";

        setup(testData);

        Character winner = app.runGame();

        assertEquals('Y', winner);
        char[][] board = app.getBoard();
        assertEquals('Y', board[5][1]);
        assertEquals('Y', board[4][1]);
        assertEquals('Y', board[3][1]);
        assertEquals('Y', board[2][1]);
    }

    @Test
    public void fillWholeBoard()
    {
        printPreample("Fill whole board");

        // 1. Fill up the first three columns (0, 1, 2)
        //    Since there are 6 rows, we need 18 moves to fill 3 columns.
        //    We alternate Red and Yellow.
        //    Col 0: 6 times, Col 1: 6 times, Col 2: 6 times
        
        StringBuilder sb = new StringBuilder();
        // Filling first 3 columns (0, 1, 2)
        for (int col = 0; col <= 2; col++) {
            for (int row = 0; row < 6; row++) {
                sb.append(col);
            }
        }

        // 2. make a twist three times
        for (int i = 0; i < 3; i++) {
            sb.append("63344556");
        }

        String sequence = sb.toString();
        
        setup(sequence);
        Character winner = app.runGame();
        char[][] board = app.getBoard();
        app.printBoard();
        System.out.println("Winner: " + winner);
        
        int count = 0;
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                if (board[r][c] == 'R' || board[r][c] == 'Y') {
                    count++;
                }
            }
        }

        // It's a draw
        assertNull( winner );

        // As it is a draw all fields in the board should be filled
        assertEquals(42, count, "Board should be significantly filled. Count: " + count);

        // If the user wants to see it "until whole board is filled",
        // they might expect 42, but with a fixed sequence it depends on if someone wins.
    }
}
