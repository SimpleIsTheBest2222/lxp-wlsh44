package com.lxp.view;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.controller.CourseController;
import com.lxp.controller.response.ContentSummaryResponse;
import com.lxp.controller.response.CourseDetailResponse;
import com.lxp.view.command.CourseDetailCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseDetailView 테스트")
class CourseDetailViewTest {

	@Mock
	private MenuRenderer menuRenderer;

	@Mock
	private OutputView outputView;

	@Mock
	private CourseController courseController;

	@InjectMocks
	private CourseDetailView courseDetailView;

	@Test
	@DisplayName("성공 - 강의 상세 화면 본문에 강사 정보와 콘텐츠 목록을 출력한다")
	void screen() {
		courseDetailView.run(1L);
		when(courseController.findDetailById(1L)).thenReturn(new CourseDetailResponse(
			1L,
			"자바의 정석",
			"김남준",
			"10년 경력의 자바 전문 강사입니다.",
			100000,
			"입문",
			"자바 기초부터 심화까지 다루는 강의입니다.",
			List.of(
				new ContentSummaryResponse(1L, "원시타입"),
				new ContentSummaryResponse(2L, "for 문")
			),
			LocalDateTime.of(2025, 3, 10, 0, 0),
			LocalDateTime.of(2025, 4, 1, 0, 0)
		));

		MenuScreen screen = courseDetailView.screen();

		assertThat(screen.title()).isEqualTo("강의 상세");
		assertThat(screen.body()).contains(
			"강의 id      : 1",
			"강의 제목    : 자바의 정석",
			"강사         : 김남준",
			"강사 소개    : 10년 경력의 자바 전문 강사입니다.",
			"1. 원시타입",
			"2. for 문",
			"게시일         : 2025.03.10",
			"마지막 수정일  : 2025.04.01"
		);
	}

	@Test
	@DisplayName("성공 - 미구현 메뉴 선택 시 안내 문구를 출력한다")
	void handle_notImplemented() {
		boolean result = courseDetailView.handle(CourseDetailCommand.UPDATE);

		verify(outputView).printNotImplemented();
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("성공 - BACK 처리 시 현재 화면을 종료한다")
	void handle_back() {
		boolean result = courseDetailView.handle(CourseDetailCommand.BACK);

		verifyNoInteractions(outputView);
		assertThat(result).isFalse();
	}
}
