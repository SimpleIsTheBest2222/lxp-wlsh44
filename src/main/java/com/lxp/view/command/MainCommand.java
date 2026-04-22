package com.lxp.view.command;

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
		for (MainCommand command : values()) {
			if (command.value == value) {
				return command;
			}
		}
		throw new LxpException(ErrorCode.INVALID_INPUT);
	}

}
