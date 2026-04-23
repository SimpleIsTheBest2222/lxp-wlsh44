package com.lxp.view.command;

import java.util.Arrays;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CourseDetailCommand implements MenuCommand {

	UPDATE(1, "강의 수정"),
	DELETE(2, "강의 삭제"),
	SELECT_CONTENT(3, "콘텐츠 선택"),
	BACK(4, "뒤로 가기");

	private final int value;
	private final String label;

	public static CourseDetailCommand from(int value) {
		return Arrays.stream(values())
			.filter(command -> command.value == value)
			.findFirst()
			.orElseThrow(() -> new LxpException(ErrorCode.INVALID_INPUT));
	}
}
