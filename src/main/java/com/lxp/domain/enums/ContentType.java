package com.lxp.domain.enums;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

public enum ContentType {
	VIDEO, TEXT, FILE;

	public static ContentType from(String input) {
		return switch (input.trim().toUpperCase()) {
			case "1", "VIDEO" -> VIDEO;
			case "2", "TEXT" -> TEXT;
			case "3", "FILE" -> FILE;
			default -> throw new LxpException(ErrorCode.INVALID_CONTENT_TYPE);
		};
	}
}
