package com.lxp.controller.response;

import java.util.List;
import java.util.Objects;

public record InstructorListResponse(
	List<InstructorSummaryResponse> instructors
) {
	public InstructorListResponse {
		instructors = List.copyOf(Objects.requireNonNullElse(instructors, List.of()));
	}

	public boolean isEmpty() {
		return instructors.isEmpty();
	}
}
