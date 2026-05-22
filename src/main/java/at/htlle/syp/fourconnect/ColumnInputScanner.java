package at.htlle.syp.fourconnect;

import java.util.Scanner;

public class ColumnInputScanner implements ColumnInputInterface {
    public int getColumn() {
        Scanner scan = new Scanner (System.in);

        int c = scan.nextInt();
        return c;
    }

}
