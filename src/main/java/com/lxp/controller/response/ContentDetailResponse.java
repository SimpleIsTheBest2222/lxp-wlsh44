package com.lxp.controller.response;

public record ContentDetailResponse(
	Long id,
	String title,
	String body,
	String contentType
) {
}
