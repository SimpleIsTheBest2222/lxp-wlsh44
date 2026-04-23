package com.lxp.view;

import java.util.List;
import java.util.stream.IntStream;

import com.lxp.controller.CourseController;
import com.lxp.controller.response.CourseListResponse;
import com.lxp.view.command.CourseSelectCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseSelectView implements MenuStrategy<CourseSelectCommand> {

	private final MenuRenderer menuRenderer;
	private final OutputView outputView;
	private final CourseController courseController;
	private final CourseDetailView courseDetailView;

	public void run() {
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		CourseListResponse response = courseController.findAll();
		return new MenuScreen("강의 선택", createBody(response), List.of());
	}

	@Override
	public CourseSelectCommand parse(int input) {
		return CourseSelectCommand.from(input);
	}

	@Override
	public boolean handle(CourseSelectCommand command) {
		if (command.isBack()) {
			return false;
		}
		courseDetailView.run(command.courseId());
		return false;
	}

	private String createBody(CourseListResponse response) {
		String baseBody = "%s%n%s";
		if (response.isEmpty()) {
			return baseBody.formatted(
				outputView.muted("  등록된 강의가 없습니다."),
				outputView.muted("  선택할 강의 id를 입력하세요. (0: 뒤로 가기)")
			);
		}
		return baseBody.formatted(
			IntStream.range(0, response.courses().size())
				.mapToObj(index -> "  %d. %s (id: %d, 강사: %s)".formatted(
					index + 1,
					response.courses().get(index).title(),
					response.courses().get(index).id(),
					response.courses().get(index).instructorName()
				))
				.reduce((left, right) -> left + System.lineSeparator() + right)
				.orElse(""),
			outputView.muted("  선택할 강의 id를 입력하세요. (0: 뒤로 가기)")
		);
	}
}
