package ru.stukalo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.stukalo.model.Sudoku;

import static ru.stukalo.model.Sudoku.row;

@SpringBootApplication
public class SudokuApplication {
	public static void main(String[] args) {
		SpringApplication.run(SudokuApplication.class, args);
	}
}
