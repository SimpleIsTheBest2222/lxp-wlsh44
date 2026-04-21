package com.lxp.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
			// given
			String title = "Java 입문";
			String description = "Java 기초 강의입니다.";
			int price = 10000;
			Level level = Level.LOW;

			// when
			Course course = Course.create(title, description, price, level);

			// then
			assertThat(course.getTitle()).isEqualTo(title);
			assertThat(course.getDescription()).isEqualTo(description);
			assertThat(course.getPrice()).isEqualTo(price);
			assertThat(course.getLevel()).isEqualTo(level);
			assertThat(course.getId()).isNull();
		}

		@Test
		@DisplayName("가격이 0이면 성공")
		void priceZeroSuccess() {
			// given
			int zeroPrice = 0;

			// when & then
			assertThatNoException()
				.isThrownBy(() -> Course.create("Java 입문", "설명입니다.", zeroPrice, Level.LOW));
		}

		@Test
		@DisplayName("제목이 null이면 INVALID_ARGUMENTS 예외")
		void titleNull() {
			// when & then
			assertThatThrownBy(() -> Course.create(null, "설명입니다.", 0, Level.LOW))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INVALID_ARGUMENTS.getMessage());
		}

		@Test
		@DisplayName("제목이 공백이면 COURSE_TITLE_OUT_OF_RANGE 예외")
		void titleBlank() {
			// given
			String blankTitle = "   ";

			// when & then
			assertThatThrownBy(() -> Course.create(blankTitle, "설명입니다.", 0, Level.LOW))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.COURSE_TITLE_OUT_OF_RANGE.getMessage());
		}

		@Test
		@DisplayName("제목이 50자를 초과하면 COURSE_TITLE_OUT_OF_RANGE 예외")
		void titleTooLong() {
			// given
			String tooLongTitle = "가".repeat(51);

			// when & then
			assertThatThrownBy(() -> Course.create(tooLongTitle, "설명입니다.", 0, Level.LOW))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.COURSE_TITLE_OUT_OF_RANGE.getMessage());
		}

		@Test
		@DisplayName("제목이 정확히 50자이면 성공")
		void titleMaxLength() {
			// given
			String maxLengthTitle = "가".repeat(50);

			// when & then
			assertThatNoException()
				.isThrownBy(() -> Course.create(maxLengthTitle, "설명입니다.", 0, Level.LOW));
		}

		@Test
		@DisplayName("설명이 null이면 INVALID_ARGUMENTS 예외")
		void descriptionNull() {
			// when & then
			assertThatThrownBy(() -> Course.create("Java 입문", null, 0, Level.LOW))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INVALID_ARGUMENTS.getMessage());
		}

		@Test
		@DisplayName("설명이 공백이면 COURSE_DESCRIPTION_OUT_OF_RANGE 예외")
		void descriptionBlank() {
			// given
			String blankDescription = "   ";

			// when & then
			assertThatThrownBy(() -> Course.create("Java 입문", blankDescription, 0, Level.LOW))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.COURSE_DESCRIPTION_OUT_OF_RANGE.getMessage());
		}

		@Test
		@DisplayName("설명이 200자를 초과하면 COURSE_DESCRIPTION_OUT_OF_RANGE 예외")
		void descriptionTooLong() {
			// given
			String tooLongDescription = "가".repeat(201);

			// when & then
			assertThatThrownBy(() -> Course.create("Java 입문", tooLongDescription, 0, Level.LOW))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.COURSE_DESCRIPTION_OUT_OF_RANGE.getMessage());
		}

		@Test
		@DisplayName("가격이 음수이면 COURSE_PRICE_NEGATIVE 예외")
		void priceNegative() {
			// given
			int negativePrice = -1;

			// when & then
			assertThatThrownBy(() -> Course.create("Java 입문", "설명입니다.", negativePrice, Level.LOW))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.COURSE_PRICE_NEGATIVE.getMessage());
		}

		@Test
		@DisplayName("레벨이 null이면 INVALID_ARGUMENTS 예외")
		void levelNull() {
			// when & then
			assertThatThrownBy(() -> Course.create("Java 입문", "설명입니다.", 0, null))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.INVALID_ARGUMENTS.getMessage());
		}
	}

	@Nested
	@DisplayName("createWithId()")
	class CreateWithId {

		@Test
		@DisplayName("정상 생성 — 타임스탬프 그대로 복원")
		void success() {
			// given
			Long id = 1L;
			LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 0, 0);
			LocalDateTime modifiedAt = LocalDateTime.of(2026, 1, 2, 0, 0);

			// when
			Course course = Course.createWithId(id, "Java 입문", "설명입니다.", 10000, Level.LOW, createdAt, modifiedAt);

			// then
			assertThat(course.getId()).isEqualTo(id);
			assertThat(course.getCreatedAt()).isEqualTo(createdAt);
			assertThat(course.getModifiedAt()).isEqualTo(modifiedAt);
		}

		@Test
		@DisplayName("id가 null이면 INVALID_ARGUMENTS 예외")
		void idNull() {
			// when & then
			assertThatThrownBy(() -> Course.createWithId(null, "Java 입문", "설명입니다.", 0, Level.LOW, null, null))
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
			Course course = Course.create("Java 입문", "설명입니다.", 10000, Level.LOW);
			String newTitle = "Java 심화";

			// when
			course.update(newTitle, null, null, null);

			// then
			assertThat(course.getTitle()).isEqualTo(newTitle);
			assertThat(course.getDescription()).isEqualTo("설명입니다.");
			assertThat(course.getPrice()).isEqualTo(10000);
			assertThat(course.getLevel()).isEqualTo(Level.LOW);
		}

		@Test
		@DisplayName("모든 필드를 null로 전달하면 기존 값 유지")
		void allNullKeepsExistingValues() {
			// given
			Course course = Course.create("Java 입문", "설명입니다.", 10000, Level.LOW);

			// when
			course.update(null, null, null, null);

			// then
			assertThat(course.getTitle()).isEqualTo("Java 입문");
			assertThat(course.getDescription()).isEqualTo("설명입니다.");
			assertThat(course.getPrice()).isEqualTo(10000);
			assertThat(course.getLevel()).isEqualTo(Level.LOW);
		}

		@Test
		@DisplayName("수정 제목이 50자를 초과하면 COURSE_TITLE_OUT_OF_RANGE 예외")
		void updateTitleTooLong() {
			// given
			Course course = Course.create("Java 입문", "설명입니다.", 0, Level.LOW);
			String tooLongTitle = "가".repeat(51);

			// when & then
			assertThatThrownBy(() -> course.update(tooLongTitle, null, null, null))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.COURSE_TITLE_OUT_OF_RANGE.getMessage());
		}

		@Test
		@DisplayName("수정 가격이 음수이면 COURSE_PRICE_NEGATIVE 예외")
		void updatePriceNegative() {
			// given
			Course course = Course.create("Java 입문", "설명입니다.", 0, Level.LOW);
			int negativePrice = -1;

			// when & then
			assertThatThrownBy(() -> course.update(null, null, negativePrice, null))
				.isInstanceOf(LxpException.class)
				.hasMessage(ErrorCode.COURSE_PRICE_NEGATIVE.getMessage());
		}
	}
}
