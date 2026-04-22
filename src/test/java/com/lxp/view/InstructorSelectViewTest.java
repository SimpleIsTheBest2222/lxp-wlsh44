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
import com.lxp.view.command.InstructorSelectCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("InstructorSelectView 테스트")
class InstructorSelectViewTest {

	@Mock
	private MenuRenderer menuRenderer;

	@Mock
	private OutputView outputView;

	@Mock
	private InstructorController instructorController;

	@Mock
	private InstructorDetailView instructorDetailView;

	@InjectMocks
	private InstructorSelectView instructorSelectView;

	@Test
	@DisplayName("성공 - 등록된 강사가 있으면 id를 포함한 목록 본문을 출력한다")
	void screen_withInstructors() {
		// given
		InstructorListResponse response = new InstructorListResponse(List.of(
			new InstructorSummaryResponse(1L, "홍길동"),
			new InstructorSummaryResponse(2L, "김남준")
		));
		when(instructorController.findAll()).thenReturn(response);
		when(outputView.muted("  선택할 강사 id를 입력하세요. (0: 뒤로 가기)"))
			.thenReturn("  선택할 강사 id를 입력하세요. (0: 뒤로 가기)");

		// when
		MenuScreen screen = instructorSelectView.screen();

		// then
		assertThat(screen.title()).isEqualTo("강사 선택");
		assertThat(screen.body()).contains("1. 홍길동 (id: 1)", "2. 김남준 (id: 2)");
		assertThat(screen.body()).contains("선택할 강사 id를 입력하세요. (0: 뒤로 가기)");
	}

	@Test
	@DisplayName("성공 - 등록된 강사가 없으면 빈 상태 문구를 출력한다")
	void screen_empty() {
		// given
		when(instructorController.findAll()).thenReturn(new InstructorListResponse(List.of()));
		when(outputView.muted("  등록된 강사가 없습니다.")).thenReturn("muted");

		// when
		MenuScreen screen = instructorSelectView.screen();

		// then
		assertThat(screen.body()).startsWith("muted");
	}

	@Test
	@DisplayName("성공 - 강사 id를 입력하면 선택한 강사 id를 반환한다")
	void handle_select() {
		// given
		when(instructorController.findById(2L)).thenReturn(new InstructorSummaryResponse(2L, "김남준"));

		// when
		boolean result = instructorSelectView.handle(InstructorSelectCommand.from(2));

		// then
		verify(instructorController).findById(2L);
		verify(instructorDetailView).run(2L);
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("성공 - 0을 입력하면 현재 화면을 종료한다")
	void handle_back() {
		// when
		boolean result = instructorSelectView.handle(InstructorSelectCommand.from(0));

		// then
		verifyNoInteractions(instructorController);
		assertThat(result).isFalse();
	}
}
