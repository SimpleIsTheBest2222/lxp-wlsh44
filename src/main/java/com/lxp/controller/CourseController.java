package com.lxp.controller;

import java.util.List;

import com.lxp.controller.request.CourseDeleteRequest;
import com.lxp.controller.request.CourseRegisterRequest;
import com.lxp.controller.request.CourseUpdateRequest;
import com.lxp.controller.response.ContentSummaryResponse;
import com.lxp.controller.response.CourseDeleteResponse;
import com.lxp.controller.response.CourseDetailResponse;
import com.lxp.controller.response.CourseListResponse;
import com.lxp.controller.response.CourseRegisterResponse;
import com.lxp.controller.response.CourseSummaryResponse;
import com.lxp.controller.response.CourseUpdateResponse;
import com.lxp.domain.Course;
import com.lxp.domain.Instructor;
import com.lxp.service.CourseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseController {

	private final CourseService courseService;

	public CourseRegisterResponse register(CourseRegisterRequest request) {
		Course course = courseService.register(request);
		return new CourseRegisterResponse(course.getId());
	}

	public CourseListResponse findAll() {
		List<CourseSummaryResponse> courses = courseService.findAll().stream()
			.map(course -> new CourseSummaryResponse(
				course.getId(),
				course.getTitle(),
				courseService.findInstructorById(course.getInstructorId()).getName()
			))
			.toList();
		return new CourseListResponse(courses);
	}

	public CourseDetailResponse findDetailById(Long id) {
		Course course = courseService.findDetailById(id);
		Instructor instructor = courseService.findInstructorById(course.getInstructorId());
		List<ContentSummaryResponse> contents = courseService.findContentsByCourseId(id).stream()
			.map(content -> new ContentSummaryResponse(content.getId(), content.getTitle()))
			.toList();

		return new CourseDetailResponse(
			course.getId(),
			course.getTitle(),
			instructor.getName(),
			instructor.getIntroduction(),
			course.getPrice(),
			course.getLevel().displayName(),
			course.getDescription(),
			contents,
			course.getCreatedAt(),
			course.getModifiedAt()
		);
	}

	public CourseUpdateResponse update(CourseUpdateRequest request) {
		Course course = courseService.update(request);
		return new CourseUpdateResponse(course.getId());
	}

	public CourseDeleteResponse delete(CourseDeleteRequest request) {
		Course course = courseService.delete(request);
		return new CourseDeleteResponse(course.getId());
	}
}
