package ru.stukalo.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static ru.stukalo.model.Sudoku.row;

public class SudokuTest {
    private Sudoku sudoku;

    @Before
    public void setUp() throws Exception {
        sudoku = new Sudoku(
                row(7,0,0,0,4,0,5,3,0),
                row(0,0,5,0,0,8,0,1,0),
                row(0,0,8,5,0,9,0,4,0),
                row(5,3,9,0,6,0,0,0,1),
                row(0,0,0,0,1,0,0,0,5),
                row(8,0,0,7,2,0,9,0,0),
                row(9,0,7,4,0,0,0,0,0),
                row(0,0,0,0,5,7,0,0,0),
                row(6,0,0,0,0,0,0,5,0)
        );
    }

    @Test(expected = IllegalSudokuMove.class)
    public void shouldThrowExceptionIfPosIsHint() throws Exception {
        sudoku.addDigit(8, 2, 3);
    }

    @Test
    public void shouldReturnFalseIfPosIsNotHint() throws Exception {
        sudoku.addDigit(8, 1, 2);
        assertFalse(sudoku.isHint(0,1));
    }

    @Test
    public void shouldFindCoordOfSquare() throws Exception {
        assertEquals(sudoku.new Coord(0, 0), sudoku.findSquare(0,1));
        assertEquals(sudoku.new Coord(3, 6), sudoku.findSquare(4,6));
        assertEquals(sudoku.new Coord(6, 3), sudoku.findSquare(6,3));
    }

    @Test
    public void shouldReturnTrueIfDigitIsOkForSquare() throws Exception {
        sudoku.insert(3, 0, 5);
        assertTrue(sudoku.isOkForSquare(0, 5));
        sudoku.insert(5, 7, 1);
        assertTrue(sudoku.isOkForSquare(7, 1));
        sudoku.insert(1, 0, 3);
        sudoku.insert(2, 0, 5);
        sudoku.insert(6, 1, 3);
        sudoku.insert(3, 1, 4);
        sudoku.insert(7, 2, 4);
        assertTrue(sudoku.isOkForSquare(2, 4));
    }

    @Test
    public void shouldReturnFalseIfDigitIsnotOkForSquare() throws Exception {
        sudoku.insert(4, 0, 5);
        assertFalse(sudoku.isOkForSquare(0, 5));
        sudoku.insert(9, 7, 1);
        assertFalse(sudoku.isOkForSquare(7, 1));
        sudoku.insert(1, 0, 3);
        sudoku.insert(2, 0, 5);
        sudoku.insert(6, 1, 3);
        sudoku.insert(3, 1, 4);
        sudoku.insert(7, 2, 4);
        //overwriting 6 at [1][3]
        sudoku.insert(7, 1, 3);
        assertFalse(sudoku.isOkForSquare(1, 3));
    }

    @Test
    public void shouldReturnTrueIfDigitIsOkForRow() throws Exception {
        sudoku.insert(7, 0, 0);
        assertTrue(sudoku.isOkForRow(0));
        sudoku.insert(6, 0, 2);
        assertTrue(sudoku.isOkForRow(0));
        sudoku.insert(1, 7, 2);
        assertTrue(sudoku.isOkForRow(7));

        //when the row #3 is all filled
        sudoku.insert(2, 3, 3);
        sudoku.insert(4, 3, 5);
        sudoku.insert(7, 3, 6);
        sudoku.insert(8, 3, 7);
        //then okForRow must be true
        assertTrue(sudoku.isOkForRow(3));
    }

    @Test
    public void shouldReturnTrueIfDigitIsOkForCol() throws Exception {
        sudoku.insert(1, 0, 2);
        assertTrue(sudoku.isOkForColumn(2));
        sudoku.insert(2, 4, 2);
        assertTrue(sudoku.isOkForColumn(2));
        sudoku.insert(3, 5, 2);
        sudoku.insert(4, 7, 2);
        sudoku.insert(6, 8, 2);
        assertTrue(sudoku.isOkForColumn(2));
    }

    @Test
    public void shouldReturnFalseIfDigitIsnotOkForRow() throws Exception {
        sudoku.insert(4, 0, 0);
        assertFalse(sudoku.isOkForRow(0));
        sudoku.insert(7, 0, 2);
        assertFalse(sudoku.isOkForRow(0));
        sudoku.insert(5, 7, 2);
        assertFalse(sudoku.isOkForRow(7));

        //given the row #3 as almost filled
        sudoku.insert(2, 3, 3);
        sudoku.insert(4, 3, 5);
        sudoku.insert(7, 3, 6);
        //when incorrect value is inserted
        sudoku.insert(9, 3, 7);
        //then okForRow must be false
        assertFalse(sudoku.isOkForRow(3));
    }

    @Test
    public void shouldReturnFalseIfDigitIsnotOkForCol() throws Exception {
        sudoku.insert(9, 2, 2);
        assertFalse(sudoku.isOkForColumn(2));
        sudoku.insert(5, 4, 2);
        assertFalse(sudoku.isOkForColumn(2));
        sudoku.insert(1, 0, 2);
        sudoku.insert(2, 4, 2);
        sudoku.insert(3, 5, 2);
        sudoku.insert(4, 7, 2);
        sudoku.insert(6, 8, 2);
        sudoku.insert(4, 5, 2);
        assertFalse(sudoku.isOkForColumn(2));
    }

    @Test(expected = IllegalSudokuMove.class)
    public void shouldThrowExceptionWhenInvalidDigit() throws Exception {
        sudoku.addDigit(0, 1, 2);
    }

    @Test
    public void shouldSolveSudoku() throws Exception {
        sudoku = new Sudoku(
                row(6,1,5,2,3,7,0,4,8),
                row(4,3,2,1,9,8,7,5,6),
                row(9,8,7,4,5,6,1,2,3),
                row(1,2,0,3,7,4,6,9,5),
                row(3,4,6,9,8,5,2,1,7),
                row(5,7,9,6,2,1,3,8,4),
                row(7,9,1,5,4,3,8,6,2),
                row(8,6,4,7,0,2,5,3,9),
                row(2,5,0,8,6,9,4,7,1)
        );

        //made incorrect move
        assertEquals(new SudokuMoveResult(false, true, false, false), sudoku.addDigit(3, 4, 3));
        assertFalse(sudoku.isFilled());
        assertFalse(sudoku.isSolved());

        //overwriting incorrect move
        assertEquals(new SudokuMoveResult(true, true, true, false), sudoku.addDigit(8, 4, 3));
        assertFalse(sudoku.isFilled());
        assertFalse(sudoku.isSolved());

        assertEquals(new SudokuMoveResult(true, true, true, false), sudoku.addDigit(3, 9, 3));
        assertFalse(sudoku.isFilled());
        assertFalse(sudoku.isSolved());

        assertEquals(new SudokuMoveResult(true, true, true, false), sudoku.addDigit(1, 8, 5));
        assertFalse(sudoku.isFilled());
        assertFalse(sudoku.isSolved());

        //made last move, but incorrect
        assertEquals(new SudokuMoveResult(false, false, false, false), sudoku.addDigit(8, 1, 7));
        assertTrue(sudoku.isFilled());
        assertFalse(sudoku.isSolved());

        //made last move, but incorrect
        assertEquals(new SudokuMoveResult(true, true, true, true), sudoku.addDigit(9, 1, 7));
        assertTrue(sudoku.isFilled());
        assertTrue(sudoku.isSolved());
    }
}