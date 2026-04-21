package com.lxp.exception;

public class LxpException extends RuntimeException {
	private final ErrorCode errorCode;

	public LxpException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
