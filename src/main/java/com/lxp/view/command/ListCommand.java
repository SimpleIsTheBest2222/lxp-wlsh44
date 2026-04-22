package com.lxp.view.command;

import java.util.Arrays;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ListCommand implements MenuCommand {

	SELECT(1, "선택"),
	BACK(2, "뒤로 가기");

	private final int value;
	private final String label;

	public static ListCommand from(int value) {
		return Arrays.stream(values())
			.filter(command -> command.value == value)
			.findFirst()
			.orElseThrow(() -> new LxpException(ErrorCode.INVALID_INPUT));
	}

	@Override
	public boolean isSelectable() {
		return this == SELECT;
	}
}
