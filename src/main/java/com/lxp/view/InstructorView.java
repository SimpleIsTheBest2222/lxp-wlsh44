package com.lxp.view;

import com.lxp.controller.InstructorController;
import com.lxp.view.command.InstructorCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorView implements MenuStrategy<InstructorCommand> {

	private final MenuRunner menuRunner;
	private final InstructorController instructorController;
	private final InstructorListView instructorListView;

	public void run() {
		menuRunner.run(this);
	}

	@Override
	public String title() {
		return "강사 관리";
	}

	@Override
	public String body() {
		return "";
	}

	@Override
	public InstructorCommand[] commands() {
		return InstructorCommand.values();
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
