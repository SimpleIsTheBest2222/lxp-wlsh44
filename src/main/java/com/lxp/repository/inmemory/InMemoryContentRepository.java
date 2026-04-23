package com.lxp.repository.inmemory;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.lxp.domain.Content;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.ContentRepository;
import com.lxp.repository.entity.ContentEntity;

public class InMemoryContentRepository implements ContentRepository {

	private final Map<Long, ContentEntity> data = new LinkedHashMap<>();
	private long nextId = 1L;

	@Override
	public Optional<Content> findById(Long id) {
		return Optional.ofNullable(data.get(id))
			.filter(content -> !content.isDeleted())
			.map(ContentEntity::toDomain);
	}

	@Override
	public List<Content> findAll() {
		return data.values().stream()
			.filter(content -> !content.isDeleted())
			.map(ContentEntity::toDomain)
			.toList();
	}

	@Override
	public Content save(Content content) {
		LocalDateTime now = LocalDateTime.now();

		if (content.getId() == null) {
			return saveNew(content, now);
		}

		ContentEntity existingContent = Optional.ofNullable(data.get(content.getId()))
			.orElseThrow(() -> new LxpException(ErrorCode.NOT_FOUND_CONTENT));

		return saveExisting(content, existingContent, now);
	}

	private Content saveNew(Content content, LocalDateTime now) {
		ContentEntity savedContent = ContentEntity.create(nextId++, content, now);
		data.put(savedContent.getId(), savedContent);
		return savedContent.toDomain();
	}

	private Content saveExisting(Content content, ContentEntity existingContent, LocalDateTime now) {
		existingContent.update(content, now);
		return existingContent.toDomain();
	}

	@Override
	public void deleteById(Long id) {
		Optional.ofNullable(data.get(id))
			.ifPresent(content -> content.delete(LocalDateTime.now()));
	}
}
