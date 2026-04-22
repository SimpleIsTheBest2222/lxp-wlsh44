package com.lxp.view;

import java.util.List;

import com.lxp.controller.InstructorController;
import com.lxp.view.command.InstructorCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorView implements MenuStrategy<InstructorCommand> {

	private final MenuRenderer menuRenderer;
	private final InstructorController instructorController;
	private final InstructorListView instructorListView;

	public void run() {
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		return new MenuScreen("강사 관리", "", List.of(InstructorCommand.values()));
	}

	@Override
	public InstructorCommand parse(int input) {
		return InstructorCommand.from(input);
	}

	@Override
	public boolean handle(InstructorCommand command) {
		switch (command) {
			case REGISTER -> {
				instructorController.register();
				OutputView.printNotImplemented();
			}
			case LIST -> {
				instructorController.findAll();
				instructorListView.run();
			}
			case BACK -> {
				return false;
			}
		}
		return true;
	}
}
