package com.lxp.exception;

import java.util.Objects;

public class LxpException extends RuntimeException {
	private final ErrorCode errorCode;

	public LxpException(ErrorCode errorCode) {
		super(Objects.requireNonNull(errorCode, "ErrorCode는 null일 수 없습니다.").getMessage());
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
