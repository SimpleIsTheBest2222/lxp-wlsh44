package com.lxp.controller.request;

import com.lxp.common.validate.Assert;

public record InstructorRegisterRequest(
	String name,
	String introduction
) {

	public InstructorRegisterRequest {
		Assert.notEmpty(name);
		Assert.notNull(introduction);
	}
}
