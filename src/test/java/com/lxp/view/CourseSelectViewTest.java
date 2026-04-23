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
import com.lxp.view.command.CourseSelectCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseSelectView 테스트")
class CourseSelectViewTest {

	@Mock
	private MenuRenderer menuRenderer;

	@Mock
	private OutputView outputView;

	@Mock
	private CourseController courseController;

	@Mock
	private CourseDetailView courseDetailView;

	@InjectMocks
	private CourseSelectView courseSelectView;

	@Test
	@DisplayName("성공 - 등록된 강의가 있으면 목록 본문에 강의와 강사를 출력한다")
	void screen_withCourses() {
		CourseListResponse response = new CourseListResponse(List.of(
			new CourseSummaryResponse(1L, "Java 입문", "홍길동"),
			new CourseSummaryResponse(2L, "스프링 실전", "김남준")
		));
		when(courseController.findAll()).thenReturn(response);
		when(outputView.muted("  선택할 강의 id를 입력하세요. (0: 뒤로 가기)")).thenReturn("guide");

		MenuScreen screen = courseSelectView.screen();

		assertThat(screen.body()).contains("1. Java 입문", "강사: 홍길동", "2. 스프링 실전", "guide");
	}

	@Test
	@DisplayName("성공 - 등록된 강의가 없으면 빈 상태 문구를 출력한다")
	void screen_empty() {
		when(courseController.findAll()).thenReturn(new CourseListResponse(List.of()));
		when(outputView.muted("  등록된 강의가 없습니다.")).thenReturn("empty");
		when(outputView.muted("  선택할 강의 id를 입력하세요. (0: 뒤로 가기)")).thenReturn("guide");

		MenuScreen screen = courseSelectView.screen();

		assertThat(screen.body()).isEqualTo("empty\nguide");
	}

	@Test
	@DisplayName("성공 - 강의를 선택하면 상세 화면으로 이동한다")
	void handle_select() {
		boolean result = courseSelectView.handle(CourseSelectCommand.from(2));

		verify(courseDetailView).run(2L);
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("성공 - 0 입력 시 이전 화면으로 돌아간다")
	void handle_back() {
		boolean result = courseSelectView.handle(CourseSelectCommand.from(0));

		verifyNoInteractions(courseDetailView);
		assertThat(result).isFalse();
	}
}
