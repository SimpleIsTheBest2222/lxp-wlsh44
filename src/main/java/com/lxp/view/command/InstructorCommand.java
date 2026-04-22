package com.lxp.view.command;

import java.util.Arrays;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InstructorCommand implements MenuCommand {

	REGISTER(1, "강사 등록"),
	LIST(2, "강사 조회"),
	BACK(3, "뒤로 가기");

	private final int value;
	private final String label;

	public static InstructorCommand from(int value) {
		return Arrays.stream(values())
			.filter(command -> command.value == value)
			.findFirst()
			.orElseThrow(() -> new LxpException(ErrorCode.INVALID_INPUT));
	}

}
