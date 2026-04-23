package com.lxp.controller.request;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("InstructorRegisterRequest 테스트")
class InstructorRegisterRequestTest {

	@Test
	@DisplayName("성공 - 이름이 비어있지 않으면 요청을 생성한다")
	void create() {
		assertThatNoException()
			.isThrownBy(() -> new InstructorRegisterRequest("김남준", "자바 강사"));
	}

	@Test
	@DisplayName("실패 - 이름이 공백이면 예외가 발생한다")
	void create_blankName() {
		assertThatThrownBy(() -> new InstructorRegisterRequest("   ", "자바 강사"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - 이름이 null이면 예외가 발생한다")
	void create_nullName() {
		assertThatThrownBy(() -> new InstructorRegisterRequest(null, "자바 강사"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}
}
