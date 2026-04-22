package com.lxp.config;

import com.lxp.controller.CourseController;
import com.lxp.controller.InstructorController;
import com.lxp.view.CourseListView;
import com.lxp.view.CourseView;
import com.lxp.view.InstructorListView;
import com.lxp.view.InstructorView;
import com.lxp.view.MainView;
import com.lxp.view.MenuRunner;

public class AppConfig {

	private final MenuRunner menuRunner = new MenuRunner();
	private final CourseController courseController = new CourseController();
	private final InstructorController instructorController = new InstructorController();
	private final CourseListView courseListView = new CourseListView(menuRunner, courseController);
	private final InstructorListView instructorListView = new InstructorListView(menuRunner, instructorController);
	private final CourseView courseView = new CourseView(menuRunner, courseController, courseListView);
	private final InstructorView instructorView = new InstructorView(menuRunner, instructorController,
		instructorListView);
	private final MainView mainView = new MainView(menuRunner, courseView, instructorView);

	public MainView mainView() {
		return mainView;
	}
}
