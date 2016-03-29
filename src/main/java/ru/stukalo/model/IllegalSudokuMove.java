package ru.stukalo.model;

public class IllegalSudokuMove extends Exception {
    public IllegalSudokuMove() {
    }

    public IllegalSudokuMove(String message) {
        super(message);
    }
}
