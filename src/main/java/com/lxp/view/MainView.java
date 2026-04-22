package com.lxp.view;

import com.lxp.exception.LxpException;
import com.lxp.view.command.MainCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainView implements MenuStrategy<MainCommand> {

	private final MenuRunner menuRunner;
	private final CourseView courseView;
	private final InstructorView instructorView;

	public void run() {
		menuRunner.run(this);
	}

	@Override
	public String title() {
		return "강의 관리 콘솔";
	}

	@Override
	public String body() {
		return "";
	}

	@Override
	public MainCommand[] commands() {
		return MainCommand.values();
	}

	@Override
	public MainCommand parse(int input) {
		return MainCommand.from(input);
	}

	@Override
	public boolean handle(MainCommand command) {
		switch (command) {
			case COURSE_MANAGEMENT -> courseView.run();
			case INSTRUCTOR_MANAGEMENT -> instructorView.run();
			case EXIT -> {
				return false;
			}
		}
		return true;
	}
}
