package com.lxp.view;

import java.util.List;

import com.lxp.controller.InstructorController;
import com.lxp.controller.request.InstructorRegisterRequest;
import com.lxp.controller.response.InstructorRegisterResponse;
import com.lxp.view.command.InstructorCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorView implements MenuStrategy<InstructorCommand> {

	private final MenuRenderer menuRenderer;
	private final InputView inputView;
	private final OutputView outputView;
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
				registerInstructor();
			}
			case LIST -> {
				instructorListView.run();
			}
			case BACK -> {
				return false;
			}
		}
		return true;
	}

	private void registerInstructor() {
		outputView.printHeader("강사 등록");
		outputView.printBody("  강사 정보를 입력하세요.");

		outputView.printLabel("  이름  : ");
		String name = inputView.readLine();

		outputView.printLabel("  소개  : ");
		String introduction = inputView.readLine();

		outputView.printEmptyLine();
		outputView.printSectionLine();

		InstructorRegisterRequest request = new InstructorRegisterRequest(name, introduction);
		InstructorRegisterResponse response = instructorController.register(request);
		outputView.printSuccess("  강사가 등록되었습니다. id: " + response.id());
		outputView.printSectionLine();
	}
}
