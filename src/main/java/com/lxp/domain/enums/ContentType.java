package com.lxp.domain.enums;

import com.lxp.common.validate.Assert;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

public enum ContentType {
	VIDEO, TEXT, FILE;

	public static ContentType from(String value) {
		Assert.notEmpty(value, ErrorCode.INVALID_INPUT);

		return switch (value.trim().toUpperCase()) {
			case "1", "VIDEO" -> VIDEO;
			case "2", "TEXT" -> TEXT;
			case "3", "FILE" -> FILE;
			default -> throw new LxpException(ErrorCode.INVALID_CONTENT_TYPE);
		};
	}
}
