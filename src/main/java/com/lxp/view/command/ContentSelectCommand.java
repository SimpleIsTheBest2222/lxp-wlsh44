package com.lxp.view.command;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

public class ContentSelectCommand implements MenuCommand {

	private final int input;

	private ContentSelectCommand(int input) {
		this.input = input;
	}

	public static ContentSelectCommand from(int input) {
		if (input < 0) {
			throw new LxpException(ErrorCode.INVALID_INPUT);
		}
		return new ContentSelectCommand(input);
	}

	public boolean isBack() {
		return input == 0;
	}

	public Long contentId() {
		return (long)input;
	}

	@Override
	public int getValue() {
		return 0;
	}

	@Override
	public String getLabel() {
		return "뒤로 가기";
	}
}
