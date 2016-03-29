package ru.stukalo.model;

//may need to add validation here
public class SudokuMove {
    private int digit;
    private int topRow;
    private int colLeft;

    public SudokuMove() {
    }

    public SudokuMove(int digit, int topRow, int colLeft) {
        this.digit = digit;
        this.topRow = topRow;
        this.colLeft = colLeft;
    }

    public int getDigit() {
        return digit;
    }

    public int getTopRow() {
        return topRow;
    }

    public int getColLeft() {
        return colLeft;
    }
}
