package com.lxp.view.command;

import java.util.Arrays;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentDetailCommand implements MenuCommand {

	UPDATE(1, "콘텐츠 수정"),
	DELETE(2, "콘텐츠 삭제"),
	BACK(3, "뒤로 가기");

	private final int value;
	private final String label;

	public static ContentDetailCommand from(int value) {
		return Arrays.stream(values())
			.filter(command -> command.value == value)
			.findFirst()
			.orElseThrow(() -> new LxpException(ErrorCode.INVALID_INPUT));
	}
}
