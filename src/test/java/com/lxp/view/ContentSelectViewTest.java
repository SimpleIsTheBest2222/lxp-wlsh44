package com.lxp.view;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.controller.ContentController;
import com.lxp.controller.response.ContentListResponse;
import com.lxp.controller.response.ContentSummaryResponse;
import com.lxp.view.command.ContentSelectCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContentSelectView 테스트")
class ContentSelectViewTest {

	@Mock
	private MenuRenderer menuRenderer;

	@Mock
	private OutputView outputView;

	@Mock
	private ContentController contentController;

	@Mock
	private ContentDetailView contentDetailView;

	@InjectMocks
	private ContentSelectView contentSelectView;

	@Test
	@DisplayName("성공 - 콘텐츠 목록 본문을 출력한다")
	void screen_withContents() {
		contentSelectView.run(1L);
		when(contentController.findAllByCourseId(1L)).thenReturn(new ContentListResponse(List.of(
			new ContentSummaryResponse(1L, "원시타입"),
			new ContentSummaryResponse(2L, "for 문")
		)));
		when(outputView.muted("  선택할 콘텐츠 id를 입력하세요. (0: 뒤로 가기)")).thenReturn("guide");

		MenuScreen screen = contentSelectView.screen();

		assertThat(screen.body()).contains("1. 원시타입", "id: 1", "2. for 문", "guide");
	}

	@Test
	@DisplayName("성공 - 등록된 콘텐츠가 없으면 빈 상태 문구를 출력한다")
	void screen_empty() {
		contentSelectView.run(1L);
		when(contentController.findAllByCourseId(1L)).thenReturn(new ContentListResponse(List.of()));
		when(outputView.muted("  등록된 콘텐츠가 없습니다.")).thenReturn("empty");
		when(outputView.muted("  선택할 콘텐츠 id를 입력하세요. (0: 뒤로 가기)")).thenReturn("guide");

		MenuScreen screen = contentSelectView.screen();

		assertThat(screen.body()).isEqualTo("empty\nguide");
	}

	@Test
	@DisplayName("성공 - 콘텐츠를 선택하면 상세 화면으로 이동한다")
	void handle_select() {
		boolean result = contentSelectView.handle(ContentSelectCommand.from(2));

		verify(contentDetailView).run(2L);
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("성공 - 0 입력 시 이전 화면으로 돌아간다")
	void handle_back() {
		boolean result = contentSelectView.handle(ContentSelectCommand.from(0));

		verifyNoInteractions(contentDetailView);
		assertThat(result).isFalse();
	}
}
