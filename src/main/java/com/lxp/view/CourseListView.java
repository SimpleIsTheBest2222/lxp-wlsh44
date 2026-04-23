package com.lxp.view;

import java.util.List;
import java.util.stream.IntStream;

import com.lxp.controller.CourseController;
import com.lxp.controller.response.CourseListResponse;
import com.lxp.view.command.CourseListCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseListView implements MenuStrategy<CourseListCommand> {

	private final MenuRenderer menuRenderer;
	private final OutputView outputView;
	private final CourseController courseController;
	private final CourseSelectView courseSelectView;

	public void run() {
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		CourseListResponse response = courseController.findAll();
		return new MenuScreen(
			"강의 목록",
			createBody(response),
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
				courseSelectView.run();
			}
			case BACK -> {
				return false;
			}
		}
		return true;
	}

	private String createBody(CourseListResponse response) {
		if (response.isEmpty()) {
			return outputView.muted("  등록된 강의가 없습니다.");
		}
		return IntStream.range(0, response.courses().size())
			.mapToObj(index -> "  %d. %s (id: %d, 강사: %s)".formatted(
				index + 1,
				response.courses().get(index).title(),
				response.courses().get(index).id(),
				response.courses().get(index).instructorName()
			))
			.reduce((left, right) -> left + System.lineSeparator() + right)
			.orElse("");
	}
}
