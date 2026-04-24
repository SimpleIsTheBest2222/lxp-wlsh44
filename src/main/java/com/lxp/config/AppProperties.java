package com.lxp.config;

public record AppProperties(
	String repositoryMode,
	DatabaseProperties db
) {

	public record DatabaseProperties(
		String url,
		String username,
		String password
	) {
	}
}
