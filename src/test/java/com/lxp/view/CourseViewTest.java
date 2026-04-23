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
import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.request.CourseRegisterRequest;
import com.lxp.controller.response.CourseRegisterResponse;
import com.lxp.view.command.CourseCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseView 테스트")
class CourseViewTest {

	@Mock
	private MenuRenderer menuRenderer;

	@Mock
	private InputView inputView;

	@Mock
	private OutputView outputView;

	@Mock
	private CourseController courseController;

	@Mock
	private CourseListView courseListView;

	@InjectMocks
	private CourseView courseView;

	@Test
	@DisplayName("성공 - REGISTER 처리 시 입력을 받아 등록 후 성공 문구를 출력한다")
	void handle_register() {
		// given
		when(inputView.readLine()).thenReturn("Java 입문", "2", "기초 문법", "원시타입", "설명", "0");
		when(inputView.readInt()).thenReturn(10000, 1);
		when(courseController.register(new CourseRegisterRequest(
			1L,
			"Java 입문",
			"기초 문법",
			10000,
			"2",
			List.of(new ContentRegisterRequest("원시타입", "설명"))
		)))
			.thenReturn(new CourseRegisterResponse(3L));

		// when
		boolean result = courseView.handle(CourseCommand.REGISTER);

		// then
		verify(outputView).printHeader("강의 등록");
		verify(outputView).printBody("  강의 정보를 입력하세요.");
		verify(outputView).printLabel("  강의 제목    : ");
		verify(outputView).printLabel("  가격         : ");
		verify(outputView).printLabel("  강사 ID      : ");
		verify(outputView).printLabel("  난이도(LOW/MIDDLE/HIGH)   : ");
		verify(outputView).printLabel("  강의 설명    : ");
		verify(outputView).printBody("  콘텐츠를 추가하세요. (제목에 0 입력 시 완료)");
		verify(outputView, times(2)).printLabel("  콘텐츠 제목  : ");
		verify(outputView).printLabel("  콘텐츠 내용  : ");
		verify(courseController).register(new CourseRegisterRequest(
			1L,
			"Java 입문",
			"기초 문법",
			10000,
			"2",
			List.of(new ContentRegisterRequest("원시타입", "설명"))
		));
		verify(outputView).printSuccess("  강의가 등록되었습니다. id: 3");
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
