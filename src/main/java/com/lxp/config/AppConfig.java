package com.lxp.config;

import com.lxp.controller.CourseController;
import com.lxp.controller.InstructorController;
import com.lxp.view.CourseListView;
import com.lxp.view.CourseView;
import com.lxp.view.InstructorListView;
import com.lxp.view.InstructorView;
import com.lxp.view.MainView;
import com.lxp.view.MenuRenderer;

public class AppConfig {

	private final MenuRenderer menuRenderer = new MenuRenderer();
	private final CourseController courseController = new CourseController();
	private final InstructorController instructorController = new InstructorController();
	private final CourseListView courseListView = new CourseListView(menuRenderer, courseController);
	private final InstructorListView instructorListView = new InstructorListView(menuRenderer, instructorController);
	private final CourseView courseView = new CourseView(menuRenderer, courseController, courseListView);
	private final InstructorView instructorView = new InstructorView(menuRenderer, instructorController,
		instructorListView);
	private final MainView mainView = new MainView(menuRenderer, courseView, instructorView);

	public MainView mainView() {
		return mainView;
	}
}
