package com.lxp.config;

import com.lxp.controller.CourseController;
import com.lxp.controller.ContentController;
import com.lxp.controller.InstructorController;

public class ControllerConfig {

	private final CourseController courseController;
	private final ContentController contentController;
	private final InstructorController instructorController;

	public ControllerConfig(ServiceConfig serviceConfig) {
		this.courseController = new CourseController(serviceConfig.courseService());
		this.contentController = new ContentController(serviceConfig.contentService());
		this.instructorController = new InstructorController(serviceConfig.instructorService());
	}

	public CourseController courseController() {
		return courseController;
	}

	public ContentController contentController() {
		return contentController;
	}

	public InstructorController instructorController() {
		return instructorController;
	}
}
