package com.lxp.service;

import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.request.CourseRegisterRequest;
import com.lxp.domain.Content;
import com.lxp.domain.Course;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.ContentRepository;
import com.lxp.repository.CourseRepository;
import com.lxp.repository.InstructorRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseService {

	private final CourseRepository courseRepository;
	private final ContentRepository contentRepository;
	private final InstructorRepository instructorRepository;

	public Course register(CourseRegisterRequest request) {
		Long instructorId = instructorRepository.findById(request.instructorId())
			.orElseThrow(() -> new LxpException(ErrorCode.NOT_FOUND_INSTRUCTOR))
			.getId();
		Course course = Course.create(
			instructorId,
			request.title(),
			request.description(),
			request.price(),
			request.level()
		);
		Course savedCourse = courseRepository.save(course);
		saveContents(savedCourse.getId(), request);
		return savedCourse;
	}

	private void saveContents(Long courseId, CourseRegisterRequest request) {
		int seq = 1;
		for (ContentRegisterRequest contentRequest : request.contents()) {
			Content content = Content.create(
				courseId,
				contentRequest.title(),
				contentRequest.body(),
				contentRequest.contentType(),
				seq++
			);
			contentRepository.save(content);
		}
	}
}
