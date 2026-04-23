package com.lxp.controller.request;

import com.lxp.common.validate.Assert;
import com.lxp.domain.enums.ContentType;
import com.lxp.exception.ErrorCode;

public record ContentRegisterRequest(
	Long courseId,
	String title,
	String body,
	ContentType contentType,
	int seq
) {

	public ContentRegisterRequest(String title, String body, int seq) {
		this(null, title, body, ContentType.TEXT, seq);
	}

	public ContentRegisterRequest(Long courseId, String title, String body, int seq) {
		this(courseId, title, body, ContentType.TEXT, seq);
	}

	public ContentRegisterRequest {
		Assert.notEmpty(title, ErrorCode.INVALID_INPUT);
		Assert.notEmpty(body, ErrorCode.INVALID_INPUT);
		Assert.notNull(contentType, ErrorCode.INVALID_INPUT);
		Assert.isTrue(seq > 0, ErrorCode.INVALID_INPUT);
	}
}
