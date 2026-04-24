package com.lxp.service;

import java.util.List;

import com.lxp.controller.request.CourseDeleteRequest;
import com.lxp.controller.request.CourseRegisterRequest;
import com.lxp.controller.request.CourseUpdateRequest;
import com.lxp.domain.Content;
import com.lxp.domain.Course;
import com.lxp.domain.Instructor;

public interface CourseService {

	Course register(CourseRegisterRequest request);

	List<Course> findAll();

	Course findDetailById(Long id);

	Instructor findInstructorById(Long id);

	List<Content> findContentsByCourseId(Long courseId);

	Course update(CourseUpdateRequest request);

	Course delete(CourseDeleteRequest request);
}
