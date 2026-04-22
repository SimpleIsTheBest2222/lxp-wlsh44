package com.lxp.view;

import java.util.List;

import com.lxp.controller.InstructorController;
import com.lxp.view.command.InstructorListCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorListView implements MenuStrategy<InstructorListCommand> {

	private final MenuRenderer menuRenderer;
	private final OutputView outputView;
	private final InstructorController instructorController;

	public void run() {
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		return new MenuScreen(
			"강사 목록",
			outputView.muted("  등록된 강사가 없습니다."),
			List.of(InstructorListCommand.values())
		);
	}

	@Override
	public InstructorListCommand parse(int input) {
		return InstructorListCommand.from(input);
	}

	@Override
	public boolean handle(InstructorListCommand command) {
		switch (command) {
			case SELECT -> {
				instructorController.findById();
				outputView.printNotImplemented();
			}
			case BACK -> {
				return false;
			}
		}
		return true;
	}
}
