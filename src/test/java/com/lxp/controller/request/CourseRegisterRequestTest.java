package com.lxp.controller.request;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.domain.enums.Level;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("CourseRegisterRequest 테스트")
class CourseRegisterRequestTest {

	@Test
	@DisplayName("성공 - 숫자 난이도 입력을 Level로 변환한다")
	void create_numericLevel() {
		CourseRegisterRequest request = new CourseRegisterRequest("Java 입문", "기초 문법", 10000, "1");

		assertThat(request.level()).isEqualTo(Level.LOW);
		assertThat(request.contents()).isEmpty();
	}

	@Test
	@DisplayName("성공 - 문자 난이도 입력을 Level로 변환한다")
	void create_textLevel() {
		CourseRegisterRequest request = new CourseRegisterRequest("Java 입문", "기초 문법", 10000, "middle");

		assertThat(request.level()).isEqualTo(Level.MIDDLE);
	}

	@Test
	@DisplayName("성공 - 콘텐츠 목록을 함께 담을 수 있다")
	void create_withContents() {
		CourseRegisterRequest request = new CourseRegisterRequest(
			"Java 입문",
			"기초 문법",
			10000,
			"LOW",
			List.of(new ContentRegisterRequest("원시타입", "설명", 1))
		);

		assertThat(request.contents()).hasSize(1);
		assertThat(request.contents().get(0).title()).isEqualTo("원시타입");
	}

	@Test
	@DisplayName("실패 - 콘텐츠 목록이 null이면 예외가 발생한다")
	void create_nullContents() {
		assertThatThrownBy(() -> new CourseRegisterRequest(
			"Java 입문",
			"기초 문법",
			10000,
			"LOW",
			null
		))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - 제목이 공백이면 예외가 발생한다")
	void create_blankTitle() {
		assertThatThrownBy(() -> new CourseRegisterRequest("   ", "기초 문법", 10000, "LOW"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - 가격이 음수이면 예외가 발생한다")
	void create_negativePrice() {
		assertThatThrownBy(() -> new CourseRegisterRequest("Java 입문", "기초 문법", -1, "LOW"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - 난이도 입력이 유효하지 않으면 예외가 발생한다")
	void create_invalidLevel() {
		assertThatThrownBy(() -> new CourseRegisterRequest("Java 입문", "기초 문법", 10000, "expert"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_LEVEL.getMessage());
	}
}
