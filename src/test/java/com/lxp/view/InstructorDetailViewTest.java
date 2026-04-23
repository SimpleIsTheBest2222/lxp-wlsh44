package com.lxp.view;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.controller.InstructorController;
import com.lxp.controller.request.InstructorDeleteRequest;
import com.lxp.controller.request.InstructorUpdateRequest;
import com.lxp.controller.response.InstructorDeleteResponse;
import com.lxp.controller.response.InstructorDetailResponse;
import com.lxp.controller.response.InstructorUpdateResponse;
import com.lxp.view.command.InstructorDetailCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("InstructorDetailView 테스트")
class InstructorDetailViewTest {

	@Mock
	private MenuRenderer menuRenderer;

	@Mock
	private InputView inputView;

	@Mock
	private OutputView outputView;

	@Mock
	private InstructorController instructorController;

	@InjectMocks
	private InstructorDetailView instructorDetailView;

	@Test
	@DisplayName("성공 - 강사 상세 화면 본문을 출력한다")
	void screen() {
		// given
		instructorDetailView.run(2L);
		when(instructorController.findDetailById(2L))
			.thenReturn(new InstructorDetailResponse(2L, "김남준", "자바 강사"));

		// when
		MenuScreen screen = instructorDetailView.screen();

		// then
		assertThat(screen.title()).isEqualTo("강사 상세");
		assertThat(screen.body()).contains("강사 id  : 2", "이름     : 김남준", "소개     : 자바 강사");
	}

	@Test
	@DisplayName("성공 - UPDATE 처리 시 수정 후 성공 문구를 출력한다")
	void handle_update() {
		// given
		instructorDetailView.run(2L);
		when(inputView.readLine()).thenReturn("김남준 교수", "스프링 강사");
		when(instructorController.update(new InstructorUpdateRequest(2L, "김남준 교수", "스프링 강사")))
			.thenReturn(new InstructorUpdateResponse(2L));

		// when
		boolean result = instructorDetailView.handle(InstructorDetailCommand.UPDATE);

		// then
		verify(outputView).printHeader("강사 수정");
		verify(outputView).printBody("  빈 값 입력 시 기존 값이 유지됩니다.");
		verify(outputView).printLabel("  이름    : ");
		verify(outputView).printLabel("  소개    : ");
		verify(instructorController).update(new InstructorUpdateRequest(2L, "김남준 교수", "스프링 강사"));
		verify(outputView).printSuccess("  수정되었습니다. id: 2");
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("성공 - BACK 처리 시 현재 화면을 종료한다")
	void handle_back() {
		// when
		boolean result = instructorDetailView.handle(InstructorDetailCommand.BACK);

		// then
		verifyNoInteractions(outputView);
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("성공 - DELETE 처리 시 삭제 후 화면을 종료한다")
	void handle_delete() {
		// given
		instructorDetailView.run(2L);
		when(instructorController.delete(new InstructorDeleteRequest(2L)))
			.thenReturn(new InstructorDeleteResponse(2L));

		// when
		boolean result = instructorDetailView.handle(InstructorDetailCommand.DELETE);

		// then
		verify(instructorController).delete(new InstructorDeleteRequest(2L));
		verify(outputView).printSuccess("  삭제가 완료되었습니다. id: 2");
		assertThat(result).isFalse();
	}
}
