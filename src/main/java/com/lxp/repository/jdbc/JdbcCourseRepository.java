package com.lxp.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.lxp.domain.Course;
import com.lxp.domain.enums.Level;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.CourseRepository;
import com.lxp.repository.entity.CourseEntity;
import com.lxp.repository.entity.enums.EntityStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcCourseRepository implements CourseRepository {

	private static final String ACTIVE = EntityStatus.ACTIVE.name();
	private static final String DELETED = EntityStatus.DELETED.name();

	private final JdbcTemplate jdbcTemplate;

	@Override
	public Optional<Course> findById(Long id) {
		String sql = """
			SELECT id, instructor_id, title, description, price, level, status, created_at, modified_at
			FROM courses
			WHERE id = ? AND status = ?
			""";
		return jdbcTemplate.queryForObject(
			sql,
			preparedStatement -> {
				preparedStatement.setLong(1, id);
				preparedStatement.setString(2, ACTIVE);
			},
			this::mapRow
		);
	}

	@Override
	public List<Course> findAll() {
		String sql = """
			SELECT id, instructor_id, title, description, price, level, status, created_at, modified_at
			FROM courses
			WHERE status = ?
			ORDER BY id
			""";
		return jdbcTemplate.query(
			sql,
			preparedStatement -> preparedStatement.setString(1, ACTIVE),
			this::mapRow
		);
	}

	@Override
	public Course save(Course course) {
		if (course.getId() == null) {
			return saveNew(course);
		}
		return saveExisting(course);
	}

	@Override
	public void deleteById(Long id) {
		String sql = """
			UPDATE courses
			SET status = ?, modified_at = ?
			WHERE id = ? AND status = ?
			""";
		jdbcTemplate.update(sql, preparedStatement -> {
			preparedStatement.setString(1, DELETED);
			preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
			preparedStatement.setLong(3, id);
			preparedStatement.setString(4, ACTIVE);
		});
	}

	private Course saveNew(Course course) {
		LocalDateTime now = LocalDateTime.now();
		String sql = """
			INSERT INTO courses (instructor_id, title, description, price, level, status, created_at, modified_at)
			VALUES (?, ?, ?, ?, ?, ?, ?, ?)
			""";
		long id = jdbcTemplate.insert(sql, preparedStatement -> {
			preparedStatement.setLong(1, course.getInstructorId());
			preparedStatement.setString(2, course.getTitle());
			preparedStatement.setString(3, course.getDescription());
			preparedStatement.setInt(4, course.getPrice());
			preparedStatement.setString(5, course.getLevel().name());
			preparedStatement.setString(6, ACTIVE);
			preparedStatement.setTimestamp(7, Timestamp.valueOf(now));
			preparedStatement.setTimestamp(8, Timestamp.valueOf(now));
		});
		return Course.createWithId(
			id,
			course.getInstructorId(),
			course.getTitle(),
			course.getDescription(),
			course.getPrice(),
			course.getLevel(),
			now,
			now
		);
	}

	private Course saveExisting(Course course) {
		LocalDateTime now = LocalDateTime.now();
		String sql = """
			UPDATE courses
			SET instructor_id = ?, title = ?, description = ?, price = ?, level = ?, modified_at = ?
			WHERE id = ? AND status = ?
			""";
		int updated = jdbcTemplate.update(sql, preparedStatement -> {
			preparedStatement.setLong(1, course.getInstructorId());
			preparedStatement.setString(2, course.getTitle());
			preparedStatement.setString(3, course.getDescription());
			preparedStatement.setInt(4, course.getPrice());
			preparedStatement.setString(5, course.getLevel().name());
			preparedStatement.setTimestamp(6, Timestamp.valueOf(now));
			preparedStatement.setLong(7, course.getId());
			preparedStatement.setString(8, ACTIVE);
		});
		if (updated == 0) {
			throw new LxpException(ErrorCode.NOT_FOUND_COURSE);
		}
		return Course.createWithId(
			course.getId(),
			course.getInstructorId(),
			course.getTitle(),
			course.getDescription(),
			course.getPrice(),
			course.getLevel(),
			course.getCreatedAt(),
			now
		);
	}

	private Course mapRow(ResultSet resultSet) throws SQLException {
		return CourseEntity.restore(
			resultSet.getLong("id"),
			resultSet.getLong("instructor_id"),
			resultSet.getString("title"),
			resultSet.getString("description"),
			resultSet.getInt("price"),
			Level.valueOf(resultSet.getString("level")),
			EntityStatus.valueOf(resultSet.getString("status")),
			resultSet.getTimestamp("created_at").toLocalDateTime(),
			resultSet.getTimestamp("modified_at").toLocalDateTime()
		).toDomain();
	}
}
