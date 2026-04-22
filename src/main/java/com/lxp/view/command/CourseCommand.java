package com.lxp.view.command;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CourseCommand implements MenuCommand {

	REGISTER(1, "강의 등록"),
	LIST(2, "강의 조회"),
	BACK(3, "뒤로 가기");

	private final int value;
	private final String label;

	public static CourseCommand from(int value) {
		for (CourseCommand command : values()) {
			if (command.value == value) {
				return command;
			}
		}
		throw new LxpException(ErrorCode.INVALID_INPUT);
	}

}
