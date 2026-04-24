package com.lxp.config;

import com.lxp.repository.ContentRepository;
import com.lxp.repository.CourseRepository;
import com.lxp.repository.InstructorRepository;
import com.lxp.repository.inmemory.InMemoryContentRepository;
import com.lxp.repository.inmemory.InMemoryCourseRepository;
import com.lxp.repository.inmemory.InMemoryInstructorRepository;
import com.lxp.repository.jdbc.ConnectionProvider;
import com.lxp.repository.jdbc.DriverManagerConnectionProvider;
import com.lxp.repository.jdbc.JdbcContentRepository;
import com.lxp.repository.jdbc.JdbcCourseRepository;
import com.lxp.repository.jdbc.JdbcInstructorRepository;
import com.lxp.repository.jdbc.JdbcTemplate;

public class RepositoryConfig {

	private static final String JDBC = "jdbc";

	private final CourseRepository courseRepository;
	private final ContentRepository contentRepository;
	private final InstructorRepository instructorRepository;
	private final ConnectionProvider connectionProvider;
	private final boolean jdbcMode;

	public RepositoryConfig() {
		AppProperties appProperties = new ConfigLoader().load();
		this.jdbcMode = JDBC.equalsIgnoreCase(appProperties.repositoryMode());
		if (jdbcMode) {
			this.connectionProvider = DriverManagerConnectionProvider.from(appProperties.db());
			JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionProvider);
			this.courseRepository = new JdbcCourseRepository(jdbcTemplate);
			this.contentRepository = new JdbcContentRepository(jdbcTemplate);
			this.instructorRepository = new JdbcInstructorRepository(jdbcTemplate);
			return;
		}

		this.connectionProvider = null;
		this.courseRepository = new InMemoryCourseRepository();
		this.contentRepository = new InMemoryContentRepository();
		this.instructorRepository = new InMemoryInstructorRepository();
	}

	public CourseRepository courseRepository() {
		return courseRepository;
	}

	public ContentRepository contentRepository() {
		return contentRepository;
	}

	public InstructorRepository instructorRepository() {
		return instructorRepository;
	}

	public boolean isJdbcMode() {
		return jdbcMode;
	}

	public ConnectionProvider connectionProvider() {
		return connectionProvider;
	}
}
