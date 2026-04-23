package com.lxp.controller.request;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.domain.enums.ContentType;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("ContentRegisterRequest 테스트")
class ContentRegisterRequestTest {

	@Test
	@DisplayName("성공 - courseId 없이 콘텐츠 등록 요청을 생성하면 기본 타입은 TEXT다")
	void create() {
		ContentRegisterRequest request = new ContentRegisterRequest("원시타입", "설명", 1);

		assertThat(request.contentType()).isEqualTo(ContentType.TEXT);
		assertThat(request.courseId()).isNull();
	}

	@Test
	@DisplayName("실패 - seq가 0이면 예외가 발생한다")
	void create_invalidSeq() {
		assertThatThrownBy(() -> new ContentRegisterRequest(1L, "원시타입", "설명", 0))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}
}
