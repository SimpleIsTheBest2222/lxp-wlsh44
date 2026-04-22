package com.lxp.config;

import java.util.Scanner;

import com.lxp.view.CourseListView;
import com.lxp.view.CourseView;
import com.lxp.view.InputView;
import com.lxp.view.InstructorDetailView;
import com.lxp.view.InstructorListView;
import com.lxp.view.InstructorSelectView;
import com.lxp.view.InstructorView;
import com.lxp.view.MainView;
import com.lxp.view.MenuRenderer;
import com.lxp.view.OutputView;

public class ViewConfig {

	private final ControllerConfig controllerConfig;

	private final Scanner scanner = new Scanner(System.in);
	private final InputView inputView = new InputView(scanner);
	private final OutputView outputView = new OutputView();
	private final MenuRenderer menuRenderer = new MenuRenderer(inputView, outputView);

	private final InstructorDetailView instructorDetailView;
	private final InstructorSelectView instructorSelectView;
	private final CourseListView courseListView;
	private final InstructorListView instructorListView;
	private final CourseView courseView;
	private final InstructorView instructorView;
	private final MainView mainView;

	public ViewConfig(ControllerConfig controllerConfig) {
		this.controllerConfig = controllerConfig;
		this.instructorDetailView = new InstructorDetailView(
			menuRenderer,
			inputView,
			outputView,
			this.controllerConfig.instructorController()
		);
		this.instructorSelectView = new InstructorSelectView(
			menuRenderer,
			outputView,
			this.controllerConfig.instructorController(),
			instructorDetailView
		);
		this.courseListView = new CourseListView(
			menuRenderer,
			outputView,
			this.controllerConfig.courseController()
		);
		this.instructorListView = new InstructorListView(
			menuRenderer,
			outputView,
			this.controllerConfig.instructorController(),
			instructorSelectView
		);
		this.courseView = new CourseView(
			menuRenderer,
			outputView,
			this.controllerConfig.courseController(),
			courseListView
		);
		this.instructorView = new InstructorView(
			menuRenderer,
			inputView,
			outputView,
			this.controllerConfig.instructorController(),
			instructorListView
		);
		this.mainView = new MainView(menuRenderer, courseView, instructorView);
	}

	public MainView mainView() {
		return mainView;
	}
}
