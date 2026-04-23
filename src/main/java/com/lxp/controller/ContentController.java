package com.lxp.controller;

import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.response.ContentRegisterResponse;
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
}
