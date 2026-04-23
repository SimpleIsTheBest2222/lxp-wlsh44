package com.lxp.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.controller.request.CourseRegisterRequest;
import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.domain.Content;
import com.lxp.domain.Course;
import com.lxp.domain.enums.ContentType;
import com.lxp.domain.enums.Level;
import com.lxp.repository.CourseRepository;
import com.lxp.repository.ContentRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService 테스트")
class CourseServiceTest {

	@Mock
	private CourseRepository courseRepository;

	@Mock
	private ContentRepository contentRepository;

	@InjectMocks
	private CourseService courseService;

	@Test
	@DisplayName("성공 - 강의와 콘텐츠를 함께 등록한다")
	void register() {
		CourseRegisterRequest request = new CourseRegisterRequest(
			"Java 입문",
			"기초 문법",
			10000,
			"LOW",
			List.of(new ContentRegisterRequest("원시타입", "설명", 1))
		);
		when(courseRepository.save(any())).thenAnswer(invocation -> {
			Course saved = invocation.getArgument(0);
			return Course.createWithId(
				1L,
				saved.getTitle(),
				saved.getDescription(),
				saved.getPrice(),
				saved.getLevel(),
				null,
				null
			);
		});

		Course response = courseService.register(request);

		ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
		ArgumentCaptor<Content> contentCaptor = ArgumentCaptor.forClass(Content.class);
		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getTitle()).isEqualTo("Java 입문");
		assertThat(response.getDescription()).isEqualTo("기초 문법");
		assertThat(response.getPrice()).isEqualTo(10000);
		assertThat(response.getLevel()).isEqualTo(Level.LOW);
		verify(courseRepository).save(captor.capture());
		assertThat(captor.getValue().getTitle()).isEqualTo("Java 입문");
		assertThat(captor.getValue().getDescription()).isEqualTo("기초 문법");
		assertThat(captor.getValue().getPrice()).isEqualTo(10000);
		assertThat(captor.getValue().getLevel()).isEqualTo(Level.LOW);
		verify(contentRepository).save(contentCaptor.capture());
		assertThat(contentCaptor.getValue().getCourseId()).isEqualTo(1L);
		assertThat(contentCaptor.getValue().getTitle()).isEqualTo("원시타입");
		assertThat(contentCaptor.getValue().getBody()).isEqualTo("설명");
		assertThat(contentCaptor.getValue().getContentType()).isEqualTo(ContentType.TEXT);
		assertThat(contentCaptor.getValue().getSeq()).isEqualTo(1);
	}
}
