package com.lxp.domain.enums;

public enum Level {
	LOW, MIDDLE, HIGH;

	public String displayName() {
		return switch (this) {
			case LOW -> "입문";
			case MIDDLE -> "중급";
			case HIGH -> "고급";
		};
	}
}
