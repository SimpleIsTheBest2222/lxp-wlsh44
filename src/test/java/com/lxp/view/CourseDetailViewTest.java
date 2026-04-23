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

import com.lxp.controller.ContentController;
import com.lxp.controller.CourseController;
import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.request.CourseDeleteRequest;
import com.lxp.controller.request.CourseUpdateRequest;
import com.lxp.controller.response.ContentRegisterResponse;
import com.lxp.controller.response.ContentSummaryResponse;
import com.lxp.controller.response.CourseDeleteResponse;
import com.lxp.controller.response.CourseDetailResponse;
import com.lxp.controller.response.CourseUpdateResponse;
import com.lxp.view.command.CourseDetailCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseDetailView 테스트")
class CourseDetailViewTest {

	@Mock
	private MenuRenderer menuRenderer;

	@Mock
	private InputView inputView;

	@Mock
	private OutputView outputView;

	@Mock
	private CourseController courseController;

	@Mock
	private ContentController contentController;

	@Mock
	private ContentSelectView contentSelectView;

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
	@DisplayName("성공 - UPDATE 처리 시 입력을 받아 수정 후 성공 문구를 출력한다")
	void handle_update() {
		courseDetailView.run(1L);
		when(inputView.readLine()).thenReturn("Java 심화", "120000", "HIGH", "심화 문법");
		when(courseController.update(new CourseUpdateRequest(1L, "Java 심화", "심화 문법", "120000", "HIGH")))
			.thenReturn(new CourseUpdateResponse(1L));

		boolean result = courseDetailView.handle(CourseDetailCommand.UPDATE);

		verify(outputView).printHeader("강의 상세 정보 수정");
		verify(outputView).printBody("  빈 값 입력 시 기존 값이 유지됩니다.");
		verify(outputView).printLabel("  강의 제목    : ");
		verify(outputView).printLabel("  가격         : ");
		verify(outputView).printLabel("  난이도       : ");
		verify(outputView).printLabel("  강의 설명    : ");
		verify(courseController).update(new CourseUpdateRequest(1L, "Java 심화", "심화 문법", "120000", "HIGH"));
		verify(outputView).printSuccess("  수정되었습니다. id: 1");
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("성공 - DELETE 처리 시 삭제 후 화면을 종료한다")
	void handle_delete() {
		courseDetailView.run(1L);
		when(courseController.delete(new CourseDeleteRequest(1L))).thenReturn(new CourseDeleteResponse(1L));

		boolean result = courseDetailView.handle(CourseDetailCommand.DELETE);

		verify(courseController).delete(new CourseDeleteRequest(1L));
		verify(outputView).printSuccess("  삭제가 완료되었습니다. id: 1");
		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("성공 - REGISTER_CONTENT 처리 시 입력을 받아 콘텐츠 등록 후 성공 문구를 출력한다")
	void handle_registerContent() {
		courseDetailView.run(1L);
		when(inputView.readLine()).thenReturn("Stream", "TEXT", "Stream API 설명");
		when(contentController.register(new ContentRegisterRequest(1L, "Stream", "Stream API 설명", "TEXT")))
			.thenReturn(new ContentRegisterResponse(3L));

		boolean result = courseDetailView.handle(CourseDetailCommand.REGISTER_CONTENT);

		verify(outputView).printHeader("콘텐츠 등록");
		verify(outputView).printLabel("  콘텐츠 제목  : ");
		verify(outputView).printLabel("  콘텐츠 타입(VIDEO/TEXT/FILE)  : ");
		verify(outputView).printLabel("  콘텐츠 내용  : ");
		verify(contentController).register(new ContentRegisterRequest(1L, "Stream", "Stream API 설명", "TEXT"));
		verify(outputView).printSuccess("  콘텐츠가 등록되었습니다. id: 3");
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("성공 - SELECT_CONTENT 처리 시 콘텐츠 선택 화면으로 이동한다")
	void handle_selectContent() {
		courseDetailView.run(1L);

		boolean result = courseDetailView.handle(CourseDetailCommand.SELECT_CONTENT);

		verify(contentSelectView).run(1L);
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
