package com.lxp.view;

import java.util.List;
import java.util.stream.Collectors;

import com.lxp.controller.InstructorController;
import com.lxp.controller.response.InstructorListResponse;
import com.lxp.view.command.InstructorListCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorListView implements MenuStrategy<InstructorListCommand> {

	private final MenuRenderer menuRenderer;
	private final OutputView outputView;
	private final InstructorController instructorController;
	private final InstructorSelectView instructorSelectView;

	public void run() {
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		InstructorListResponse response = instructorController.findAll();
		return new MenuScreen(
			"강사 목록",
			createBody(response),
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
				instructorSelectView.run();
			}
			case BACK -> {
				return false;
			}
		}
		return true;
	}

	private String createBody(InstructorListResponse response) {
		if (response.isEmpty()) {
			return outputView.muted("  등록된 강사가 없습니다.");
		}
		return response.instructors().stream()
			.map(instructor -> "  %d. %s".formatted(instructor.id(), instructor.name()))
			.collect(Collectors.joining(System.lineSeparator()));
	}
}
