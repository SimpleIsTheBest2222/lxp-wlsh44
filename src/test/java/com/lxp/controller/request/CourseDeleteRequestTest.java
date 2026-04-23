package com.lxp.controller.request;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("CourseDeleteRequest 테스트")
class CourseDeleteRequestTest {

	@Test
	@DisplayName("성공 - 강의 id를 담는다")
	void create() {
		CourseDeleteRequest request = new CourseDeleteRequest(1L);

		assertThat(request.id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("실패 - id가 0이면 예외가 발생한다")
	void create_invalidId() {
		assertThatThrownBy(() -> new CourseDeleteRequest(0L))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}
}
