package com.lxp.config;

import com.lxp.controller.CourseController;
import com.lxp.controller.InstructorController;

public class ControllerConfig {

	private final CourseController courseController;
	private final InstructorController instructorController;

	public ControllerConfig(ServiceConfig serviceConfig) {
		this.courseController = new CourseController(serviceConfig.courseService());
		this.instructorController = new InstructorController(serviceConfig.instructorService());
	}

	public CourseController courseController() {
		return courseController;
	}

	public InstructorController instructorController() {
		return instructorController;
	}
}
