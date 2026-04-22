package com.lxp.config;

import java.util.Scanner;

import com.lxp.controller.CourseController;
import com.lxp.controller.InstructorController;
import com.lxp.view.CourseListView;
import com.lxp.view.CourseView;
import com.lxp.view.InputView;
import com.lxp.view.InstructorListView;
import com.lxp.view.InstructorView;
import com.lxp.view.MainView;
import com.lxp.view.MenuRenderer;
import com.lxp.view.OutputView;

public class AppConfig {

	private final Scanner scanner = new Scanner(System.in);
	private final InputView inputView = new InputView(scanner);
	private final OutputView outputView = new OutputView();
	private final MenuRenderer menuRenderer = new MenuRenderer(inputView, outputView);
	private final CourseController courseController = new CourseController();
	private final InstructorController instructorController = new InstructorController();
	private final CourseListView courseListView = new CourseListView(menuRenderer, outputView, courseController);
	private final InstructorListView instructorListView = new InstructorListView(menuRenderer, outputView,
		instructorController);
	private final CourseView courseView = new CourseView(menuRenderer, outputView, courseController, courseListView);
	private final InstructorView instructorView = new InstructorView(menuRenderer, outputView, instructorController,
		instructorListView);
	private final MainView mainView = new MainView(menuRenderer, courseView, instructorView);

	public MainView mainView() {
		return mainView;
	}
}
