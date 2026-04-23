package com.lxp.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.lxp.domain.Instructor;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.InstructorRepository;
import com.lxp.repository.entity.InstructorEntity;
import com.lxp.repository.entity.enums.EntityStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcInstructorRepository implements InstructorRepository {

	private static final String ACTIVE = EntityStatus.ACTIVE.name();
	private static final String DELETED = EntityStatus.DELETED.name();

	private final JdbcTemplate jdbcTemplate;

	@Override
	public Optional<Instructor> findById(Long id) {
		String sql = """
			SELECT id, name, introduction, status, created_at, modified_at
			FROM instructors
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
	public List<Instructor> findAll() {
		String sql = """
			SELECT id, name, introduction, status, created_at, modified_at
			FROM instructors
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
	public Instructor save(Instructor instructor) {
		if (instructor.getId() == null) {
			return saveNew(instructor);
		}
		return saveExisting(instructor);
	}

	@Override
	public void deleteById(Long id) {
		String sql = """
			UPDATE instructors
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

	private Instructor saveNew(Instructor instructor) {
		LocalDateTime now = LocalDateTime.now();
		String sql = """
			INSERT INTO instructors (name, introduction, status, created_at, modified_at)
			VALUES (?, ?, ?, ?, ?)
			""";
		long id = jdbcTemplate.insert(sql, preparedStatement -> {
			preparedStatement.setString(1, instructor.getName());
			preparedStatement.setString(2, instructor.getIntroduction());
			preparedStatement.setString(3, ACTIVE);
			preparedStatement.setTimestamp(4, Timestamp.valueOf(now));
			preparedStatement.setTimestamp(5, Timestamp.valueOf(now));
		});
		return Instructor.createWithId(id, instructor.getName(), instructor.getIntroduction());
	}

	private Instructor saveExisting(Instructor instructor) {
		LocalDateTime now = LocalDateTime.now();
		String sql = """
			UPDATE instructors
			SET name = ?, introduction = ?, modified_at = ?
			WHERE id = ? AND status = ?
			""";
		int updated = jdbcTemplate.update(sql, preparedStatement -> {
			preparedStatement.setString(1, instructor.getName());
			preparedStatement.setString(2, instructor.getIntroduction());
			preparedStatement.setTimestamp(3, Timestamp.valueOf(now));
			preparedStatement.setLong(4, instructor.getId());
			preparedStatement.setString(5, ACTIVE);
		});
		if (updated == 0) {
			throw new LxpException(ErrorCode.NOT_FOUND_INSTRUCTOR);
		}
		return instructor;
	}

	private Instructor mapRow(ResultSet resultSet) throws SQLException {
		return InstructorEntity.restore(
			resultSet.getLong("id"),
			resultSet.getString("name"),
			resultSet.getString("introduction"),
			EntityStatus.valueOf(resultSet.getString("status")),
			resultSet.getTimestamp("created_at").toLocalDateTime(),
			resultSet.getTimestamp("modified_at").toLocalDateTime()
		).toDomain();
	}
}
