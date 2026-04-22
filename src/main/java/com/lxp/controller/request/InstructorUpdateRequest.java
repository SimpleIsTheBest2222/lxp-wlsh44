package com.lxp.controller.request;

public record InstructorUpdateRequest(
	Long id,
	String name,
	String introduction
) {

	public InstructorUpdateRequest {
		name = normalize(name);
		introduction = normalize(introduction);
	}

	private static String normalize(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value;
	}
}
