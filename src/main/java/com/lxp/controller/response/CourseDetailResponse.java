package com.lxp.controller.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record CourseDetailResponse(
	Long id,
	String title,
	String instructorName,
	String instructorIntroduction,
	int price,
	String level,
	String description,
	List<ContentSummaryResponse> contents,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {
	public CourseDetailResponse {
		contents = List.copyOf(Objects.requireNonNullElse(contents, List.of()));
	}
}
