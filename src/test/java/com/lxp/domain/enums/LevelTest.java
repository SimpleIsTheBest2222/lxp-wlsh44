package com.lxp.domain.enums;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

@DisplayName("Level 테스트")
class LevelTest {

	@Test
	@DisplayName("성공 - 숫자 입력으로 난이도를 변환한다")
	void from_numericValue() {
		assertThat(Level.from("1")).isEqualTo(Level.LOW);
		assertThat(Level.from("2")).isEqualTo(Level.MIDDLE);
		assertThat(Level.from("3")).isEqualTo(Level.HIGH);
	}

	@Test
	@DisplayName("성공 - 문자 입력으로 난이도를 변환한다")
	void from_textValue() {
		assertThat(Level.from("low")).isEqualTo(Level.LOW);
		assertThat(Level.from("MIDDLE")).isEqualTo(Level.MIDDLE);
		assertThat(Level.from("High")).isEqualTo(Level.HIGH);
	}

	@Test
	@DisplayName("실패 - 공백 입력이면 INVALID_INPUT 예외가 발생한다")
	void from_blankValue() {
		assertThatThrownBy(() -> Level.from("   "))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_INPUT.getMessage());
	}

	@Test
	@DisplayName("실패 - 유효하지 않은 값이면 INVALID_LEVEL 예외가 발생한다")
	void from_invalidValue() {
		assertThatThrownBy(() -> Level.from("expert"))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.INVALID_LEVEL.getMessage());
	}
}
