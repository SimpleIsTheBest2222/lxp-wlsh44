package com.lxp.controller.response;

import java.util.List;

public record InstructorListResponse(
	List<InstructorSummaryResponse> instructors
) {
	public boolean isEmpty() {
		return instructors.isEmpty();
	}
}
