package com.lxp.repository.inmemory;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.domain.Content;
import com.lxp.domain.enums.ContentType;

@DisplayName("InMemoryContentRepository 테스트")
class InMemoryContentRepositoryTest {

	private final InMemoryContentRepository contentRepository = new InMemoryContentRepository();

	@Test
	@DisplayName("성공 - id가 없는 콘텐츠를 저장하면 id를 할당한다")
	void save_newContent() {
		Content content = Content.create(1L, "원시타입", "설명", ContentType.TEXT, 1);

		Content savedContent = contentRepository.save(content);

		assertThat(savedContent.getId()).isEqualTo(1L);
		assertThat(contentRepository.findAll()).hasSize(1);
	}

	@Test
	@DisplayName("성공 - 콘텐츠를 삭제하면 soft delete 처리되어 조회되지 않는다")
	void deleteById() {
		Content savedContent = contentRepository.save(Content.create(1L, "원시타입", "설명", ContentType.TEXT, 1));

		contentRepository.deleteById(savedContent.getId());

		assertThat(contentRepository.findById(savedContent.getId())).isEmpty();
		assertThat(contentRepository.findAll()).isEmpty();
	}
}
