package com.lxp.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.lxp.domain.enums.ContentType;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("Content")
class ContentTest {

	@Nested
	@DisplayName("create()")
	class Create {

		@Test
		@DisplayName("정상 생성")
		void success() {
			Content content = Content.create(1L, "1강. 소개", "Java란 무엇인가.", ContentType.VIDEO, 1);

			assertEquals(1L, content.getCourseId());
			assertEquals("1강. 소개", content.getTitle());
			assertEquals("Java란 무엇인가.", content.getBody());
			assertEquals(ContentType.VIDEO, content.getContentType());
			assertEquals(1, content.getSeq());
		}

		@Test
		@DisplayName("courseId가 null이면 INVALID_ARGUMENTS 예외")
		void courseIdNull() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Content.create(null, "1강. 소개", "내용입니다.", ContentType.VIDEO, 1));
			assertEquals(ErrorCode.INVALID_ARGUMENTS, ex.getErrorCode());
		}

		@Test
		@DisplayName("제목이 null이면 INVALID_ARGUMENTS 예외")
		void titleNull() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Content.create(1L, null, "내용입니다.", ContentType.VIDEO, 1));
			assertEquals(ErrorCode.INVALID_ARGUMENTS, ex.getErrorCode());
		}

		@Test
		@DisplayName("제목이 공백이면 CONTENT_TITLE_OUT_OF_RANGE 예외")
		void titleBlank() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Content.create(1L, "   ", "내용입니다.", ContentType.VIDEO, 1));
			assertEquals(ErrorCode.CONTENT_TITLE_OUT_OF_RANGE, ex.getErrorCode());
		}

		@Test
		@DisplayName("제목이 50자를 초과하면 CONTENT_TITLE_OUT_OF_RANGE 예외")
		void titleTooLong() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Content.create(1L, "가".repeat(51), "내용입니다.", ContentType.VIDEO, 1));
			assertEquals(ErrorCode.CONTENT_TITLE_OUT_OF_RANGE, ex.getErrorCode());
		}

		@Test
		@DisplayName("제목이 정확히 50자이면 성공")
		void titleMaxLength() {
			assertDoesNotThrow(() -> Content.create(1L, "가".repeat(50), "내용입니다.", ContentType.VIDEO, 1));
		}

		@Test
		@DisplayName("본문이 null이면 INVALID_ARGUMENTS 예외")
		void bodyNull() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Content.create(1L, "1강. 소개", null, ContentType.VIDEO, 1));
			assertEquals(ErrorCode.INVALID_ARGUMENTS, ex.getErrorCode());
		}

		@Test
		@DisplayName("본문이 공백이면 CONTENT_BODY_OUT_OF_RANGE 예외")
		void bodyBlank() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Content.create(1L, "1강. 소개", "   ", ContentType.VIDEO, 1));
			assertEquals(ErrorCode.CONTENT_BODY_OUT_OF_RANGE, ex.getErrorCode());
		}

		@Test
		@DisplayName("본문이 200자를 초과하면 CONTENT_BODY_OUT_OF_RANGE 예외")
		void bodyTooLong() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Content.create(1L, "1강. 소개", "가".repeat(201), ContentType.VIDEO, 1));
			assertEquals(ErrorCode.CONTENT_BODY_OUT_OF_RANGE, ex.getErrorCode());
		}

		@Test
		@DisplayName("contentType이 null이면 INVALID_ARGUMENTS 예외")
		void contentTypeNull() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Content.create(1L, "1강. 소개", "내용입니다.", null, 1));
			assertEquals(ErrorCode.INVALID_ARGUMENTS, ex.getErrorCode());
		}
	}

	@Nested
	@DisplayName("createWithId()")
	class CreateWithId {

		@Test
		@DisplayName("정상 생성")
		void success() {
			Content content = Content.createWithId(10L, 1L, "1강. 소개", "내용입니다.", ContentType.TEXT, 1);

			assertEquals(10L, content.getId());
			assertEquals(1L, content.getCourseId());
		}

		@Test
		@DisplayName("id가 null이면 INVALID_ARGUMENTS 예외")
		void idNull() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Content.createWithId(null, 1L, "1강. 소개", "내용입니다.", ContentType.TEXT, 1));
			assertEquals(ErrorCode.INVALID_ARGUMENTS, ex.getErrorCode());
		}
	}

	@Nested
	@DisplayName("update()")
	class Update {

		@Test
		@DisplayName("전달된 필드만 수정되고 나머지는 유지")
		void updatePartialFields() {
			Content content = Content.create(1L, "1강. 소개", "기존 내용입니다.", ContentType.VIDEO, 1);

			content.update("2강. 심화", null);

			assertEquals("2강. 심화", content.getTitle());
			assertEquals("기존 내용입니다.", content.getBody());
		}

		@Test
		@DisplayName("모든 필드를 null로 전달하면 기존 값 유지")
		void allNullKeepsExistingValues() {
			Content content = Content.create(1L, "1강. 소개", "기존 내용입니다.", ContentType.VIDEO, 1);

			content.update(null, null);

			assertEquals("1강. 소개", content.getTitle());
			assertEquals("기존 내용입니다.", content.getBody());
		}

		@Test
		@DisplayName("수정 제목이 50자를 초과하면 CONTENT_TITLE_OUT_OF_RANGE 예외")
		void updateTitleTooLong() {
			Content content = Content.create(1L, "1강. 소개", "내용입니다.", ContentType.VIDEO, 1);

			LxpException ex = assertThrows(LxpException.class,
				() -> content.update("가".repeat(51), null));
			assertEquals(ErrorCode.CONTENT_TITLE_OUT_OF_RANGE, ex.getErrorCode());
		}
	}
}
