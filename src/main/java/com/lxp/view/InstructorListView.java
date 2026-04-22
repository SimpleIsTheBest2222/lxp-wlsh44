package com.lxp.view;

import com.lxp.controller.InstructorController;
import com.lxp.view.command.ListCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorListView implements MenuStrategy<ListCommand> {

	private final MenuRunner menuRunner;
	private final InstructorController instructorController;

	public void run() {
		menuRunner.run(this);
	}

	@Override
	public String title() {
		return "강사 목록";
	}

	@Override
	public String body() {
		return OutputView.GRAY + "  등록된 강사가 없습니다." + OutputView.RESET;
	}

	@Override
	public ListCommand[] commands() {
		return ListCommand.values();
	}

	@Override
	public ListCommand parse(int input) {
		return ListCommand.from(input);
	}

	@Override
	public boolean handle(ListCommand command) {
		switch (command) {
			case SELECT -> {
				instructorController.findById();
				OutputView.printNotImplemented();
			}
			case BACK -> {
				return false;
			}
		}
		return true;
	}

	@Override
	public String menuPrefix() {
		return "강사";
	}
}
