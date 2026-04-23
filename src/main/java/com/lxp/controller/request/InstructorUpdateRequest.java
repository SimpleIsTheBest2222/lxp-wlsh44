package com.lxp.controller.request;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

public record InstructorUpdateRequest(
	Long id,
	String name,
	String introduction
) {

	public InstructorUpdateRequest {
		validateId(id);
		name = normalize(name);
		introduction = normalize(introduction);
	}

	private static void validateId(Long id) {
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
}
