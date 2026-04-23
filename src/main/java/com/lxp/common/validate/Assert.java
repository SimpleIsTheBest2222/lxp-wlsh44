package com.lxp.common.validate;

import static com.lxp.exception.ErrorCode.*;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Assert {

	public static void notNull(Object obj) {
		if (obj == null) {
			throw new LxpException(INVALID_ARGUMENTS);
		}
	}

	public static void notNull(Object obj, ErrorCode errorCode) {
		if (obj == null) {
			throw new LxpException(errorCode);
		}
	}

	public static void notEmpty(String value) {
		notNull(value);
		if (value.isBlank()) {
			throw new LxpException(INVALID_INPUT);
		}
	}

	public static void notEmpty(String value, ErrorCode errorCode) {
		notNull(value, errorCode);
		if (value.isBlank()) {
			throw new LxpException(errorCode);
		}
	}

	public static void isTrue(boolean expression, ErrorCode errorCode) {
		if (!expression) {
			throw new LxpException(errorCode);
		}
	}
}
