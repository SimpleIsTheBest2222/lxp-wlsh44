package com.lxp.service;

import java.util.List;

import com.lxp.controller.request.ContentDeleteRequest;
import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.request.ContentUpdateRequest;
import com.lxp.domain.Content;

public interface ContentService {

	Content register(ContentRegisterRequest request);

	List<Content> findAllByCourseId(Long courseId);

	Content findDetailById(Long id);

	Content update(ContentUpdateRequest request);

	Content delete(ContentDeleteRequest request);
}
