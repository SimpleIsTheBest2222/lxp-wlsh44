package com.lxp.view;

import java.util.List;
import java.util.stream.Collectors;

import com.lxp.controller.InstructorController;
import com.lxp.controller.response.InstructorListResponse;
import com.lxp.view.command.InstructorSelectCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorSelectView implements MenuStrategy<InstructorSelectCommand> {

	private final MenuRenderer menuRenderer;
	private final OutputView outputView;
	private final InstructorController instructorController;
	private final InstructorDetailView instructorDetailView;

	public void run() {
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		InstructorListResponse response = instructorController.findAll();
		return new MenuScreen("강사 선택", createBody(response), List.of());
	}

	@Override
	public InstructorSelectCommand parse(int input) {
		return InstructorSelectCommand.from(input);
	}

	@Override
	public boolean handle(InstructorSelectCommand command) {
		if (command.isBack()) {
			return false;
		}
		Long instructorId = instructorController.findById(command.instructorId()).id();
		instructorDetailView.run(instructorId);
		return false;
	}

	private String createBody(InstructorListResponse response) {
		if (response.isEmpty()) {
			return outputView.muted("  등록된 강사가 없습니다.");
		}
		return response.instructors()
			.stream()
			.map(instructor -> "  %d. %s (id: %d)".formatted(instructor.id(), instructor.name(), instructor.id()))
			.collect(Collectors.joining(System.lineSeparator()))
			+ System.lineSeparator()
			+ System.lineSeparator()
			+ outputView.muted("  선택할 강사 id를 입력하세요. (0: 뒤로 가기)");
	}
}
