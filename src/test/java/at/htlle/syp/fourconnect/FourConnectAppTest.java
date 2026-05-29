package at.htlle.syp.fourconnect;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * System test for four connect app (7x7 Grid)
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

        // R:0, Y:0, R:1, Y:1, R:2, Y:2, R:3 -> R gewinnt in der untersten Zeile
        String testData = "0011223";

        setup(testData);
        Character winner = app.runGame();

        assertEquals('R', winner);
        char[][] board = app.getBoard();
        // Bei ROWS=7 ist der Boden bei Index 6
        assertEquals('R', board[6][0]);
        assertEquals('R', board[6][1]);
        assertEquals('R', board[6][2]);
        assertEquals('R', board[6][3]);
    }

    @Test
    public void redWinsInColumn()
    {
        printPreample("Red wins in a Column");

        // R:0, Y:1, R:0, Y:1, R:0, Y:1, R:0 -> Vertikaler Sieg für Rot in Spalte 0
        String testData = "0101010";

        setup(testData);
        Character winner = app.runGame();

        assertEquals('R', winner);
        char[][] board = app.getBoard();
        // Von unten nach oben: Zeilen 6, 5, 4, 3
        assertEquals('R', board[6][0]);
        assertEquals('R', board[5][0]);
        assertEquals('R', board[4][0]);
        assertEquals('R', board[3][0]);
    }

    @Test
    public void redWinsInLeftToRightDown()
    {
        printPreample("Red wins diagonal left to right down");

        // erzeugt eine Diagonale von (3,0) bis (6,3) im 7x7 Board
        // checkWinner nutzt: board[row][column] == board[row+1][column+1]...
        String testData = "00010112253";

        setup(testData);
        Character winner = app.runGame();

        assertEquals('R', winner);
        char[][] board = app.getBoard();

        assertEquals('R', board[3][0]);
        assertEquals('R', board[4][1]);
        assertEquals('R', board[5][2]);
        assertEquals('R', board[6][3]);
    }

    @Test
    public void redWinsInLeftUpToRight()
    {
        printPreample("Red wins diagonal left up to right");

        // Erzeugt eine ansteigende Diagonale von (6,0) bis (3,3)
        // checkWinner nutzt: board[row][column] == board[row-1][column+1]...
        String testData = "33322414454";

        setup(testData);
        Character winner = app.runGame();

        assertEquals('R', winner);
        char[][] board = app.getBoard();

        assertEquals('R', board[6][0]);
        assertEquals('R', board[5][1]);
        assertEquals('R', board[4][2]);
        assertEquals('R', board[3][3]);
    }

    @Test
    public void yellowWinsInRow()
    {
        printPreample("Yellow wins in a Row");

        // R verfehlt, Y baut Reihe: R:6, Y:0, R:6, Y:1, R:6, Y:2, R:5, Y:3
        String testData = "60616253";

        setup(testData);
        Character winner = app.runGame();

        assertEquals('Y', winner);
        char[][] board = app.getBoard();
        assertEquals('Y', board[6][0]);
        assertEquals('Y', board[6][1]);
        assertEquals('Y', board[6][2]);
        assertEquals('Y', board[6][3]);
    }

    @Test
    public void yellowWinsInColumn()
    {
        printPreample("Yellow wins in a Column");

        // R:0, Y:1, R:2, Y:1, R:2, Y:1, R:2, Y:1
        String testData = "01212121";

        setup(testData);
        Character winner = app.runGame();

        assertEquals('Y', winner);
        char[][] board = app.getBoard();
        assertEquals('Y', board[6][1]);
        assertEquals('Y', board[5][1]);
        assertEquals('Y', board[4][1]);
        assertEquals('Y', board[3][1]);
    }

    @Test
    public void fillWholeBoard()
    {
        printPreample("Fill whole board");

        // Eine deterministische Sequenz für ein 7x7 Unentschieden (49 Felder).
        // Es befüllt das Board spaltenweise/schachbrettartig so, dass niemand 4 in eine Reihe kriegt.
        String sequence = "0000000111111122222224444444333333355555556666666";

        setup(sequence);
        Character winner = app.runGame();
        char[][] board = app.getBoard();

        int count = 0;
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 7; c++) {
                if (board[r][c] == 'R' || board[r][c] == 'Y') {
                    count++;
                }
            }
        }

        // Es muss ein Unentschieden sein
        assertNull(winner, "Es sollte keinen Gewinner geben (Unentschieden).");

        // Alle Felder (7x7 = 49) müssen voll sein
        assertEquals(49, count, "Das gesamte 7x7 Board (49 Felder) sollte gefüllt sein.");
    }
}