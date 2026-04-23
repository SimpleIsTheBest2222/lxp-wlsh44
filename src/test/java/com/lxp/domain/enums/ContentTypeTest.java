package com.lxp.domain.enums;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("ContentType 테스트")
class ContentTypeTest {

	@Test
	@DisplayName("성공 - 숫자 입력으로 콘텐츠 타입을 변환한다")
	void from_numericValue() {
		assertThat(ContentType.from("1")).isEqualTo(ContentType.VIDEO);
		assertThat(ContentType.from("2")).isEqualTo(ContentType.TEXT);
		assertThat(ContentType.from("3")).isEqualTo(ContentType.FILE);
	}

	@Test
	@DisplayName("성공 - 문자 입력으로 콘텐츠 타입을 변환한다")
	void from_textValue() {
		assertThat(ContentType.from("video")).isEqualTo(ContentType.VIDEO);
		assertThat(ContentType.from("TEXT")).isEqualTo(ContentType.TEXT);
		assertThat(ContentType.from("File")).isEqualTo(ContentType.FILE);
	}

	@Test
	@DisplayName("실패 - 공백 입력이면 INVALID_INPUT 예외가 발생한다")
	void from_blankValue() {
		assertThatThrownBy(() -> ContentType.from("   "))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - 유효하지 않은 값이면 INVALID_CONTENT_TYPE 예외가 발생한다")
	void from_invalidValue() {
		assertThatThrownBy(() -> ContentType.from("link"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_CONTENT_TYPE.getMessage());
	}
}
