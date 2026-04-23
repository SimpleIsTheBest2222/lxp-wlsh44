package com.lxp.controller.request;

import java.util.List;

import com.lxp.common.validate.Assert;
import com.lxp.domain.enums.Level;
import com.lxp.exception.ErrorCode;

public record CourseRegisterRequest(
	Long instructorId,
	String title,
	String description,
	int price,
	Level level,
	List<ContentRegisterRequest> contents
) {

	public CourseRegisterRequest(Long instructorId, String title, String description, int price, String level) {
		this(instructorId, title, description, price, Level.from(level), List.of());
	}

	public CourseRegisterRequest(
		Long instructorId,
		String title,
		String description,
		int price,
		String level,
		List<ContentRegisterRequest> contents
	) {
		this(instructorId, title, description, price, Level.from(level), contents);
	}

	public CourseRegisterRequest {
		Assert.notNull(instructorId, ErrorCode.INVALID_INPUT);
		Assert.isTrue(instructorId > 0, ErrorCode.INVALID_INPUT);
		Assert.notEmpty(title, ErrorCode.INVALID_INPUT);
		Assert.notEmpty(description, ErrorCode.INVALID_INPUT);
		Assert.isTrue(price >= 0, ErrorCode.INVALID_INPUT);
		Assert.notNull(level, ErrorCode.INVALID_INPUT);
		Assert.notNull(contents, ErrorCode.INVALID_INPUT);
		contents = List.copyOf(contents);
	}
}
