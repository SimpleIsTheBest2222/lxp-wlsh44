package com.lxp.config;

import com.lxp.service.ContentService;
import com.lxp.service.CourseService;
import com.lxp.service.DefaultContentService;
import com.lxp.service.DefaultCourseService;
import com.lxp.service.DefaultInstructorService;
import com.lxp.service.InstructorService;
import com.lxp.transaction.JdbcTransactionManager;
import com.lxp.transaction.TransactionProxyFactory;

public class ServiceConfig {

	private final CourseService courseService;
	private final ContentService contentService;
	private final InstructorService instructorService;

	public ServiceConfig(RepositoryConfig repositoryConfig) {
		CourseService courseService = new DefaultCourseService(
			repositoryConfig.courseRepository(),
			repositoryConfig.contentRepository(),
			repositoryConfig.instructorRepository()
		);
		ContentService contentService = new DefaultContentService(
			repositoryConfig.courseRepository(),
			repositoryConfig.contentRepository()
		);
		InstructorService instructorService = new DefaultInstructorService(repositoryConfig.instructorRepository());

		if (repositoryConfig.isJdbcMode()) {
			TransactionProxyFactory proxyFactory = new TransactionProxyFactory(
				new JdbcTransactionManager(repositoryConfig.connectionProvider())
			);
			this.courseService = proxyFactory.createProxy(CourseService.class, courseService);
			this.contentService = proxyFactory.createProxy(ContentService.class, contentService);
			this.instructorService = proxyFactory.createProxy(InstructorService.class, instructorService);
			return;
		}

		this.courseService = courseService;
		this.contentService = contentService;
		this.instructorService = instructorService;
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
