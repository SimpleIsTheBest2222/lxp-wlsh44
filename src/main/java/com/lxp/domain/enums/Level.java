package com.lxp.domain.enums;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

public enum Level {
	LOW, MIDDLE, HIGH;

	public static Level from(String input) {
		return switch (input.trim().toUpperCase()) {
			case "1", "LOW" -> LOW;
			case "2", "MIDDLE" -> MIDDLE;
			case "3", "HIGH" -> HIGH;
			default -> throw new LxpException(ErrorCode.INVALID_LEVEL);
		};
	}

	public String displayName() {
		return switch (this) {
			case LOW -> "입문";
			case MIDDLE -> "중급";
			case HIGH -> "고급";
		};
	}
}
