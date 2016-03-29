package ru.stukalo.model;

import java.util.Objects;

public class SudokuMoveResult {
    private boolean okForRow;
    private boolean okForCol;
    private boolean okForSquare;
    private boolean solved;

    public SudokuMoveResult() {
    }

    public SudokuMoveResult(boolean okForRow, boolean okForCol, boolean okForSquare, boolean solved) {
        this.okForRow = okForRow;
        this.okForCol = okForCol;
        this.okForSquare = okForSquare;
        this.solved = solved;
    }

    public boolean isOkForRow() {
        return okForRow;
    }

    public boolean isOkForCol() {
        return okForCol;
    }

    public boolean isOkForSquare() {
        return okForSquare;
    }

    public boolean isSolved() {
        return solved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SudokuMoveResult that = (SudokuMoveResult) o;
        return okForRow == that.okForRow &&
                okForCol == that.okForCol &&
                okForSquare == that.okForSquare &&
                solved == that.solved;
    }

    @Override
    public int hashCode() {
        return Objects.hash(okForRow, okForCol, okForSquare, solved);
    }

    @Override
    public String toString() {
        return "SudokuMoveResult{" +
                "okForRow=" + okForRow +
                ", okForCol=" + okForCol +
                ", okForSquare=" + okForSquare +
                ", solved=" + solved +
                '}';
    }
}
