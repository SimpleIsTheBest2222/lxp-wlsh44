package com.lxp.view;

import java.util.List;
import java.util.stream.IntStream;

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
		String baseBody = "%s%n%s";
		if (response.isEmpty()) {
			return baseBody.formatted(
				outputView.muted("  등록된 강사가 없습니다."),
				outputView.muted("  선택할 강사 id를 입력하세요. (0: 뒤로 가기)")
			);
		}
		return baseBody.formatted(
			IntStream.range(0, response.instructors().size())
				.mapToObj(index -> "  %d. %s (id: %d)".formatted(
					index + 1,
					response.instructors().get(index).name(),
					response.instructors().get(index).id()
				))
				.reduce((left, right) -> left + System.lineSeparator() + right)
				.orElse(""),
			outputView.muted("  선택할 강사 id를 입력하세요. (0: 뒤로 가기)")
		);
	}
}
