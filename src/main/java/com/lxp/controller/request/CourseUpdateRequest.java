package com.lxp.controller.request;

import com.lxp.domain.enums.Level;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

public record CourseUpdateRequest(
	Long id,
	String title,
	String description,
	Integer price,
	Level level
) {

	public CourseUpdateRequest(
		Long id,
		String title,
		String description,
		String price,
		String level
	) {
		this(
			id,
			normalize(title),
			normalize(description),
			parseInt(price),
			parseLevel(level)
		);
	}

	public CourseUpdateRequest {
		if (id == null || id <= 0L) {
			throw new LxpException(ErrorCode.INVALID_INPUT);
		}
	}

	private static String normalize(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value;
	}

	private static Integer parseInt(String value) {
		String normalizedValue = normalize(value);
		if (normalizedValue == null) {
			return null;
		}
		try {
			return Integer.parseInt(normalizedValue);
		} catch (NumberFormatException e) {
			throw new LxpException(ErrorCode.INVALID_INPUT);
		}
	}

	private static Level parseLevel(String value) {
		String normalizedValue = normalize(value);
		if (normalizedValue == null) {
			return null;
		}
		return Level.from(normalizedValue);
	}
}
