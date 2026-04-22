package com.lxp.view;

import com.lxp.controller.CourseController;
import com.lxp.view.command.CourseCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseView implements MenuStrategy<CourseCommand> {

	private final MenuRunner menuRunner;
	private final CourseController courseController;
	private final CourseListView courseListView;

	public void run() {
		menuRunner.run(this);
	}

	@Override
	public String title() {
		return "강의 관리";
	}

	@Override
	public String body() {
		return "";
	}

	@Override
	public CourseCommand[] commands() {
		return CourseCommand.values();
	}

	@Override
	public CourseCommand parse(int input) {
		return CourseCommand.from(input);
	}

	@Override
	public boolean handle(CourseCommand command) {
		switch (command) {
			case REGISTER -> {
				courseController.register();
				OutputView.printNotImplemented();
			}
			case LIST -> {
				courseController.findAll();
				courseListView.run();
			}
			case BACK -> {
				return false;
			}
		}
		return true;
	}
}
