package com.lxp.config;

import com.lxp.service.CourseService;
import com.lxp.service.ContentService;
import com.lxp.service.InstructorService;

public class ServiceConfig {

	private final CourseService courseService;
	private final ContentService contentService;
	private final InstructorService instructorService;

	public ServiceConfig(RepositoryConfig repositoryConfig) {
		this.courseService = new CourseService(
			repositoryConfig.courseRepository(),
			repositoryConfig.contentRepository()
		);
		this.contentService = new ContentService(repositoryConfig.contentRepository());
		this.instructorService = new InstructorService(repositoryConfig.instructorRepository());
	}

	public CourseService courseService() {
		return courseService;
	}

	public ContentService contentService() {
		return contentService;
	}

	public InstructorService instructorService() {
		return instructorService;
	}
}
