package com.lxp.controller;

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

import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.request.CourseRegisterRequest;
import com.lxp.controller.response.CourseDetailResponse;
import com.lxp.controller.response.CourseListResponse;
import com.lxp.controller.response.CourseRegisterResponse;
import com.lxp.domain.Content;
import com.lxp.domain.Course;
import com.lxp.domain.Instructor;
import com.lxp.domain.enums.ContentType;
import com.lxp.domain.enums.Level;
import com.lxp.service.CourseService;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseController 테스트")
class CourseControllerTest {

	@Mock
	private CourseService courseService;

	@InjectMocks
	private CourseController courseController;

	@Test
	@DisplayName("성공 - 강의를 등록하면 id를 반환한다")
	void register() {
		CourseRegisterRequest request = new CourseRegisterRequest(
			1L,
			"Java 입문",
			"기초 문법",
			10000,
			"LOW",
			List.of(new ContentRegisterRequest("원시타입", "설명"))
		);
		when(courseService.register(request))
			.thenReturn(Course.createWithId(1L, 1L, "Java 입문", "기초 문법", 10000, Level.LOW, null, null));

		CourseRegisterResponse response = courseController.register(request);

		assertThat(response.id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("성공 - 강의 목록을 조회한다")
	void findAll() {
		when(courseService.findAll()).thenReturn(List.of(
			Course.createWithId(1L, 2L, "Java 입문", "기초 문법", 10000, Level.LOW, null, null)
		));
		when(courseService.findInstructorById(2L)).thenReturn(Instructor.createWithId(2L, "김남준", "자바 강사"));

		CourseListResponse response = courseController.findAll();

		assertThat(response.courses()).hasSize(1);
		assertThat(response.courses().get(0).title()).isEqualTo("Java 입문");
		assertThat(response.courses().get(0).instructorName()).isEqualTo("김남준");
	}

	@Test
	@DisplayName("성공 - 강의 상세 조회 시 강사 정보와 콘텐츠 목록을 함께 반환한다")
	void findDetailById() {
		when(courseService.findDetailById(1L)).thenReturn(Course.createWithId(
			1L,
			2L,
			"Java 입문",
			"기초 문법",
			10000,
			Level.LOW,
			LocalDateTime.of(2025, 3, 10, 0, 0),
			LocalDateTime.of(2025, 4, 1, 0, 0)
		));
		when(courseService.findInstructorById(2L)).thenReturn(Instructor.createWithId(2L, "김남준", "자바 강사"));
		when(courseService.findContentsByCourseId(1L)).thenReturn(List.of(
			Content.createWithId(1L, 1L, "원시타입", "설명", ContentType.TEXT, 1)
		));

		CourseDetailResponse response = courseController.findDetailById(1L);

		assertThat(response.id()).isEqualTo(1L);
		assertThat(response.instructorName()).isEqualTo("김남준");
		assertThat(response.instructorIntroduction()).isEqualTo("자바 강사");
		assertThat(response.contents()).hasSize(1);
		assertThat(response.contents().get(0).title()).isEqualTo("원시타입");
		assertThat(response.level()).isEqualTo("입문");
	}
}
