package com.lms.exception;

public class LmsException extends RuntimeException {
    private final ErrorCode errorCode;

    public LmsException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() { return errorCode; }
}
