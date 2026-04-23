package com.lxp.view;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import com.lxp.controller.CourseController;
import com.lxp.controller.response.CourseDetailResponse;
import com.lxp.view.command.CourseDetailCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseDetailView implements MenuStrategy<CourseDetailCommand> {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

	private final MenuRenderer menuRenderer;
	private final OutputView outputView;
	private final CourseController courseController;

	private Long courseId;

	public void run(Long courseId) {
		this.courseId = courseId;
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		CourseDetailResponse response = courseController.findDetailById(courseId);
		return new MenuScreen("강의 상세", createBody(response), List.of(CourseDetailCommand.values()));
	}

	@Override
	public CourseDetailCommand parse(int input) {
		return CourseDetailCommand.from(input);
	}

	@Override
	public boolean handle(CourseDetailCommand command) {
		switch (command) {
			case UPDATE, DELETE, SELECT_CONTENT -> outputView.printNotImplemented();
			case BACK -> {
				return false;
			}
		}
		return true;
	}

	private String createBody(CourseDetailResponse response) {
		String line = "-".repeat(60);
		return String.join(System.lineSeparator(),
			"  강의 id      : %d",
			"  강의 제목    : %s",
			"  강사         : %s",
			"  가격         : %,d원",
			"  난이도       : %s",
			"  강의 설명    : %s",
			"",
			line,
			"  강사 소개",
			line,
			"  강사         : %s",
			"  강사 소개    : %s",
			"",
			line,
			"  콘텐츠 목록",
			line,
			"",
			"%s",
			"",
			line,
			"  게시일         : %s",
			"  마지막 수정일  : %s"
		).formatted(
			response.id(),
			response.title(),
			response.instructorName(),
			response.price(),
			response.level(),
			response.description(),
			response.instructorName(),
			response.instructorIntroduction(),
			createContentBody(response),
			response.createdAt().format(DATE_FORMATTER),
			response.modifiedAt().format(DATE_FORMATTER)
		);
	}

	private String createContentBody(CourseDetailResponse response) {
		return IntStream.range(0, response.contents().size())
			.mapToObj(index -> "  %d. %s".formatted(index + 1, response.contents().get(index).title()))
			.reduce((left, right) -> left + System.lineSeparator() + right)
			.orElse(outputView.muted("  등록된 콘텐츠가 없습니다."));
	}
}
