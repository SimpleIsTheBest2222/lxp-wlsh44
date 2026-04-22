package com.lxp.domain;

import static org.assertj.core.api.Assertions.*;

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
			// given
			Long courseId = 1L;
			String title = "1강. 소개";
			String body = "Java란 무엇인가.";
			ContentType contentType = ContentType.VIDEO;
			int seq = 1;

			// when
			Content content = Content.create(courseId, title, body, contentType, seq);

			// then
			assertThat(content.getCourseId()).isEqualTo(courseId);
			assertThat(content.getTitle()).isEqualTo(title);
			assertThat(content.getBody()).isEqualTo(body);
			assertThat(content.getContentType()).isEqualTo(contentType);
			assertThat(content.getSeq()).isEqualTo(seq);
		}

		@Test
		@DisplayName("courseId가 null이면 INVALID_ARGUMENTS 예외")
		void courseIdNull() {
			// when & then
			assertThatThrownBy(() -> Content.create(null, "1강. 소개", "내용입니다.", ContentType.VIDEO, 1))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INVALID_ARGUMENTS.getMessage());
		}

		@Test
		@DisplayName("제목이 null이면 INVALID_ARGUMENTS 예외")
		void titleNull() {
			// when & then
			assertThatThrownBy(() -> Content.create(1L, null, "내용입니다.", ContentType.VIDEO, 1))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INVALID_ARGUMENTS.getMessage());
		}

		@Test
		@DisplayName("제목이 공백이면 CONTENT_TITLE_OUT_OF_RANGE 예외")
		void titleBlank() {
			// given
			String blankTitle = "   ";

			// when & then
			assertThatThrownBy(() -> Content.create(1L, blankTitle, "내용입니다.", ContentType.VIDEO, 1))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.CONTENT_TITLE_OUT_OF_RANGE.getMessage());
		}

		@Test
		@DisplayName("제목이 50자를 초과하면 CONTENT_TITLE_OUT_OF_RANGE 예외")
		void titleTooLong() {
			// given
			String tooLongTitle = "가".repeat(51);

			// when & then
			assertThatThrownBy(() -> Content.create(1L, tooLongTitle, "내용입니다.", ContentType.VIDEO, 1))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.CONTENT_TITLE_OUT_OF_RANGE.getMessage());
		}

		@Test
		@DisplayName("제목이 정확히 50자이면 성공")
		void titleMaxLength() {
			// given
			String maxLengthTitle = "가".repeat(50);

			// when & then
			assertThatNoException()
				.isThrownBy(() -> Content.create(1L, maxLengthTitle, "내용입니다.", ContentType.VIDEO, 1));
		}

		@Test
		@DisplayName("본문이 null이면 INVALID_ARGUMENTS 예외")
		void bodyNull() {
			// when & then
			assertThatThrownBy(() -> Content.create(1L, "1강. 소개", null, ContentType.VIDEO, 1))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INVALID_ARGUMENTS.getMessage());
		}

		@Test
		@DisplayName("본문이 공백이면 CONTENT_BODY_OUT_OF_RANGE 예외")
		void bodyBlank() {
			// given
			String blankBody = "   ";

			// when & then
			assertThatThrownBy(() -> Content.create(1L, "1강. 소개", blankBody, ContentType.VIDEO, 1))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.CONTENT_BODY_OUT_OF_RANGE.getMessage());
		}

		@Test
		@DisplayName("본문이 200자를 초과하면 CONTENT_BODY_OUT_OF_RANGE 예외")
		void bodyTooLong() {
			// given
			String tooLongBody = "가".repeat(201);

			// when & then
			assertThatThrownBy(() -> Content.create(1L, "1강. 소개", tooLongBody, ContentType.VIDEO, 1))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.CONTENT_BODY_OUT_OF_RANGE.getMessage());
		}

		@Test
		@DisplayName("contentType이 null이면 INVALID_ARGUMENTS 예외")
		void contentTypeNull() {
			// when & then
			assertThatThrownBy(() -> Content.create(1L, "1강. 소개", "내용입니다.", null, 1))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INVALID_ARGUMENTS.getMessage());
		}
	}

	@Nested
	@DisplayName("createWithId()")
	class CreateWithId {

		@Test
		@DisplayName("정상 생성")
		void success() {
			// given
			Long id = 10L;
			Long courseId = 1L;

			// when
			Content content = Content.createWithId(id, courseId, "1강. 소개", "내용입니다.", ContentType.TEXT, 1);

			// then
			assertThat(content.getId()).isEqualTo(id);
			assertThat(content.getCourseId()).isEqualTo(courseId);
		}

		@Test
		@DisplayName("id가 null이면 INVALID_ARGUMENTS 예외")
		void idNull() {
			// when & then
			assertThatThrownBy(() -> Content.createWithId(null, 1L, "1강. 소개", "내용입니다.", ContentType.TEXT, 1))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INVALID_ARGUMENTS.getMessage());
		}
	}

	@Nested
	@DisplayName("update()")
	class Update {

		@Test
		@DisplayName("전달된 필드만 수정되고 나머지는 유지")
		void updatePartialFields() {
			// given
			Content content = Content.create(1L, "1강. 소개", "기존 내용입니다.", ContentType.VIDEO, 1);
			String newTitle = "2강. 심화";

			// when
			content.update(newTitle, null);

			// then
			assertThat(content.getTitle()).isEqualTo(newTitle);
			assertThat(content.getBody()).isEqualTo("기존 내용입니다.");
		}

		@Test
		@DisplayName("모든 필드를 null로 전달하면 기존 값 유지")
		void allNullKeepsExistingValues() {
			// given
			Content content = Content.create(1L, "1강. 소개", "기존 내용입니다.", ContentType.VIDEO, 1);

			// when
			content.update(null, null);

			// then
			assertThat(content.getTitle()).isEqualTo("1강. 소개");
			assertThat(content.getBody()).isEqualTo("기존 내용입니다.");
		}

		@Test
		@DisplayName("수정 제목이 50자를 초과하면 CONTENT_TITLE_OUT_OF_RANGE 예외")
		void updateTitleTooLong() {
			// given
			Content content = Content.create(1L, "1강. 소개", "내용입니다.", ContentType.VIDEO, 1);
			String tooLongTitle = "가".repeat(51);

			// when & then
			assertThatThrownBy(() -> content.update(tooLongTitle, null))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.CONTENT_TITLE_OUT_OF_RANGE.getMessage());
		}
	}
}
