package com.lxp.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.controller.request.ContentDeleteRequest;
import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.request.ContentUpdateRequest;
import com.lxp.domain.Content;
import com.lxp.domain.Course;
import com.lxp.domain.enums.ContentType;
import com.lxp.domain.enums.Level;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.ContentRepository;
import com.lxp.repository.CourseRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContentService 테스트")
class ContentServiceTest {

	@Mock
	private CourseRepository courseRepository;

	@Mock
	private ContentRepository contentRepository;

	@InjectMocks
	private DefaultContentService contentService;

	@Test
	@DisplayName("성공 - 콘텐츠를 등록하면 다음 seq를 부여한다")
	void register() {
		ContentRegisterRequest request = new ContentRegisterRequest(1L, "Stream", "설명", "TEXT");
		when(courseRepository.findById(1L)).thenReturn(Optional.of(
			Course.createWithId(1L, 1L, "Java 입문", "기초 문법", 10000, Level.LOW, null, null)
		));
		when(contentRepository.findAll()).thenReturn(List.of(
			Content.createWithId(1L, 1L, "원시타입", "설명", ContentType.TEXT, 1),
			Content.createWithId(2L, 1L, "for 문", "설명", ContentType.TEXT, 2)
		));
		when(contentRepository.save(any())).thenAnswer(invocation -> {
			Content saved = invocation.getArgument(0);
			return Content.createWithId(
				3L,
				saved.getCourseId(),
				saved.getTitle(),
				saved.getBody(),
				saved.getContentType(),
				saved.getSeq()
			);
		});

		Content response = contentService.register(request);

		ArgumentCaptor<Content> captor = ArgumentCaptor.forClass(Content.class);
		verify(contentRepository).save(captor.capture());
		assertThat(response.getId()).isEqualTo(3L);
		assertThat(captor.getValue().getCourseId()).isEqualTo(1L);
		assertThat(captor.getValue().getTitle()).isEqualTo("Stream");
		assertThat(captor.getValue().getContentType()).isEqualTo(ContentType.TEXT);
		assertThat(captor.getValue().getSeq()).isEqualTo(3);
	}

	@Test
	@DisplayName("성공 - 다른 강의 콘텐츠는 seq 계산에서 제외한다")
	void register_excludeOtherCourseContentsWhenCalculatingSeq() {
		ContentRegisterRequest request = new ContentRegisterRequest(1L, "Stream", "설명", "TEXT");
		when(courseRepository.findById(1L)).thenReturn(Optional.of(
			Course.createWithId(1L, 1L, "Java 입문", "기초 문법", 10000, Level.LOW, null, null)
		));
		when(contentRepository.findAll()).thenReturn(List.of(
			Content.createWithId(1L, 2L, "JPA", "설명", ContentType.TEXT, 7)
		));
		when(contentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		contentService.register(request);

		ArgumentCaptor<Content> captor = ArgumentCaptor.forClass(Content.class);
		verify(contentRepository).save(captor.capture());
		assertThat(captor.getValue().getSeq()).isEqualTo(1);
	}

	@Test
	@DisplayName("성공 - 강의의 콘텐츠 목록을 순서대로 조회한다")
	void findAllByCourseId() {
		when(courseRepository.findById(1L)).thenReturn(Optional.of(
			Course.createWithId(1L, 1L, "Java 입문", "기초 문법", 10000, Level.LOW, null, null)
		));
		when(contentRepository.findAll()).thenReturn(List.of(
			Content.createWithId(2L, 1L, "for 문", "설명", ContentType.TEXT, 2),
			Content.createWithId(1L, 1L, "원시타입", "설명", ContentType.TEXT, 1),
			Content.createWithId(3L, 2L, "JPA", "설명", ContentType.TEXT, 1)
		));

		List<Content> result = contentService.findAllByCourseId(1L);

		assertThat(result).extracting(Content::getTitle)
			.containsExactly("원시타입", "for 문");
	}

	@Test
	@DisplayName("성공 - 콘텐츠 상세를 조회한다")
	void findDetailById() {
		when(contentRepository.findById(1L)).thenReturn(Optional.of(
			Content.createWithId(1L, 1L, "원시타입", "설명", ContentType.TEXT, 1)
		));

		Content result = contentService.findDetailById(1L);

		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getTitle()).isEqualTo("원시타입");
	}

	@Test
	@DisplayName("성공 - 콘텐츠를 수정한다")
	void update() {
		Content content = Content.createWithId(1L, 1L, "원시타입", "설명", ContentType.TEXT, 1);
		ContentUpdateRequest request = new ContentUpdateRequest(1L, "Stream", "Stream 설명");
		when(contentRepository.findById(1L)).thenReturn(Optional.of(content));
		when(contentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		Content response = contentService.update(request);

		assertThat(response.getTitle()).isEqualTo("Stream");
		assertThat(response.getBody()).isEqualTo("Stream 설명");
		verify(contentRepository).save(content);
	}

	@Test
	@DisplayName("성공 - 빈 값으로 콘텐츠를 수정하면 기존 값이 유지된다")
	void update_keepExistingValuesWhenFieldsAreBlank() {
		Content content = Content.createWithId(1L, 1L, "원시타입", "설명", ContentType.TEXT, 1);
		ContentUpdateRequest request = new ContentUpdateRequest(1L, "", "  ");
		when(contentRepository.findById(1L)).thenReturn(Optional.of(content));
		when(contentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		Content response = contentService.update(request);

		assertThat(response.getTitle()).isEqualTo("원시타입");
		assertThat(response.getBody()).isEqualTo("설명");
		verify(contentRepository).save(content);
	}

	@Test
	@DisplayName("성공 - 콘텐츠를 삭제한다")
	void delete() {
		Content content = Content.createWithId(1L, 1L, "원시타입", "설명", ContentType.TEXT, 1);
		when(contentRepository.findById(1L)).thenReturn(Optional.of(content));

		Content response = contentService.delete(new ContentDeleteRequest(1L));

		assertThat(response.getId()).isEqualTo(1L);
		verify(contentRepository).deleteById(1L);
	}

	@Test
	@DisplayName("실패 - 존재하지 않는 강의에 콘텐츠를 등록하면 예외가 발생한다")
	void register_failWhenCourseNotFound() {
		ContentRegisterRequest request = new ContentRegisterRequest(99L, "Stream", "설명", "TEXT");
		when(courseRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> contentService.register(request))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.NOT_FOUND_COURSE.getMessage());
		verifyNoInteractions(contentRepository);
	}

	@Test
	@DisplayName("실패 - 존재하지 않는 강의의 콘텐츠 목록을 조회하면 예외가 발생한다")
	void findAllByCourseId_failWhenCourseNotFound() {
		when(courseRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> contentService.findAllByCourseId(99L))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.NOT_FOUND_COURSE.getMessage());
		verifyNoInteractions(contentRepository);
	}

	@Test
	@DisplayName("실패 - 콘텐츠 id가 존재하지 않으면 상세 조회 시 예외가 발생한다")
	void findDetailById_failWhenContentNotFound() {
		when(contentRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> contentService.findDetailById(99L))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.NOT_FOUND_CONTENT.getMessage());
	}

	@Test
	@DisplayName("실패 - 존재하지 않는 콘텐츠를 수정하면 예외가 발생한다")
	void update_failWhenContentNotFound() {
		when(contentRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> contentService.update(new ContentUpdateRequest(99L, "Stream", "설명")))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.NOT_FOUND_CONTENT.getMessage());
		verify(contentRepository, never()).save(any());
	}

	@Test
	@DisplayName("실패 - 존재하지 않는 콘텐츠를 삭제하면 예외가 발생한다")
	void delete_failWhenContentNotFound() {
		when(contentRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> contentService.delete(new ContentDeleteRequest(99L)))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.NOT_FOUND_CONTENT.getMessage());
		verify(contentRepository, never()).deleteById(any());
	}
}
