package com.lxp.exception;

public enum ErrorCode {
	INVALID_ARGUMENTS("잘못된 인자입니다."),

	INVALID_INPUT("잘못된 입력입니다."),

	// Course
	NOT_FOUND_COURSE("강의를 찾을 수 없습니다."),

	// Content
	INVALID_LEVEL("유효하지 않은 난이도입니다. (LOW/MIDDLE/HIGH)"),
	INVALID_CONTENT_TYPE("유효하지 않은 콘텐츠 타입입니다. (VIDEO/TEXT/FILE)"),
	NOT_FOUND_CONTENT("콘텐츠를 찾을 수 없습니다."),

	// Instructor
	NOT_FOUND_INSTRUCTOR("강사를 찾을 수 없습니다."),
	INSTRUCTOR_NAME_OUT_OF_RANGE("강사 이름의 길이가 범위를 벗어났습니다."),
	INSTRUCTOR_INTRODUCTION_OUT_OF_RANGE("강사 소개의 길이가 범위를 벗어났습니다."),

	// Course
	NOT_FOUND_COURSE_LEVEL("유효하지 않은 난이도입니다. (LOW/MIDDLE/HIGH)"),
	COURSE_TITLE_OUT_OF_RANGE("강의 제목의 길이가 범위를 벗어났습니다."),
	COURSE_DESCRIPTION_OUT_OF_RANGE("강의 설명의 길이가 범위를 벗어났습니다."),
	COURSE_PRICE_NEGATIVE("강의 가격은 0 이상이어야 합니다."),

	// Content
	NOT_FOUND_CONTENT_TYPE("유효하지 않은 콘텐츠 타입입니다. (VIDEO/TEXT/FILE)"),
	CONTENT_TITLE_OUT_OF_RANGE("콘텐츠 제목의 길이가 범위를 벗어났습니다."),
	CONTENT_BODY_OUT_OF_RANGE("콘텐츠 내용의 길이가 범위를 벗어났습니다.");

	private final String message;

	ErrorCode(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
