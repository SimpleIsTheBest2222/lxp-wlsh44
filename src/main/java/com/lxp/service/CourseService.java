package com.lxp.service;

import com.lxp.controller.request.CourseRegisterRequest;
import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.domain.Content;
import com.lxp.domain.Course;
import com.lxp.repository.CourseRepository;
import com.lxp.repository.ContentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseService {

	private final CourseRepository courseRepository;
	private final ContentRepository contentRepository;

	public Course register(CourseRegisterRequest request) {
		Course course = Course.create(request.title(), request.description(), request.price(), request.level());
		Course savedCourse = courseRepository.save(course);
		saveContents(savedCourse.getId(), request);
		return savedCourse;
	}

	private void saveContents(Long courseId, CourseRegisterRequest request) {
		for (ContentRegisterRequest contentRequest : request.contents()) {
			Content content = Content.create(
				courseId,
				contentRequest.title(),
				contentRequest.body(),
				contentRequest.contentType(),
				contentRequest.seq()
			);
			contentRepository.save(content);
		}
	}
}
