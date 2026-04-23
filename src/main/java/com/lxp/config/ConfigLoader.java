package com.lxp.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

public class ConfigLoader {

	private static final String CONFIG_FILE = "config.yml";
	private static final String DEFAULT_REPOSITORY_MODE = "inmemory";
	private static final String DEFAULT_URL = "";
	private static final String DEFAULT_USERNAME = "";
	private static final String DEFAULT_PASSWORD = "";

	public AppProperties load() {
		try (InputStream inputStream = resourceStream()) {
			Map<String, String> values = parse(inputStream);
			return new AppProperties(
				values.getOrDefault("repository.mode", DEFAULT_REPOSITORY_MODE),
				new AppProperties.DatabaseProperties(
					values.getOrDefault("db.url", DEFAULT_URL),
					values.getOrDefault("db.username", DEFAULT_USERNAME),
					values.getOrDefault("db.password", DEFAULT_PASSWORD)
				)
			);
		} catch (IOException e) {
			throw new LxpException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private InputStream resourceStream() {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
		if (inputStream == null) {
			throw new LxpException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		return inputStream;
	}

	private Map<String, String> parse(InputStream inputStream) throws IOException {
		Map<String, String> values = new LinkedHashMap<>();
		try (BufferedReader reader = new BufferedReader(
			new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			String currentSection = "";
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.isBlank() || line.stripLeading().startsWith("#")) {
					continue;
				}

				int indent = indentation(line);
				String trimmed = line.trim();
				if (trimmed.endsWith(":")) {
					currentSection = trimmed.substring(0, trimmed.length() - 1);
					continue;
				}

				String[] keyValue = trimmed.split(":", 2);
				if (keyValue.length != 2) {
					continue;
				}

				String key = keyValue[0].trim();
				String value = stripQuotes(keyValue[1].trim());
				String fullKey = indent > 0 && !currentSection.isBlank() ? currentSection + "." + key : key;
				values.put(fullKey, value);
			}
		}
		return values;
	}

	private int indentation(String line) {
		int indent = 0;
		while (indent < line.length() && line.charAt(indent) == ' ') {
			indent++;
		}
		return indent;
	}

	private String stripQuotes(String value) {
		if (value.length() >= 2) {
			boolean doubleQuoted = value.startsWith("\"") && value.endsWith("\"");
			boolean singleQuoted = value.startsWith("'") && value.endsWith("'");
			if (doubleQuoted || singleQuoted) {
				return value.substring(1, value.length() - 1);
			}
		}
		return value;
	}
}
