package ru.stukalo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.stukalo.model.Sudoku;

import static ru.stukalo.model.Sudoku.row;

@SpringBootApplication
public class SudokuApplication {
	@Bean
	public static Sudoku createSudoku() {
		return new Sudoku(
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

	public static void main(String[] args) {
		SpringApplication.run(SudokuApplication.class, args);
	}
}
