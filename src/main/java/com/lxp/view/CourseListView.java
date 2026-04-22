package com.lxp.view;

import java.util.List;

import com.lxp.controller.CourseController;
import com.lxp.view.command.CourseListCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseListView implements MenuStrategy<CourseListCommand> {

	private final MenuRenderer menuRenderer;
	private final CourseController courseController;

	public void run() {
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		return new MenuScreen(
			"강의 목록",
			OutputView.GRAY + "  등록된 강의가 없습니다." + OutputView.RESET,
			List.of(CourseListCommand.values())
		);
	}

	@Override
	public CourseListCommand parse(int input) {
		return CourseListCommand.from(input);
	}

	@Override
	public boolean handle(CourseListCommand command) {
		switch (command) {
			case SELECT -> {
				courseController.findById();
				OutputView.printNotImplemented();
			}
			case BACK -> {
				return false;
			}
		}
		return true;
	}
}
