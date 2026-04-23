package com.lxp.controller.request;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

public record ContentUpdateRequest(
	Long id,
	String title,
	String body
) {

	public ContentUpdateRequest {
		if (id == null || id <= 0L) {
			throw new LxpException(ErrorCode.INVALID_INPUT);
		}
		title = normalize(title);
		body = normalize(body);
	}

	private static String normalize(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value;
	}
}
