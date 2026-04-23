package com.lxp.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.request.CourseRegisterRequest;
import com.lxp.controller.response.CourseRegisterResponse;
import com.lxp.domain.Course;
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
			"Java 입문",
			"기초 문법",
			10000,
			"LOW",
			List.of(new ContentRegisterRequest("원시타입", "설명", 1))
		);
		when(courseService.register(request))
			.thenReturn(Course.createWithId(1L, "Java 입문", "기초 문법", 10000, Level.LOW, null, null));

		CourseRegisterResponse response = courseController.register(request);

		assertThat(response.id()).isEqualTo(1L);
	}
}
