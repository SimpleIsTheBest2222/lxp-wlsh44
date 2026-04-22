package com.lxp.config;

import java.util.Scanner;

import com.lxp.controller.CourseController;
import com.lxp.controller.InstructorController;
import com.lxp.controller.MainController;
import com.lxp.view.InputView;
import com.lxp.view.OutputView;

public class AppConfig {

	private final Scanner scanner = new Scanner(System.in);
	private final InputView inputView = new InputView(scanner);
	private final OutputView outputView = new OutputView();
	private final CourseController courseController = new CourseController(inputView, outputView);
	private final InstructorController instructorController = new InstructorController(inputView, outputView);
	private final MainController mainController = new MainController(
			inputView, outputView, courseController, instructorController);

	public MainController mainController() {
		return mainController;
	}
}
