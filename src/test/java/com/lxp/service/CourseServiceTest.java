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

import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.request.CourseRegisterRequest;
import com.lxp.domain.Content;
import com.lxp.domain.Course;
import com.lxp.domain.Instructor;
import com.lxp.domain.enums.ContentType;
import com.lxp.domain.enums.Level;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.ContentRepository;
import com.lxp.repository.CourseRepository;
import com.lxp.repository.InstructorRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService 테스트")
class CourseServiceTest {

	@Mock
	private CourseRepository courseRepository;

	@Mock
	private ContentRepository contentRepository;

	@Mock
	private InstructorRepository instructorRepository;

	@InjectMocks
	private CourseService courseService;

	@Test
	@DisplayName("성공 - 강의와 콘텐츠를 함께 등록한다")
	void register() {
		CourseRegisterRequest request = new CourseRegisterRequest(
			1L,
			"Java 입문",
			"기초 문법",
			10000,
			"LOW",
			List.of(new ContentRegisterRequest("원시타입", "설명"))
		);
		when(courseRepository.save(any())).thenAnswer(invocation -> {
			Course saved = invocation.getArgument(0);
			return Course.createWithId(
				1L,
				saved.getInstructorId(),
				saved.getTitle(),
				saved.getDescription(),
				saved.getPrice(),
				saved.getLevel(),
				null,
				null
			);
		});
		when(instructorRepository.findById(1L)).thenReturn(
			java.util.Optional.of(Instructor.createWithId(1L, "김남준", "소개")));

		Course response = courseService.register(request);

		ArgumentCaptor<Course> captor = ArgumentCaptor.forClass(Course.class);
		ArgumentCaptor<Content> contentCaptor = ArgumentCaptor.forClass(Content.class);
		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getInstructorId()).isEqualTo(1L);
		assertThat(response.getTitle()).isEqualTo("Java 입문");
		assertThat(response.getDescription()).isEqualTo("기초 문법");
		assertThat(response.getPrice()).isEqualTo(10000);
		assertThat(response.getLevel()).isEqualTo(Level.LOW);
		verify(courseRepository).save(captor.capture());
		assertThat(captor.getValue().getTitle()).isEqualTo("Java 입문");
		assertThat(captor.getValue().getInstructorId()).isEqualTo(1L);
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

	@Test
	@DisplayName("실패 - 콘텐츠 저장 중 예외가 발생하면 예외를 전파한다")
	void register_failWhenContentSaveThrows() {
		CourseRegisterRequest request = new CourseRegisterRequest(
			1L,
			"Java 입문",
			"기초 문법",
			10000,
			"LOW",
			List.of(new ContentRegisterRequest("원시타입", "설명"))
		);
		when(courseRepository.save(any())).thenAnswer(invocation -> {
			Course saved = invocation.getArgument(0);
			return Course.createWithId(
				1L,
				saved.getInstructorId(),
				saved.getTitle(),
				saved.getDescription(),
				saved.getPrice(),
				saved.getLevel(),
				null,
				null
			);
		});
		when(instructorRepository.findById(1L)).thenReturn(
			java.util.Optional.of(Instructor.createWithId(1L, "김남준", "소개")));
		when(contentRepository.save(any())).thenThrow(new RuntimeException("save failed"));

		assertThatThrownBy(() -> courseService.register(request))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("save failed");
	}

	@Test
	@DisplayName("실패 - 존재하지 않는 강사 id면 예외가 발생한다")
	void register_failWhenInstructorNotFound() {
		CourseRegisterRequest request = new CourseRegisterRequest(
			99L,
			"Java 입문",
			"기초 문법",
			10000,
			"LOW",
			List.of(new ContentRegisterRequest("원시타입", "설명"))
		);
		when(instructorRepository.findById(99L)).thenReturn(java.util.Optional.empty());

		assertThatThrownBy(() -> courseService.register(request))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.NOT_FOUND_INSTRUCTOR.getMessage());
		verifyNoInteractions(courseRepository, contentRepository);
	}

	@Test
	@DisplayName("실패 - 콘텐츠가 0개면 예외가 발생한다")
	void register_failWhenContentCountIsZero() {
		CourseRegisterRequest request = new CourseRegisterRequest(
			1L,
			"Java 입문",
			"기초 문법",
			10000,
			"LOW",
			List.of()
		);

		assertThatThrownBy(() -> courseService.register(request))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.COURSE_CONTENT_COUNT_OUT_OF_RANGE.getMessage());
		verifyNoInteractions(instructorRepository, courseRepository, contentRepository);
	}

	@Test
	@DisplayName("실패 - 콘텐츠가 10개를 초과하면 예외가 발생한다")
	void register_failWhenContentCountExceedsTen() {
		List<ContentRegisterRequest> contents = java.util.stream.IntStream.rangeClosed(1, 11)
			.mapToObj(index -> new ContentRegisterRequest("콘텐츠 " + index, "설명 " + index))
			.toList();
		CourseRegisterRequest request = new CourseRegisterRequest(
			1L,
			"Java 입문",
			"기초 문법",
			10000,
			"LOW",
			contents
		);

		assertThatThrownBy(() -> courseService.register(request))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.COURSE_CONTENT_COUNT_OUT_OF_RANGE.getMessage());
		verifyNoInteractions(instructorRepository, courseRepository, contentRepository);
	}
}
