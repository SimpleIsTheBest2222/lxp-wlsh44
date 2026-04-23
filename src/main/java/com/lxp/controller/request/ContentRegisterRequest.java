package com.lxp.controller.request;

import com.lxp.common.validate.Assert;
import com.lxp.domain.enums.ContentType;
import com.lxp.exception.ErrorCode;

public record ContentRegisterRequest(Long courseId, String title, String body, ContentType contentType) {

	public ContentRegisterRequest(String title, String body) {
		this(null, title, body, ContentType.TEXT);
	}

	public ContentRegisterRequest(Long courseId, String title, String body) {
		this(courseId, title, body, ContentType.TEXT);
	}

	public ContentRegisterRequest {
		Assert.notEmpty(title, ErrorCode.INVALID_INPUT);
		Assert.notEmpty(body, ErrorCode.INVALID_INPUT);
		Assert.notNull(contentType, ErrorCode.INVALID_INPUT);
	}
}
