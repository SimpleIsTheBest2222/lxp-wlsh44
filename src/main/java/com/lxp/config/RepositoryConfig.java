package com.lxp.config;

import com.lxp.repository.CourseRepository;
import com.lxp.repository.ContentRepository;
import com.lxp.repository.InstructorRepository;
import com.lxp.repository.inmemory.InMemoryCourseRepository;
import com.lxp.repository.inmemory.InMemoryContentRepository;
import com.lxp.repository.inmemory.InMemoryInstructorRepository;

public class RepositoryConfig {

	private final CourseRepository courseRepository = new InMemoryCourseRepository();
	private final ContentRepository contentRepository = new InMemoryContentRepository();
	private final InstructorRepository instructorRepository = new InMemoryInstructorRepository();

	public CourseRepository courseRepository() {
		return courseRepository;
	}

	public ContentRepository contentRepository() {
		return contentRepository;
	}

	public InstructorRepository instructorRepository() {
		return instructorRepository;
	}
}
