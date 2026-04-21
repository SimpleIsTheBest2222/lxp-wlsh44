package com.lms.exception;

public enum ErrorCode {
    INVALID_INPUT("잘못된 입력입니다."),
    INVALID_LEVEL("유효하지 않은 난이도입니다. (LOW/MIDDLE/HIGH)"),
    INVALID_CONTENT_TYPE("유효하지 않은 콘텐츠 타입입니다. (VIDEO/TEXT/FILE)"),
    NOT_FOUND_COURSE("강의를 찾을 수 없습니다."),
    NOT_FOUND_INSTRUCTOR("강사를 찾을 수 없습니다."),
    NOT_FOUND_CONTENT("콘텐츠를 찾을 수 없습니다.");

    private final String message;

    ErrorCode(String message) { this.message = message; }

    public String getMessage() { return message; }
}
