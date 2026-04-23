package com.lxp.controller.request;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

public record CourseDeleteRequest(
	Long id
) {
	public CourseDeleteRequest {
		if (id == null || id <= 0L) {
			throw new LxpException(ErrorCode.INVALID_INPUT);
		}
	}
}
