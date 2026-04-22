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

	@InjectMocks
	private InstructorListView instructorListView;

	@Test
	@DisplayName("성공 - SELECT 처리 시 강사 조회 후 안내 문구를 출력한다")
	void handle_select() {
		// when
		boolean result = instructorListView.handle(InstructorListCommand.SELECT);

		// then
		verify(instructorController).findById();
		verify(outputView).printNotImplemented();
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("성공 - BACK 처리 시 현재 화면을 종료한다")
	void handle_back() {
		// when
		boolean result = instructorListView.handle(InstructorListCommand.BACK);

		// then
		verifyNoInteractions(instructorController);
		assertThat(result).isFalse();
	}
}
