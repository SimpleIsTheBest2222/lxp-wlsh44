package com.lxp.view.command;

import java.util.Arrays;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InstructorDetailCommand implements MenuCommand {

	UPDATE(1, "강사 수정"),
	DELETE(2, "강사 삭제"),
	BACK(3, "뒤로 가기");

	private final int value;
	private final String label;

	public static InstructorDetailCommand from(int value) {
		return Arrays.stream(values())
			.filter(command -> command.value == value)
			.findFirst()
			.orElseThrow(() -> new LxpException(ErrorCode.INVALID_INPUT));
	}
}
