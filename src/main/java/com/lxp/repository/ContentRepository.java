package com.lxp.repository;

import java.util.List;
import java.util.Optional;

import com.lxp.domain.Content;

public interface ContentRepository {

	Optional<Content> findById(Long id);

	List<Content> findAll();

	Content save(Content content);

	void deleteById(Long id);
}
