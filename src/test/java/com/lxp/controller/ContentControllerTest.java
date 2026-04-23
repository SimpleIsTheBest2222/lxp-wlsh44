package com.lxp.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.response.ContentRegisterResponse;
import com.lxp.domain.Content;
import com.lxp.domain.enums.ContentType;
import com.lxp.service.ContentService;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContentController 테스트")
class ContentControllerTest {

	@Mock
	private ContentService contentService;

	@InjectMocks
	private ContentController contentController;

	@Test
	@DisplayName("성공 - 콘텐츠를 등록하면 id를 반환한다")
	void register() {
		ContentRegisterRequest request = new ContentRegisterRequest(1L, "원시타입", "설명", 1);
		when(contentService.register(request))
			.thenReturn(Content.createWithId(1L, 1L, "원시타입", "설명", ContentType.TEXT, 1));

		ContentRegisterResponse response = contentController.register(request);

		assertThat(response.id()).isEqualTo(1L);
	}
}
