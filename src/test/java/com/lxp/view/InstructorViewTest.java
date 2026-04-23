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
import com.lxp.controller.request.InstructorRegisterRequest;
import com.lxp.controller.response.InstructorRegisterResponse;
import com.lxp.view.command.InstructorCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("InstructorView 테스트")
class InstructorViewTest {

	@Mock
	private MenuRenderer menuRenderer;

	@Mock
	private InputView inputView;

	@Mock
	private OutputView outputView;

	@Mock
	private InstructorController instructorController;

	@Mock
	private InstructorListView instructorListView;

	@InjectMocks
	private InstructorView instructorView;

	@Test
	@DisplayName("성공 - REGISTER 처리 시 입력을 받아 등록 후 성공 문구를 출력한다")
	void handle_register() {
		// given
		when(inputView.readLine()).thenReturn("김남준", "자바 강사");
		when(instructorController.register(new InstructorRegisterRequest("김남준", "자바 강사")))
			.thenReturn(new InstructorRegisterResponse(2L));

		// when
		boolean result = instructorView.handle(InstructorCommand.REGISTER);

		// then
		verify(outputView).printHeader("강사 등록");
		verify(outputView).printBody("  강사 정보를 입력하세요.");
		verify(outputView).printLabel("  이름  : ");
		verify(outputView).printLabel("  소개  : ");
		verify(instructorController).register(new InstructorRegisterRequest("김남준", "자바 강사"));
		verify(outputView).printSuccess("  강사가 등록되었습니다. id: 2");
		verifyNoInteractions(instructorListView);
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("성공 - LIST 처리 시 목록 화면으로 이동한다")
	void handle_list() {
		// when
		boolean result = instructorView.handle(InstructorCommand.LIST);

		// then
		verify(instructorListView).run();
		verifyNoInteractions(instructorController);
		assertThat(result).isTrue();
	}
}
