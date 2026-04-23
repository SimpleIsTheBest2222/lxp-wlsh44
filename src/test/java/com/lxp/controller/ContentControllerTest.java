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

import com.lxp.controller.request.ContentDeleteRequest;
import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.request.ContentUpdateRequest;
import com.lxp.controller.response.ContentDeleteResponse;
import com.lxp.controller.response.ContentDetailResponse;
import com.lxp.controller.response.ContentListResponse;
import com.lxp.controller.response.ContentRegisterResponse;
import com.lxp.controller.response.ContentUpdateResponse;
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
		ContentRegisterRequest request = new ContentRegisterRequest(1L, "Stream", "설명", "TEXT");
		when(contentService.register(request))
			.thenReturn(Content.createWithId(3L, 1L, "Stream", "설명", ContentType.TEXT, 3));

		ContentRegisterResponse response = contentController.register(request);

		assertThat(response.id()).isEqualTo(3L);
	}

	@Test
	@DisplayName("성공 - 강의의 콘텐츠 목록을 조회한다")
	void findAllByCourseId() {
		when(contentService.findAllByCourseId(1L)).thenReturn(List.of(
			Content.createWithId(1L, 1L, "원시타입", "설명", ContentType.TEXT, 1)
		));

		ContentListResponse response = contentController.findAllByCourseId(1L);

		assertThat(response.contents()).hasSize(1);
		assertThat(response.contents().get(0).title()).isEqualTo("원시타입");
	}

	@Test
	@DisplayName("성공 - 콘텐츠 상세를 조회한다")
	void findDetailById() {
		when(contentService.findDetailById(1L))
			.thenReturn(Content.createWithId(1L, 1L, "원시타입", "설명", ContentType.TEXT, 1));

		ContentDetailResponse response = contentController.findDetailById(1L);

		assertThat(response.id()).isEqualTo(1L);
		assertThat(response.title()).isEqualTo("원시타입");
		assertThat(response.body()).isEqualTo("설명");
		assertThat(response.contentType()).isEqualTo("TEXT");
	}

	@Test
	@DisplayName("성공 - 콘텐츠를 수정하면 id를 반환한다")
	void update() {
		ContentUpdateRequest request = new ContentUpdateRequest(1L, "Stream", "설명");
		when(contentService.update(request))
			.thenReturn(Content.createWithId(1L, 1L, "Stream", "설명", ContentType.TEXT, 1));

		ContentUpdateResponse response = contentController.update(request);

		assertThat(response.id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("성공 - 콘텐츠를 삭제하면 id를 반환한다")
	void delete() {
		ContentDeleteRequest request = new ContentDeleteRequest(1L);
		when(contentService.delete(request))
			.thenReturn(Content.createWithId(1L, 1L, "원시타입", "설명", ContentType.TEXT, 1));

		ContentDeleteResponse response = contentController.delete(request);

		assertThat(response.id()).isEqualTo(1L);
	}
}
