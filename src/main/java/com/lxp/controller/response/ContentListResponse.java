package com.lxp.controller.response;

import java.util.List;
import java.util.Objects;

public record ContentListResponse(
	List<ContentSummaryResponse> contents
) {
	public ContentListResponse {
		contents = List.copyOf(Objects.requireNonNullElse(contents, List.of()));
	}

	public boolean isEmpty() {
		return contents.isEmpty();
	}
}
