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
		ContentRegisterRequest request = new ContentRegisterRequest("원시타입", "설명");

		assertThat(request.contentType()).isEqualTo(ContentType.TEXT);
		assertThat(request.courseId()).isNull();
	}

	@Test
	@DisplayName("성공 - 문자열 콘텐츠 타입 입력을 변환한다")
	void create_withContentTypeText() {
		ContentRegisterRequest request = new ContentRegisterRequest(1L, "원시타입", "설명", "1");

		assertThat(request.courseId()).isEqualTo(1L);
		assertThat(request.contentType()).isEqualTo(ContentType.VIDEO);
	}

	@Test
	@DisplayName("실패 - 내용이 공백이면 예외가 발생한다")
	void create_blankBody() {
		assertThatThrownBy(() -> new ContentRegisterRequest("원시타입", "   "))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - 콘텐츠 타입이 null이면 예외가 발생한다")
	void create_nullContentType() {
		assertThatThrownBy(() -> new ContentRegisterRequest(1L, "원시타입", "설명", (ContentType)null))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - 제목이 공백이면 예외가 발생한다")
	void create_blankTitle() {
		assertThatThrownBy(() -> new ContentRegisterRequest("   ", "설명"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - courseId가 0이면 예외가 발생한다")
	void create_invalidCourseId() {
		assertThatThrownBy(() -> new ContentRegisterRequest(0L, "원시타입", "설명", "TEXT"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - 콘텐츠 타입 문자열이 유효하지 않으면 예외가 발생한다")
	void create_invalidContentTypeText() {
		assertThatThrownBy(() -> new ContentRegisterRequest(1L, "원시타입", "설명", "LINK"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_CONTENT_TYPE.getMessage());
	}
}
