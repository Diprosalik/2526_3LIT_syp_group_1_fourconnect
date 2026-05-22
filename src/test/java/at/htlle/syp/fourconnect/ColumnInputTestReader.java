package at.htlle.syp.fourconnect;

/**
 * To get testing easier we use an InputStreamReader to get the input from the user
 */
public class ColumnInputTestReader implements ColumnInputInterface {
    private final int[] columns;
    private int currentIndex = 0;

    public ColumnInputTestReader(int[] columns) {
        this.columns = columns;
    }

    public int getColumn() {
        if (currentIndex < columns.length) {
            return columns[currentIndex++];
        }
        throw new RuntimeException("No more columns to read");
    }
}
