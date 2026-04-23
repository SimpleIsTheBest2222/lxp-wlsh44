package com.lxp.view;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.controller.ContentController;
import com.lxp.controller.request.ContentDeleteRequest;
import com.lxp.controller.request.ContentUpdateRequest;
import com.lxp.controller.response.ContentDeleteResponse;
import com.lxp.controller.response.ContentDetailResponse;
import com.lxp.controller.response.ContentUpdateResponse;
import com.lxp.view.command.ContentDetailCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContentDetailView 테스트")
class ContentDetailViewTest {

	@Mock
	private MenuRenderer menuRenderer;

	@Mock
	private InputView inputView;

	@Mock
	private OutputView outputView;

	@Mock
	private ContentController contentController;

	@InjectMocks
	private ContentDetailView contentDetailView;

	@Test
	@DisplayName("성공 - 콘텐츠 상세 화면 본문을 출력한다")
	void screen() {
		contentDetailView.run(1L);
		when(contentController.findDetailById(1L))
			.thenReturn(new ContentDetailResponse(1L, "원시타입", "설명", "TEXT"));

		MenuScreen screen = contentDetailView.screen();

		assertThat(screen.title()).isEqualTo("콘텐츠 상세");
		assertThat(screen.body()).contains("콘텐츠 제목  : 원시타입", "콘텐츠 타입  : TEXT", "콘텐츠 내용  : 설명");
	}

	@Test
	@DisplayName("성공 - UPDATE 처리 시 입력을 받아 수정 후 성공 문구를 출력한다")
	void handle_update() {
		contentDetailView.run(1L);
		when(inputView.readLine()).thenReturn("Stream", "Stream 설명");
		when(contentController.update(new ContentUpdateRequest(1L, "Stream", "Stream 설명")))
			.thenReturn(new ContentUpdateResponse(1L));

		boolean result = contentDetailView.handle(ContentDetailCommand.UPDATE);

		verify(outputView).printHeader("콘텐츠 수정");
		verify(outputView).printLabel("  수정할 제목  : ");
		verify(outputView).printLabel("  수정할 내용  : ");
		verify(contentController).update(new ContentUpdateRequest(1L, "Stream", "Stream 설명"));
		verify(outputView).printSuccess("  수정되었습니다. id: 1");
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("성공 - DELETE 처리 시 삭제 후 화면을 종료한다")
	void handle_delete() {
		contentDetailView.run(1L);
		when(contentController.delete(new ContentDeleteRequest(1L))).thenReturn(new ContentDeleteResponse(1L));

		boolean result = contentDetailView.handle(ContentDetailCommand.DELETE);

		verify(contentController).delete(new ContentDeleteRequest(1L));
		verify(outputView).printSuccess("  삭제가 완료되었습니다. id: 1");
		assertThat(result).isFalse();
	}
}
