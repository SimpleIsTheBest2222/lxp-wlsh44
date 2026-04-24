package com.lxp.service;

import java.util.Comparator;
import java.util.List;

import com.lxp.controller.request.ContentDeleteRequest;
import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.request.ContentUpdateRequest;
import com.lxp.domain.Content;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.ContentRepository;
import com.lxp.repository.CourseRepository;
import com.lxp.transaction.Transaction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultContentService implements ContentService {

	private final CourseRepository courseRepository;
	private final ContentRepository contentRepository;

	@Override
	@Transaction
	public Content register(ContentRegisterRequest request) {
		validateCourseId(request.courseId());
		Content content = Content.create(
			request.courseId(),
			request.title(),
			request.body(),
			request.contentType(),
			nextSeq(request.courseId())
		);
		return contentRepository.save(content);
	}

	@Override
	public List<Content> findAllByCourseId(Long courseId) {
		validateCourseId(courseId);
		return contentRepository.findAll().stream()
			.filter(content -> content.getCourseId().equals(courseId))
			.sorted(Comparator.comparingInt(Content::getSeq))
			.toList();
	}

	@Override
	public Content findDetailById(Long id) {
		return getContentOrThrow(id);
	}

	@Override
	@Transaction
	public Content update(ContentUpdateRequest request) {
		Content content = getContentOrThrow(request.id());
		content.update(request.title(), request.body());
		return contentRepository.save(content);
	}

	@Override
	@Transaction
	public Content delete(ContentDeleteRequest request) {
		Content content = getContentOrThrow(request.id());
		contentRepository.deleteById(request.id());
		return content;
	}

	private void validateCourseId(Long courseId) {
		if (courseId == null || courseId <= 0L) {
			throw new LxpException(ErrorCode.INVALID_INPUT);
		}
		courseRepository.findById(courseId)
			.orElseThrow(() -> new LxpException(ErrorCode.NOT_FOUND_COURSE));
	}

	private int nextSeq(Long courseId) {
		return contentRepository.findAll().stream()
			.filter(content -> content.getCourseId().equals(courseId))
			.mapToInt(Content::getSeq)
			.max()
			.orElse(0) + 1;
	}

	private Content getContentOrThrow(Long id) {
		return contentRepository.findById(id)
			.orElseThrow(() -> new LxpException(ErrorCode.NOT_FOUND_CONTENT));
	}
}
