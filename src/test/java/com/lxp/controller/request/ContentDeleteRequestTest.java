package com.lxp.controller.request;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("ContentDeleteRequest 테스트")
class ContentDeleteRequestTest {

	@Test
	@DisplayName("성공 - 콘텐츠 id를 담는다")
	void create() {
		ContentDeleteRequest request = new ContentDeleteRequest(1L);

		assertThat(request.id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("실패 - id가 0이면 예외가 발생한다")
	void create_invalidId() {
		assertThatThrownBy(() -> new ContentDeleteRequest(0L))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - id가 null이면 예외가 발생한다")
	void create_nullId() {
		assertThatThrownBy(() -> new ContentDeleteRequest(null))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}
}
