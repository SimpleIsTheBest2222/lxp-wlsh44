package com.lxp.domain;

import static org.assertj.core.api.Assertions.*;

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
			// given
			String name = "홍길동";
			String introduction = "10년 경력의 Java 강사입니다.";

			// when
			Instructor instructor = Instructor.create(name, introduction);

			// then
			assertThat(instructor.getName()).isEqualTo(name);
			assertThat(instructor.getIntroduction()).isEqualTo(introduction);
		}

		@Test
		@DisplayName("소개가 null이면 빈 문자열로 저장")
		void introductionNullDefaultsToEmpty() {
			// when
			Instructor instructor = Instructor.create("홍길동", null);

			// then
			assertThat(instructor.getIntroduction()).isEmpty();
		}

		@Test
		@DisplayName("이름이 null이면 INVALID_ARGUMENTS 예외")
		void nameNull() {
			// when & then
			assertThatThrownBy(() -> Instructor.create(null, "소개입니다."))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INVALID_ARGUMENTS.getMessage());
		}

		@Test
		@DisplayName("이름이 공백이면 INSTRUCTOR_NAME_OUT_OF_RANGE 예외")
		void nameBlank() {
			// given
			String blankName = "   ";

			// when & then
			assertThatThrownBy(() -> Instructor.create(blankName, "소개입니다."))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INSTRUCTOR_NAME_OUT_OF_RANGE.getMessage());
		}

		@Test
		@DisplayName("이름이 10자를 초과하면 INSTRUCTOR_NAME_OUT_OF_RANGE 예외")
		void nameTooLong() {
			// given
			String tooLongName = "가".repeat(11);

			// when & then
			assertThatThrownBy(() -> Instructor.create(tooLongName, "소개입니다."))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INSTRUCTOR_NAME_OUT_OF_RANGE.getMessage());
		}

		@Test
		@DisplayName("이름이 정확히 10자이면 성공")
		void nameMaxLength() {
			// given
			String maxLengthName = "가".repeat(10);

			// when & then
			assertThatNoException()
				.isThrownBy(() -> Instructor.create(maxLengthName, "소개입니다."));
		}

		@Test
		@DisplayName("소개가 100자를 초과하면 INSTRUCTOR_INTRODUCTION_OUT_OF_RANGE 예외")
		void introductionTooLong() {
			// given
			String tooLongIntroduction = "가".repeat(101);

			// when & then
			assertThatThrownBy(() -> Instructor.create("홍길동", tooLongIntroduction))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INSTRUCTOR_INTRODUCTION_OUT_OF_RANGE.getMessage());
		}

		@Test
		@DisplayName("소개가 정확히 100자이면 성공")
		void introductionMaxLength() {
			// given
			String maxLengthIntroduction = "가".repeat(100);

			// when & then
			assertThatNoException()
				.isThrownBy(() -> Instructor.create("홍길동", maxLengthIntroduction));
		}
	}

	@Nested
	@DisplayName("createWithId()")
	class CreateWithId {

		@Test
		@DisplayName("정상 생성")
		void success() {
			// given
			Long id = 1L;
			String name = "홍길동";

			// when
			Instructor instructor = Instructor.createWithId(id, name, "소개입니다.");

			// then
			assertThat(instructor.getId()).isEqualTo(id);
			assertThat(instructor.getName()).isEqualTo(name);
		}

		@Test
		@DisplayName("id가 null이면 INVALID_ARGUMENTS 예외")
		void idNull() {
			// when & then
			assertThatThrownBy(() -> Instructor.createWithId(null, "홍길동", "소개입니다."))
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
			Instructor instructor = Instructor.create("홍길동", "기존 소개입니다.");
			String newName = "김철수";

			// when
			instructor.update(newName, null);

			// then
			assertThat(instructor.getName()).isEqualTo(newName);
			assertThat(instructor.getIntroduction()).isEqualTo("기존 소개입니다.");
		}

		@Test
		@DisplayName("모든 필드를 null로 전달하면 기존 값 유지")
		void allNullKeepsExistingValues() {
			// given
			Instructor instructor = Instructor.create("홍길동", "기존 소개입니다.");

			// when
			instructor.update(null, null);

			// then
			assertThat(instructor.getName()).isEqualTo("홍길동");
			assertThat(instructor.getIntroduction()).isEqualTo("기존 소개입니다.");
		}

		@Test
		@DisplayName("수정 이름이 10자를 초과하면 INSTRUCTOR_NAME_OUT_OF_RANGE 예외")
		void updateNameTooLong() {
			// given
			Instructor instructor = Instructor.create("홍길동", "소개입니다.");
			String tooLongName = "가".repeat(11);

			// when & then
			assertThatThrownBy(() -> instructor.update(tooLongName, null))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INSTRUCTOR_NAME_OUT_OF_RANGE.getMessage());
		}
	}
}
