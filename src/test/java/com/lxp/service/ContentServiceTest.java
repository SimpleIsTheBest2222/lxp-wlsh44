package com.lxp.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.domain.Content;
import com.lxp.domain.enums.ContentType;
import com.lxp.repository.ContentRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContentService 테스트")
class ContentServiceTest {

	@Mock
	private ContentRepository contentRepository;

	@InjectMocks
	private ContentService contentService;

	@Test
	@DisplayName("성공 - 콘텐츠를 등록한다")
	void register() {
		ContentRegisterRequest request = new ContentRegisterRequest(1L, "원시타입", "설명", 1);
		when(contentRepository.save(any())).thenAnswer(invocation -> {
			Content saved = invocation.getArgument(0);
			return Content.createWithId(
				1L,
				saved.getCourseId(),
				saved.getTitle(),
				saved.getBody(),
				saved.getContentType(),
				saved.getSeq()
			);
		});

		Content response = contentService.register(request);

		ArgumentCaptor<Content> captor = ArgumentCaptor.forClass(Content.class);
		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getCourseId()).isEqualTo(1L);
		assertThat(response.getTitle()).isEqualTo("원시타입");
		assertThat(response.getBody()).isEqualTo("설명");
		assertThat(response.getContentType()).isEqualTo(ContentType.TEXT);
		assertThat(response.getSeq()).isEqualTo(1);
		verify(contentRepository).save(captor.capture());
		assertThat(captor.getValue().getTitle()).isEqualTo("원시타입");
		assertThat(captor.getValue().getSeq()).isEqualTo(1);
	}
}
