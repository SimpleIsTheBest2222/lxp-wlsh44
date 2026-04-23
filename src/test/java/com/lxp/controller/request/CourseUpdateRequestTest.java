package com.lxp.controller.request;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.domain.enums.Level;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("CourseUpdateRequest 테스트")
class CourseUpdateRequestTest {

	@Test
	@DisplayName("성공 - 빈 값은 null로 정규화하고 입력값은 타입으로 변환한다")
	void create() {
		CourseUpdateRequest request = new CourseUpdateRequest(1L, "Java 심화", "  ", "120000", "MIDDLE");

		assertThat(request.id()).isEqualTo(1L);
		assertThat(request.title()).isEqualTo("Java 심화");
		assertThat(request.description()).isNull();
		assertThat(request.price()).isEqualTo(120000);
		assertThat(request.level()).isEqualTo(Level.MIDDLE);
	}

	@Test
	@DisplayName("실패 - id가 null이면 예외가 발생한다")
	void create_nullId() {
		assertThatThrownBy(() -> new CourseUpdateRequest(null, "", "", "", ""))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - 가격이 숫자가 아니면 예외가 발생한다")
	void create_invalidPrice() {
		assertThatThrownBy(() -> new CourseUpdateRequest(1L, "", "", "abc", ""))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - 난이도가 유효하지 않으면 예외가 발생한다")
	void create_invalidLevel() {
		assertThatThrownBy(() -> new CourseUpdateRequest(1L, "", "", "", "expert"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_LEVEL.getMessage());
	}
}
