package com.lxp.repository.entity;

import java.time.LocalDateTime;

import com.lxp.domain.Content;
import com.lxp.domain.enums.ContentType;
import com.lxp.repository.entity.enums.EntityStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentEntity extends BaseEntity {

	private Long id;
	private Long courseId;
	private String title;
	private String body;
	private ContentType contentType;
	private int seq;

	public static ContentEntity create(Long id, Content content, LocalDateTime now) {
		ContentEntity contentEntity = new ContentEntity();
		contentEntity.id = id;
		contentEntity.courseId = content.getCourseId();
		contentEntity.title = content.getTitle();
		contentEntity.body = content.getBody();
		contentEntity.contentType = content.getContentType();
		contentEntity.seq = content.getSeq();
		contentEntity.initialize(now);
		return contentEntity;
	}

	public static ContentEntity restore(
		Long id,
		Long courseId,
		String title,
		String body,
		ContentType contentType,
		int seq,
		EntityStatus status,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt
	) {
		ContentEntity contentEntity = new ContentEntity();
		contentEntity.id = id;
		contentEntity.courseId = courseId;
		contentEntity.title = title;
		contentEntity.body = body;
		contentEntity.contentType = contentType;
		contentEntity.seq = seq;
		contentEntity.restore(status, createdAt, modifiedAt);
		return contentEntity;
	}

	public void update(Content content, LocalDateTime now) {
		this.title = content.getTitle();
		this.body = content.getBody();
		this.contentType = content.getContentType();
		this.seq = content.getSeq();
		modified(now);
	}

	public void delete(LocalDateTime now) {
		super.delete(now);
	}

	public Content toDomain() {
		return Content.createWithId(id, courseId, title, body, contentType, seq);
	}
}
