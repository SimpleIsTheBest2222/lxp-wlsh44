package com.lxp.view;

import java.util.List;
import java.util.stream.IntStream;

import com.lxp.controller.ContentController;
import com.lxp.controller.response.ContentListResponse;
import com.lxp.view.command.ContentSelectCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentSelectView implements MenuStrategy<ContentSelectCommand> {

	private final MenuRenderer menuRenderer;
	private final OutputView outputView;
	private final ContentController contentController;
	private final ContentDetailView contentDetailView;

	private Long courseId;

	public void run(Long courseId) {
		this.courseId = courseId;
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		ContentListResponse response = contentController.findAllByCourseId(courseId);
		return new MenuScreen("콘텐츠 선택", createBody(response), List.of());
	}

	@Override
	public ContentSelectCommand parse(int input) {
		return ContentSelectCommand.from(input);
	}

	@Override
	public boolean handle(ContentSelectCommand command) {
		if (command.isBack()) {
			return false;
		}
		contentDetailView.run(command.contentId());
		return false;
	}

	private String createBody(ContentListResponse response) {
		String baseBody = "%s%n%s";
		if (response.isEmpty()) {
			return baseBody.formatted(
				outputView.muted("  등록된 콘텐츠가 없습니다."),
				outputView.muted("  선택할 콘텐츠 id를 입력하세요. (0: 뒤로 가기)")
			);
		}
		return baseBody.formatted(
			IntStream.range(0, response.contents().size())
				.mapToObj(index -> "  %d. %s (id: %d)".formatted(
					index + 1,
					response.contents().get(index).title(),
					response.contents().get(index).id()
				))
				.reduce((left, right) -> left + System.lineSeparator() + right)
				.orElse(""),
			outputView.muted("  선택할 콘텐츠 id를 입력하세요. (0: 뒤로 가기)")
		);
	}
}
