package com.lxp.controller.response;

import java.util.List;
import java.util.Objects;

public record CourseListResponse(
	List<CourseSummaryResponse> courses
) {
	public CourseListResponse {
		courses = List.copyOf(Objects.requireNonNullElse(courses, List.of()));
	}

	public boolean isEmpty() {
		return courses.isEmpty();
	}
}
