package com.lxp.repository.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.junit.jupiter.api.Assumptions;

import com.lxp.config.AppProperties;
import com.lxp.config.ConfigLoader;

public abstract class MysqlIntegrationSupport {

	protected static final String TEST_CONFIG = "config-test-mysql.yml";

	protected AppProperties.DatabaseProperties databaseProperties() {
		return new ConfigLoader().load(TEST_CONFIG).db();
	}

	protected ConnectionProvider connectionProvider() {
		return DriverManagerConnectionProvider.from(databaseProperties());
	}

	protected JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(connectionProvider());
	}

	protected void assumeMysqlAvailable() {
		AppProperties.DatabaseProperties properties = databaseProperties();
		try (Connection ignored = DriverManager.getConnection(
			properties.url(),
			properties.username(),
			properties.password()
		)) {
			// no-op
		} catch (SQLException exception) {
			Assumptions.assumeTrue(false, "MySQL test database unavailable: " + exception.getMessage());
		}
	}

	protected void resetDatabase() {
		assumeMysqlAvailable();
		AppProperties.DatabaseProperties properties = databaseProperties();
		try (
			Connection connection = DriverManager.getConnection(
				properties.url(),
				properties.username(),
				properties.password()
			);
			Statement statement = connection.createStatement()
		) {
			statement.execute("SET FOREIGN_KEY_CHECKS = 0");
			statement.execute("DROP TABLE IF EXISTS contents");
			statement.execute("DROP TABLE IF EXISTS courses");
			statement.execute("DROP TABLE IF EXISTS instructors");
			statement.execute("SET FOREIGN_KEY_CHECKS = 1");

			for (String sql : ddlStatements()) {
				statement.execute(sql);
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("MySQL test schema setup failed", exception);
		}
	}

	private String[] ddlStatements() {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ddl.sql")) {
			if (inputStream == null) {
				throw new IllegalStateException("ddl.sql not found");
			}
			String ddl = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
			return Arrays.stream(ddl.split(";"))
				.map(String::trim)
				.filter(sql -> !sql.isBlank())
				.toArray(String[]::new);
		} catch (IOException exception) {
			throw new IllegalStateException("Failed to read ddl.sql", exception);
		}
	}
}
