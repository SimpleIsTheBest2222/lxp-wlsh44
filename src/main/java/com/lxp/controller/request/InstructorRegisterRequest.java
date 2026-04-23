package com.lxp.controller.request;

import com.lxp.common.validate.Assert;
import com.lxp.exception.ErrorCode;

public record InstructorRegisterRequest(
	String name,
	String introduction
) {

	public InstructorRegisterRequest {
		Assert.notEmpty(name, ErrorCode.INVALID_INPUT);
		Assert.notNull(introduction, ErrorCode.INVALID_INPUT);
	}
}
