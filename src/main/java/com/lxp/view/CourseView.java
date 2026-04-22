package com.lxp.view;

import java.util.List;

import com.lxp.controller.CourseController;
import com.lxp.view.command.CourseCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseView implements MenuStrategy<CourseCommand> {

	private final MenuRenderer menuRenderer;
	private final CourseController courseController;
	private final CourseListView courseListView;

	public void run() {
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		return new MenuScreen("강의 관리", "", List.of(CourseCommand.values()));
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
