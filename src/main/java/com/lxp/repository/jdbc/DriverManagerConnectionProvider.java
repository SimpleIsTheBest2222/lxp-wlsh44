package com.lxp.repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.lxp.config.AppProperties;

public class DriverManagerConnectionProvider implements ConnectionProvider {

	private final String url;
	private final String username;
	private final String password;

	public DriverManagerConnectionProvider(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public static DriverManagerConnectionProvider from(AppProperties.DatabaseProperties properties) {
		return new DriverManagerConnectionProvider(
			properties.url(),
			properties.username(),
			properties.password()
		);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}
}
