package com.lxp.view.command;

import java.util.Arrays;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MainCommand implements MenuCommand {

	COURSE_MANAGEMENT(1, "강의 관리"),
	INSTRUCTOR_MANAGEMENT(2, "강사 관리"),
	EXIT(3, "종료");

	private final int value;
	private final String label;

	public static MainCommand from(int value) {
		return Arrays.stream(values())
			.filter(command -> command.value == value)
			.findFirst()
			.orElseThrow(() -> new LxpException(ErrorCode.INVALID_INPUT));
	}

}
