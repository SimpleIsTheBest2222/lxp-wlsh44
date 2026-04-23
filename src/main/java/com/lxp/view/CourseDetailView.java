package com.lxp.view;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import com.lxp.controller.CourseController;
import com.lxp.controller.request.CourseDeleteRequest;
import com.lxp.controller.request.CourseUpdateRequest;
import com.lxp.controller.response.CourseDeleteResponse;
import com.lxp.controller.response.CourseDetailResponse;
import com.lxp.controller.response.CourseUpdateResponse;
import com.lxp.view.command.CourseDetailCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseDetailView implements MenuStrategy<CourseDetailCommand> {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

	private final MenuRenderer menuRenderer;
	private final InputView inputView;
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
			case UPDATE -> updateCourse();
			case DELETE -> {
				deleteCourse();
				return false;
			}
			case SELECT_CONTENT -> outputView.printNotImplemented();
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

	private void updateCourse() {
		outputView.printHeader("강의 상세 정보 수정");
		outputView.printBody("  빈 값 입력 시 기존 값이 유지됩니다.");
		outputView.printSectionLine();
		outputView.printEmptyLine();

		outputView.printLabel("  강의 제목    : ");
		String title = inputView.readLine();

		outputView.printLabel("  가격         : ");
		String price = inputView.readLine();

		outputView.printLabel("  난이도       : ");
		String level = inputView.readLine();

		outputView.printLabel("  강의 설명    : ");
		String description = inputView.readLine();

		outputView.printEmptyLine();
		outputView.printSectionLine();

		CourseUpdateRequest request = new CourseUpdateRequest(
			courseId,
			title,
			description,
			price,
			level
		);
		CourseUpdateResponse response = courseController.update(request);
		outputView.printSuccess("  수정되었습니다. id: " + response.id());
		outputView.printSectionLine();
	}

	private void deleteCourse() {
		CourseDeleteRequest request = new CourseDeleteRequest(courseId);
		CourseDeleteResponse response = courseController.delete(request);
		outputView.printSectionLine();
		outputView.printSuccess("  삭제가 완료되었습니다. id: " + response.id());
		outputView.printSectionLine();
	}
}
