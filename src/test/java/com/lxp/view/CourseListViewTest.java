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

import com.lxp.controller.CourseController;
import com.lxp.controller.response.CourseListResponse;
import com.lxp.controller.response.CourseSummaryResponse;
import com.lxp.view.command.CourseListCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseListView 테스트")
class CourseListViewTest {

	@Mock
	private MenuRenderer menuRenderer;

	@Mock
	private OutputView outputView;

	@Mock
	private CourseController courseController;

	@Mock
	private CourseSelectView courseSelectView;

	@InjectMocks
	private CourseListView courseListView;

	@Test
	@DisplayName("성공 - 등록된 강의가 없으면 빈 상태 문구를 출력한다")
	void screen_empty() {
		when(courseController.findAll()).thenReturn(new CourseListResponse(List.of()));
		when(outputView.muted("  등록된 강의가 없습니다.")).thenReturn("muted");

		MenuScreen screen = courseListView.screen();

		assertThat(screen.body()).isEqualTo("muted");
	}

	@Test
	@DisplayName("성공 - SELECT 처리 시 강의 선택 화면으로 이동한다")
	void handle_select() {
		boolean result = courseListView.handle(CourseListCommand.SELECT);

		verify(courseSelectView).run();
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("성공 - BACK 처리 시 현재 화면을 종료한다")
	void handle_back() {
		boolean result = courseListView.handle(CourseListCommand.BACK);

		verifyNoInteractions(courseController, courseSelectView);
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("성공 - 등록된 강의가 있으면 목록 본문에 강의와 강사를 출력한다")
	void screen_withCourses() {
		when(courseController.findAll()).thenReturn(new CourseListResponse(List.of(
			new CourseSummaryResponse(1L, "Java 입문", "김남준"),
			new CourseSummaryResponse(2L, "스프링 실전", "홍길동")
		)));

		MenuScreen screen = courseListView.screen();

		assertThat(screen.body()).contains(
			"1. Java 입문",
			"id: 1",
			"강사: 김남준",
			"2. 스프링 실전",
			"id: 2",
			"강사: 홍길동"
		);
	}
}
