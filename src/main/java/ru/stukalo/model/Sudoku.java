package ru.stukalo.model;

import javax.annotation.concurrent.ThreadSafe;
import java.util.BitSet;
import java.util.Objects;

@ThreadSafe
public class Sudoku {
    static final int SUDOKU_SIZE = 9;
    static final int SUDOKU_SQUARE_SIZE = 3;

    private final byte[][] originalMatrix;
    private final byte[][] gameMatrix;
    private int unfilled = 0;

    public Sudoku(SudokuRow row, SudokuRow row1, SudokuRow row2, SudokuRow row3, SudokuRow row4, SudokuRow row5, SudokuRow row6, SudokuRow row7, SudokuRow row8) {
        originalMatrix = new byte[SUDOKU_SIZE][];
        gameMatrix = new byte[SUDOKU_SIZE][];
        fillMatrix(0,row);
        fillMatrix(1,row1);
        fillMatrix(2,row2);
        fillMatrix(3,row3);
        fillMatrix(4,row4);
        fillMatrix(5,row5);
        fillMatrix(6,row6);
        fillMatrix(7,row7);
        fillMatrix(8,row8);
    }

    private void fillMatrix(int i, SudokuRow sudokuRow) {
        int zeroCount = 0;
        for (int j = 0; j < SUDOKU_SIZE; j++) {
            if (sudokuRow.row[j] == 0) {
                zeroCount++;
            }
        }
        originalMatrix[i] = sudokuRow.row;
        gameMatrix[i] = sudokuRow.row.clone();
        unfilled += zeroCount;
    }

    /**
     * Adds a digit to sudoku
     * @param digit
     * @param topRow index of a row where to add a digit (from top), 1-based
     * @param colLeft index of a column where to add a digit (from left), 1-based
     * @return validation result
     * @throws IllegalSudokuMove in case of invalid digit or when trying to overwrite a hint
     */
    public SudokuMoveResult addDigit(int digit, int topRow, int colLeft) throws IllegalSudokuMove {
        if (!isValidSudokuDigit(digit)) throw new IllegalSudokuMove("The digit must be between 1 and 9");
        if (!isValidSudokuRow(digit)) throw new IllegalSudokuMove("The topRow must be between 1 and 9");
        if (!isValidSudokuRow(digit)) throw new IllegalSudokuMove("The colLeft must be between 1 and 9");

        //zero based index
        int row0 = (topRow - 1);
        int col0 = (colLeft - 1);

        if (isHint(row0,col0)) throw new IllegalSudokuMove("This topRow, colLeft is already filled with hint");

        //too blocking
        synchronized (this) {
            insert(digit, row0, col0);

            final boolean okForRow = isOkForRow(row0);
            final boolean okForColumn = isOkForColumn(col0);
            final boolean okForSquare = isOkForSquare(row0, col0);

            boolean solved = isFilled() && okForRow && okForColumn && okForSquare && isOkForAllExcept(row0, col0);
            return new SudokuMoveResult(okForRow, okForColumn, okForSquare, solved);
        }
    }

    private boolean isValidSudokuRow(int row) {
        return row >=1 && row <=9;
    }

    public synchronized boolean isSolved() {
        final int row = 0;
        final int col = 0;
        return isFilled() && isOkForRow(row) && isOkForColumn(col) && isOkForSquare(row, col) && isOkForAllExcept(row, col);
    }

    boolean isOkForAllExcept(int row, int col) {
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            if (i != row) {
                if (!isOkForRow(i)) {
                    return false;
                }
            }
            if (i != col) {
                if (!isOkForColumn(i)) {
                    return false;
                }
            }
        }

        final Coord excludedSquare = findSquare(row, col);
        for (int i = 0; i < SUDOKU_SIZE; i+=SUDOKU_SQUARE_SIZE) {
            for (int j = 0; j < SUDOKU_SIZE; j+=SUDOKU_SQUARE_SIZE) {
                if (!(excludedSquare.x == i && excludedSquare.y == j)) {
                    if (!checkSquare(i, j)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    boolean isFilled() {
        return unfilled == 0;
    }

    void insert(int digit, int row, int col) {
        if (gameMatrix[row][col] == 0) {
            --unfilled;
        }
        gameMatrix[row][col] = (byte) digit;
    }

    private boolean isValidSudokuDigit(int digit) {
        return digit >=1 && digit <=9;
    }

    boolean isHint(int row, int col) {
        return originalMatrix[row][col] != 0;
    }

    boolean isOkForColumn(int col) {
        final BitSet bitSet = new BitSet(9);

        for (int i = 0; i < SUDOKU_SIZE; i++) {
            if (gameMatrix[i][col] != 0) {
                if (bitSet.get(gameMatrix[i][col])) {
                    return false;
                } else {
                    bitSet.set(gameMatrix[i][col]);
                }
            }
        }

        return true;
    }

    boolean isOkForRow(int row) {
        final BitSet bitSet = new BitSet(9);

        for (int i = 0; i < SUDOKU_SIZE; i++) {
            if (gameMatrix[row][i] != 0) {
                if (bitSet.get(gameMatrix[row][i])) {
                    return false;
                } else {
                    bitSet.set(gameMatrix[row][i]);
                }
            }
        }

        return true;
    }

    boolean isOkForSquare(int row, int col) {
        final Coord coord = findSquare(row, col);
        return checkSquare(coord.x, coord.y);
    }

    private boolean checkSquare(int top, int left) {
        final BitSet bitSet = new BitSet(9);

        for (int i = 0; i < SUDOKU_SQUARE_SIZE; i++) {
            for (int j = 0; j < SUDOKU_SQUARE_SIZE; j++) {
                final int x = top + i;
                final int y = left + j;
                if (gameMatrix[x][y] != 0) {
                    if (bitSet.get(gameMatrix[x][y])) {
                        return false;
                    } else {
                        bitSet.set(gameMatrix[x][y]);
                    }
                }
            }
        }

        return true;
    }

    class Coord {
        int x, y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return x == coord.x &&
                    y == coord.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Coord{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    Coord findSquare(int row, int col) {
        return new Coord((row / SUDOKU_SQUARE_SIZE) * 3, (col / SUDOKU_SQUARE_SIZE) * 3);
    }

    public static SudokuRow row(int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        return new SudokuRow(i, i1, i2, i3, i4, i5, i6, i7, i8);
    }

    private static class SudokuRow {
        private byte[] row = new byte[9];

        private SudokuRow(int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            row[0] = (byte) i;
            row[1] = (byte) i1;
            row[2] = (byte) i2;
            row[3] = (byte) i3;
            row[4] = (byte) i4;
            row[5] = (byte) i5;
            row[6] = (byte) i6;
            row[7] = (byte) i7;
            row[8] = (byte) i8;
        }
    }
}
