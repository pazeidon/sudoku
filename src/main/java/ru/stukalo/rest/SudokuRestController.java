package ru.stukalo.rest;

import org.springframework.web.bind.annotation.*;
import ru.stukalo.model.IllegalSudokuMove;
import ru.stukalo.model.Sudoku;
import ru.stukalo.model.SudokuMove;
import ru.stukalo.model.SudokuMoveResult;

import static ru.stukalo.model.Sudoku.row;

@RestController
public class SudokuRestController {
    private final Sudoku sudoku = new Sudoku(
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

    @RequestMapping(value = "/sudoku/fill", method = RequestMethod.POST, produces={"application/json"})
    public @ResponseBody SudokuMoveResult fill(@RequestBody SudokuMove sudokuMove) throws IllegalSudokuMove {
        return sudoku.addDigit(sudokuMove.getDigit(), sudokuMove.getTopRow(), sudokuMove.getColLeft());
    }

    @ExceptionHandler(IllegalSudokuMove.class)
    public void handleException() {
    }
}
