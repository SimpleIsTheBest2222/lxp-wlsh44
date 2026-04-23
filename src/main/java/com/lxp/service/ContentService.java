package com.lxp.service;

import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.domain.Content;
import com.lxp.repository.ContentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentService {

	private final ContentRepository contentRepository;

	public Content register(ContentRegisterRequest request) {
		Content content = Content.create(
			request.courseId(),
			request.title(),
			request.body(),
			request.contentType(),
			request.seq()
		);
		return contentRepository.save(content);
	}
}
