package com.lxp.view;

import java.util.List;

import com.lxp.view.command.MainCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainView implements MenuStrategy<MainCommand> {

	private final MenuRenderer menuRenderer;
	private final CourseView courseView;
	private final InstructorView instructorView;

	public void run() {
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		return new MenuScreen("강의 관리 콘솔", "", List.of(MainCommand.values()));
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
