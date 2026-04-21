package com.lxp.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.lxp.domain.enums.Level;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("Course")
class CourseTest {

	@Nested
	@DisplayName("create()")
	class Create {

		@Test
		@DisplayName("정상 생성")
		void success() {
			Course course = Course.create("Java 입문", "Java 기초 강의입니다.", 10000, Level.LOW);

			assertEquals("Java 입문", course.getTitle());
			assertEquals("Java 기초 강의입니다.", course.getDescription());
			assertEquals(10000, course.getPrice());
			assertEquals(Level.LOW, course.getLevel());
			assertNull(course.getId());
		}

		@Test
		@DisplayName("가격이 0이면 성공")
		void priceZeroSuccess() {
			assertDoesNotThrow(() -> Course.create("Java 입문", "설명입니다.", 0, Level.LOW));
		}

		@Test
		@DisplayName("제목이 null이면 INVALID_ARGUMENTS 예외")
		void titleNull() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Course.create(null, "설명입니다.", 0, Level.LOW));
			assertEquals(ErrorCode.INVALID_ARGUMENTS, ex.getErrorCode());
		}

		@Test
		@DisplayName("제목이 공백이면 COURSE_TITLE_OUT_OF_RANGE 예외")
		void titleBlank() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Course.create("   ", "설명입니다.", 0, Level.LOW));
			assertEquals(ErrorCode.COURSE_TITLE_OUT_OF_RANGE, ex.getErrorCode());
		}

		@Test
		@DisplayName("제목이 50자를 초과하면 COURSE_TITLE_OUT_OF_RANGE 예외")
		void titleTooLong() {
			String longTitle = "가".repeat(51);
			LxpException ex = assertThrows(LxpException.class,
				() -> Course.create(longTitle, "설명입니다.", 0, Level.LOW));
			assertEquals(ErrorCode.COURSE_TITLE_OUT_OF_RANGE, ex.getErrorCode());
		}

		@Test
		@DisplayName("제목이 정확히 50자이면 성공")
		void titleMaxLength() {
			assertDoesNotThrow(() -> Course.create("가".repeat(50), "설명입니다.", 0, Level.LOW));
		}

		@Test
		@DisplayName("설명이 null이면 INVALID_ARGUMENTS 예외")
		void descriptionNull() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Course.create("Java 입문", null, 0, Level.LOW));
			assertEquals(ErrorCode.INVALID_ARGUMENTS, ex.getErrorCode());
		}

		@Test
		@DisplayName("설명이 공백이면 COURSE_DESCRIPTION_OUT_OF_RANGE 예외")
		void descriptionBlank() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Course.create("Java 입문", "   ", 0, Level.LOW));
			assertEquals(ErrorCode.COURSE_DESCRIPTION_OUT_OF_RANGE, ex.getErrorCode());
		}

		@Test
		@DisplayName("설명이 200자를 초과하면 COURSE_DESCRIPTION_OUT_OF_RANGE 예외")
		void descriptionTooLong() {
			String longDesc = "가".repeat(201);
			LxpException ex = assertThrows(LxpException.class,
				() -> Course.create("Java 입문", longDesc, 0, Level.LOW));
			assertEquals(ErrorCode.COURSE_DESCRIPTION_OUT_OF_RANGE, ex.getErrorCode());
		}

		@Test
		@DisplayName("가격이 음수이면 COURSE_PRICE_NEGATIVE 예외")
		void priceNegative() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Course.create("Java 입문", "설명입니다.", -1, Level.LOW));
			assertEquals(ErrorCode.COURSE_PRICE_NEGATIVE, ex.getErrorCode());
		}

		@Test
		@DisplayName("레벨이 null이면 INVALID_ARGUMENTS 예외")
		void levelNull() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Course.create("Java 입문", "설명입니다.", 0, null));
			assertEquals(ErrorCode.INVALID_ARGUMENTS, ex.getErrorCode());
		}
	}

	@Nested
	@DisplayName("createWithId()")
	class CreateWithId {

		@Test
		@DisplayName("정상 생성 — 타임스탬프 그대로 복원")
		void success() {
			LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 0, 0);
			LocalDateTime modifiedAt = LocalDateTime.of(2026, 1, 2, 0, 0);

			Course course = Course.createWithId(1L, "Java 입문", "설명입니다.", 10000, Level.LOW, createdAt, modifiedAt);

			assertEquals(1L, course.getId());
			assertEquals(createdAt, course.getCreatedAt());
			assertEquals(modifiedAt, course.getModifiedAt());
		}

		@Test
		@DisplayName("id가 null이면 INVALID_ARGUMENTS 예외")
		void idNull() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Course.createWithId(null, "Java 입문", "설명입니다.", 0, Level.LOW, null, null));
			assertEquals(ErrorCode.INVALID_ARGUMENTS, ex.getErrorCode());
		}
	}

	@Nested
	@DisplayName("update()")
	class Update {

		@Test
		@DisplayName("전달된 필드만 수정되고 나머지는 유지")
		void updatePartialFields() {
			Course course = Course.create("Java 입문", "설명입니다.", 10000, Level.LOW);

			course.update("Java 심화", null, null, null);

			assertEquals("Java 심화", course.getTitle());
			assertEquals("설명입니다.", course.getDescription());
			assertEquals(10000, course.getPrice());
			assertEquals(Level.LOW, course.getLevel());
		}

		@Test
		@DisplayName("모든 필드를 null로 전달하면 기존 값 유지")
		void allNullKeepsExistingValues() {
			Course course = Course.create("Java 입문", "설명입니다.", 10000, Level.LOW);

			course.update(null, null, null, null);

			assertEquals("Java 입문", course.getTitle());
			assertEquals("설명입니다.", course.getDescription());
			assertEquals(10000, course.getPrice());
			assertEquals(Level.LOW, course.getLevel());
		}

		@Test
		@DisplayName("수정 제목이 50자를 초과하면 COURSE_TITLE_OUT_OF_RANGE 예외")
		void updateTitleTooLong() {
			Course course = Course.create("Java 입문", "설명입니다.", 0, Level.LOW);

			LxpException ex = assertThrows(LxpException.class,
				() -> course.update("가".repeat(51), null, null, null));
			assertEquals(ErrorCode.COURSE_TITLE_OUT_OF_RANGE, ex.getErrorCode());
		}

		@Test
		@DisplayName("수정 가격이 음수이면 COURSE_PRICE_NEGATIVE 예외")
		void updatePriceNegative() {
			Course course = Course.create("Java 입문", "설명입니다.", 0, Level.LOW);

			LxpException ex = assertThrows(LxpException.class,
				() -> course.update(null, null, -1, null));
			assertEquals(ErrorCode.COURSE_PRICE_NEGATIVE, ex.getErrorCode());
		}
	}
}
