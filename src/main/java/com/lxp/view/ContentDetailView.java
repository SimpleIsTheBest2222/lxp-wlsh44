package com.lxp.view;

import java.util.List;

import com.lxp.controller.ContentController;
import com.lxp.controller.request.ContentDeleteRequest;
import com.lxp.controller.request.ContentUpdateRequest;
import com.lxp.controller.response.ContentDeleteResponse;
import com.lxp.controller.response.ContentDetailResponse;
import com.lxp.controller.response.ContentUpdateResponse;
import com.lxp.view.command.ContentDetailCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentDetailView implements MenuStrategy<ContentDetailCommand> {

	private final MenuRenderer menuRenderer;
	private final InputView inputView;
	private final OutputView outputView;
	private final ContentController contentController;

	private Long contentId;

	public void run(Long contentId) {
		this.contentId = contentId;
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		ContentDetailResponse response = contentController.findDetailById(contentId);
		return new MenuScreen("콘텐츠 상세", createBody(response), List.of(ContentDetailCommand.values()));
	}

	@Override
	public ContentDetailCommand parse(int input) {
		return ContentDetailCommand.from(input);
	}

	@Override
	public boolean handle(ContentDetailCommand command) {
		switch (command) {
			case UPDATE -> updateContent();
			case DELETE -> {
				deleteContent();
				return false;
			}
			case BACK -> {
				return false;
			}
		}
		return true;
	}

	private String createBody(ContentDetailResponse response) {
		return String.join(System.lineSeparator(),
			"  콘텐츠 제목  : %s",
			"  콘텐츠 타입  : %s",
			"  콘텐츠 내용  : %s"
		).formatted(response.title(), response.contentType(), response.body());
	}

	private void updateContent() {
		outputView.printHeader("콘텐츠 수정");
		outputView.printBody("  빈 값 입력 시 기존 값이 유지됩니다.");
		outputView.printSectionLine();
		outputView.printEmptyLine();

		outputView.printLabel("  수정할 제목  : ");
		String title = inputView.readLine();

		outputView.printLabel("  수정할 내용  : ");
		String body = inputView.readLine();

		outputView.printEmptyLine();
		outputView.printSectionLine();

		ContentUpdateRequest request = new ContentUpdateRequest(contentId, title, body);
		ContentUpdateResponse response = contentController.update(request);
		outputView.printSuccess("  수정되었습니다. id: " + response.id());
		outputView.printSectionLine();
	}

	private void deleteContent() {
		ContentDeleteRequest request = new ContentDeleteRequest(contentId);
		ContentDeleteResponse response = contentController.delete(request);
		outputView.printSectionLine();
		outputView.printSuccess("  삭제가 완료되었습니다. id: " + response.id());
		outputView.printSectionLine();
	}
}
