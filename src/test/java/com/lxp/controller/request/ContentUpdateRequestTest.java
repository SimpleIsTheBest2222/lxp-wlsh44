package com.lxp.controller.request;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("ContentUpdateRequest 테스트")
class ContentUpdateRequestTest {

	@Test
	@DisplayName("성공 - 빈 값은 null로 정규화한다")
	void create() {
		ContentUpdateRequest request = new ContentUpdateRequest(1L, "Stream", "  ");

		assertThat(request.id()).isEqualTo(1L);
		assertThat(request.title()).isEqualTo("Stream");
		assertThat(request.body()).isNull();
	}

	@Test
	@DisplayName("실패 - id가 null이면 예외가 발생한다")
	void create_nullId() {
		assertThatThrownBy(() -> new ContentUpdateRequest(null, "Stream", "설명"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - id가 0이면 예외가 발생한다")
	void create_invalidId() {
		assertThatThrownBy(() -> new ContentUpdateRequest(0L, "Stream", "설명"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}
}
