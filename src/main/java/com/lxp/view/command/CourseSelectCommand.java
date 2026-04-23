package com.lxp.view.command;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

public class CourseSelectCommand implements MenuCommand {

	private final int input;

	private CourseSelectCommand(int input) {
		this.input = input;
	}

	public static CourseSelectCommand from(int input) {
		if (input < 0) {
			throw new LxpException(ErrorCode.INVALID_INPUT);
		}
		return new CourseSelectCommand(input);
	}

	public boolean isBack() {
		return input == 0;
	}

	public Long courseId() {
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
