package com.lxp.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("Instructor")
class InstructorTest {

	@Nested
	@DisplayName("create()")
	class Create {

		@Test
		@DisplayName("정상 생성")
		void success() {
			Instructor instructor = Instructor.create("홍길동", "10년 경력의 Java 강사입니다.");

			assertEquals("홍길동", instructor.getName());
			assertEquals("10년 경력의 Java 강사입니다.", instructor.getIntroduction());
		}

		@Test
		@DisplayName("소개가 null이면 빈 문자열로 저장")
		void introductionNullDefaultsToEmpty() {
			Instructor instructor = Instructor.create("홍길동", null);

			assertEquals("", instructor.getIntroduction());
		}

		@Test
		@DisplayName("이름이 null이면 INVALID_ARGUMENTS 예외")
		void nameNull() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Instructor.create(null, "소개입니다."));
			assertEquals(ErrorCode.INVALID_ARGUMENTS, ex.getErrorCode());
		}

		@Test
		@DisplayName("이름이 공백이면 INSTRUCTOR_NAME_OUT_OF_RANGE 예외")
		void nameBlank() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Instructor.create("   ", "소개입니다."));
			assertEquals(ErrorCode.INSTRUCTOR_NAME_OUT_OF_RANGE, ex.getErrorCode());
		}

		@Test
		@DisplayName("이름이 10자를 초과하면 INSTRUCTOR_NAME_OUT_OF_RANGE 예외")
		void nameTooLong() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Instructor.create("가".repeat(11), "소개입니다."));
			assertEquals(ErrorCode.INSTRUCTOR_NAME_OUT_OF_RANGE, ex.getErrorCode());
		}

		@Test
		@DisplayName("이름이 정확히 10자이면 성공")
		void nameMaxLength() {
			assertDoesNotThrow(() -> Instructor.create("가".repeat(10), "소개입니다."));
		}

		@Test
		@DisplayName("소개가 100자를 초과하면 INSTRUCTOR_INTRODUCTION_OUT_OF_RANGE 예외")
		void introductionTooLong() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Instructor.create("홍길동", "가".repeat(101)));
			assertEquals(ErrorCode.INSTRUCTOR_INTRODUCTION_OUT_OF_RANGE, ex.getErrorCode());
		}

		@Test
		@DisplayName("소개가 정확히 100자이면 성공")
		void introductionMaxLength() {
			assertDoesNotThrow(() -> Instructor.create("홍길동", "가".repeat(100)));
		}
	}

	@Nested
	@DisplayName("createWithId()")
	class CreateWithId {

		@Test
		@DisplayName("정상 생성")
		void success() {
			Instructor instructor = Instructor.createWithId(1L, "홍길동", "소개입니다.");

			assertEquals(1L, instructor.getId());
			assertEquals("홍길동", instructor.getName());
		}

		@Test
		@DisplayName("id가 null이면 INVALID_ARGUMENTS 예외")
		void idNull() {
			LxpException ex = assertThrows(LxpException.class,
				() -> Instructor.createWithId(null, "홍길동", "소개입니다."));
			assertEquals(ErrorCode.INVALID_ARGUMENTS, ex.getErrorCode());
		}
	}

	@Nested
	@DisplayName("update()")
	class Update {

		@Test
		@DisplayName("전달된 필드만 수정되고 나머지는 유지")
		void updatePartialFields() {
			Instructor instructor = Instructor.create("홍길동", "기존 소개입니다.");

			instructor.update("김철수", null);

			assertEquals("김철수", instructor.getName());
			assertEquals("기존 소개입니다.", instructor.getIntroduction());
		}

		@Test
		@DisplayName("모든 필드를 null로 전달하면 기존 값 유지")
		void allNullKeepsExistingValues() {
			Instructor instructor = Instructor.create("홍길동", "기존 소개입니다.");

			instructor.update(null, null);

			assertEquals("홍길동", instructor.getName());
			assertEquals("기존 소개입니다.", instructor.getIntroduction());
		}

		@Test
		@DisplayName("수정 이름이 10자를 초과하면 INSTRUCTOR_NAME_OUT_OF_RANGE 예외")
		void updateNameTooLong() {
			Instructor instructor = Instructor.create("홍길동", "소개입니다.");

			LxpException ex = assertThrows(LxpException.class,
				() -> instructor.update("가".repeat(11), null));
			assertEquals(ErrorCode.INSTRUCTOR_NAME_OUT_OF_RANGE, ex.getErrorCode());
		}
	}
}
