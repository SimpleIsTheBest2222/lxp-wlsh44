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

import com.lxp.controller.InstructorController;
import com.lxp.controller.response.InstructorListResponse;
import com.lxp.controller.response.InstructorSummaryResponse;
import com.lxp.view.command.InstructorListCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("InstructorListView 테스트")
class InstructorListViewTest {

	@Mock
	private MenuRenderer menuRenderer;

	@Mock
	private OutputView outputView;

	@Mock
	private InstructorController instructorController;

	@Mock
	private InstructorSelectView instructorSelectView;

	@InjectMocks
	private InstructorListView instructorListView;

	@Test
	@DisplayName("성공 - 등록된 강사가 있으면 목록 본문에 이름들을 출력한다")
	void screen_withInstructors() {
		// given
		InstructorListResponse response = new InstructorListResponse(List.of(
			new InstructorSummaryResponse(1L, "홍길동"),
			new InstructorSummaryResponse(2L, "김남준")
		));
		when(instructorController.findAll()).thenReturn(response);

		// when
		MenuScreen screen = instructorListView.screen();

		// then
		assertThat(screen.body()).contains("1. 홍길동", "2. 김남준");
	}

	@Test
	@DisplayName("성공 - 등록된 강사가 없으면 빈 상태 문구를 출력한다")
	void screen_empty() {
		// given
		when(instructorController.findAll()).thenReturn(new InstructorListResponse(List.of()));
		when(outputView.muted("  등록된 강사가 없습니다.")).thenReturn("muted");

		// when
		MenuScreen screen = instructorListView.screen();

		// then
		assertThat(screen.body()).isEqualTo("muted");
	}

	@Test
	@DisplayName("성공 - SELECT 처리 시 강사 조회 후 안내 문구를 출력한다")
	void handle_select() {
		// when
		boolean result = instructorListView.handle(InstructorListCommand.SELECT);

		// then
		verify(instructorSelectView).run();
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("성공 - BACK 처리 시 현재 화면을 종료한다")
	void handle_back() {
		// when
		boolean result = instructorListView.handle(InstructorListCommand.BACK);

		// then
		verifyNoInteractions(instructorController, instructorSelectView);
		assertThat(result).isFalse();
	}
}
