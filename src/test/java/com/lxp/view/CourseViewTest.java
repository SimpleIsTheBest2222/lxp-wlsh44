package com.lxp.view;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.controller.CourseController;
import com.lxp.view.command.CourseCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseView 테스트")
class CourseViewTest {

	@Mock
	private MenuRenderer menuRenderer;

	@Mock
	private OutputView outputView;

	@Mock
	private CourseController courseController;

	@Mock
	private CourseListView courseListView;

	@InjectMocks
	private CourseView courseView;

	@Test
	@DisplayName("성공 - REGISTER 처리 시 컨트롤러 호출 후 안내 문구를 출력한다")
	void handle_register() {
		// when
		boolean result = courseView.handle(CourseCommand.REGISTER);

		// then
		verify(courseController).register();
		verify(outputView).printNotImplemented();
		verifyNoInteractions(courseListView);
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("성공 - LIST 처리 시 전체 조회 후 목록 화면으로 이동한다")
	void handle_list() {
		// when
		boolean result = courseView.handle(CourseCommand.LIST);

		// then
		verify(courseController).findAll();
		verify(courseListView).run();
		assertThat(result).isTrue();
	}
}
