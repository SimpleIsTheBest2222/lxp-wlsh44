package com.lxp.controller.request;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("InstructorUpdateRequest 테스트")
class InstructorUpdateRequestTest {

	@Test
	@DisplayName("성공 - 공백 입력은 null로 정규화한다")
	void create_normalizeBlankValue() {
		InstructorUpdateRequest request = new InstructorUpdateRequest(1L, "   ", null);

		assertThat(request.name()).isNull();
		assertThat(request.introduction()).isNull();
	}

	@Test
	@DisplayName("실패 - id가 null이면 예외가 발생한다")
	void create_nullId() {
		assertThatThrownBy(() -> new InstructorUpdateRequest(null, "김남준", "자바 강사"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - id가 0 이하이면 예외가 발생한다")
	void create_nonPositiveId() {
		assertThatThrownBy(() -> new InstructorUpdateRequest(0L, "김남준", "자바 강사"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}
}
