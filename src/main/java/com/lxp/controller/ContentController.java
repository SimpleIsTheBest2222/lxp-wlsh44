package com.lxp.controller;

import java.util.List;

import com.lxp.controller.request.ContentDeleteRequest;
import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.request.ContentUpdateRequest;
import com.lxp.controller.response.ContentDeleteResponse;
import com.lxp.controller.response.ContentDetailResponse;
import com.lxp.controller.response.ContentListResponse;
import com.lxp.controller.response.ContentRegisterResponse;
import com.lxp.controller.response.ContentSummaryResponse;
import com.lxp.controller.response.ContentUpdateResponse;
import com.lxp.domain.Content;
import com.lxp.service.ContentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentController {

	private final ContentService contentService;

	public ContentRegisterResponse register(ContentRegisterRequest request) {
		Content content = contentService.register(request);
		return new ContentRegisterResponse(content.getId());
	}

	public ContentListResponse findAllByCourseId(Long courseId) {
		List<ContentSummaryResponse> contents = contentService.findAllByCourseId(courseId).stream()
			.map(content -> new ContentSummaryResponse(content.getId(), content.getTitle()))
			.toList();
		return new ContentListResponse(contents);
	}

	public ContentDetailResponse findDetailById(Long id) {
		Content content = contentService.findDetailById(id);
		return new ContentDetailResponse(
			content.getId(),
			content.getTitle(),
			content.getBody(),
			content.getContentType().name()
		);
	}

	public ContentUpdateResponse update(ContentUpdateRequest request) {
		Content content = contentService.update(request);
		return new ContentUpdateResponse(content.getId());
	}

	public ContentDeleteResponse delete(ContentDeleteRequest request) {
		Content content = contentService.delete(request);
		return new ContentDeleteResponse(content.getId());
	}
}
