package com.lxp.controller.request;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

public record ContentDeleteRequest(
	Long id
) {
	public ContentDeleteRequest {
		if (id == null || id <= 0L) {
			throw new LxpException(ErrorCode.INVALID_INPUT);
		}
	}
}
