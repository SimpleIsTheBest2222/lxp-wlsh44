package com.lxp.controller;

import com.lxp.controller.request.CourseRegisterRequest;
import com.lxp.controller.response.CourseRegisterResponse;
import com.lxp.domain.Course;
import com.lxp.service.CourseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseController {

	private final CourseService courseService;

	public CourseRegisterResponse register(CourseRegisterRequest request) {
		Course course = courseService.register(request);
		return new CourseRegisterResponse(course.getId());
	}

	public void findAll() { // not implemented
	}

	public void findById() { // not implemented
	}
}
