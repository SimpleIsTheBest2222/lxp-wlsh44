package com.lxp.domain;

import com.lxp.common.validate.Assert;
import com.lxp.domain.enums.ContentType;
import com.lxp.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Content {
	private static final int MAXIMUM_TITLE_LENGTH = 50;
	private static final int MAXIMUM_BODY_LENGTH = 200;

	private Long id;
	private Long courseId;
	private String title;
	private String body;
	private ContentType contentType;
	private int seq;

	public static Content create(Long courseId, String title, String body, ContentType contentType, int seq) {
		Content content = new Content();

		content.courseId = validateCourseId(courseId);
		content.title = validateTitle(title);
		content.body = validateBody(body);
		content.contentType = validateContentType(contentType);
		content.seq = seq;

		return content;
	}

	public static Content createWithId(Long id, Long courseId, String title, String body,
		ContentType contentType, int seq) {
		Content content = new Content();
		content.id = validateId(id);
		content.courseId = validateCourseId(courseId);
		content.title = validateTitle(title);
		content.body = validateBody(body);
		content.contentType = validateContentType(contentType);
		content.seq = seq;
		return content;
	}

	public void update(String title, String body) {
		if (title != null) {
			this.title = validateTitle(title);
		}
		if (body != null) {
			this.body = validateBody(body);
		}
	}

	private static Long validateId(Long id) {
		Assert.notNull(id);
		return id;
	}

	private static Long validateCourseId(Long courseId) {
		Assert.notNull(courseId);
		return courseId;
	}

	private static String validateTitle(String title) {
		Assert.notNull(title);
		Assert.isTrue(!title.isBlank() && title.length() <= MAXIMUM_TITLE_LENGTH,
			ErrorCode.CONTENT_TITLE_OUT_OF_RANGE);
		return title;
	}

	private static String validateBody(String body) {
		Assert.notNull(body);
		Assert.isTrue(!body.isBlank() && body.length() <= MAXIMUM_BODY_LENGTH,
			ErrorCode.CONTENT_BODY_OUT_OF_RANGE);
		return body;
	}

	private static ContentType validateContentType(ContentType contentType) {
		Assert.notNull(contentType);
		return contentType;
	}
}
