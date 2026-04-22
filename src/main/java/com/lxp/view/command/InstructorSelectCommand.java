package com.lxp.view.command;

public class InstructorSelectCommand implements MenuCommand {

	private final int input;

	private InstructorSelectCommand(int input) {
		this.input = input;
	}

	public static InstructorSelectCommand from(int input) {
		return new InstructorSelectCommand(input);
	}

	public boolean isBack() {
		return input == 0;
	}

	public Long instructorId() {
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
