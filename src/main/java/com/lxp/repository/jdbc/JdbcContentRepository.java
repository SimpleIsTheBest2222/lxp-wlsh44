package com.lxp.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.lxp.domain.Content;
import com.lxp.domain.enums.ContentType;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.ContentRepository;
import com.lxp.repository.entity.ContentEntity;
import com.lxp.repository.entity.enums.EntityStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcContentRepository implements ContentRepository {

	private static final String ACTIVE = EntityStatus.ACTIVE.name();
	private static final String DELETED = EntityStatus.DELETED.name();

	private final JdbcTemplate jdbcTemplate;

	@Override
	public Optional<Content> findById(Long id) {
		String sql = """
			SELECT id, course_id, title, body, content_type, seq, status, created_at, modified_at
			FROM contents
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
	public List<Content> findAll() {
		String sql = """
			SELECT id, course_id, title, body, content_type, seq, status, created_at, modified_at
			FROM contents
			WHERE status = ?
			ORDER BY course_id, seq, id
			""";
		return jdbcTemplate.query(
			sql,
			preparedStatement -> preparedStatement.setString(1, ACTIVE),
			this::mapRow
		);
	}

	@Override
	public Content save(Content content) {
		if (content.getId() == null) {
			return saveNew(content);
		}
		return saveExisting(content);
	}

	@Override
	public void deleteById(Long id) {
		String sql = """
			UPDATE contents
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

	private Content saveNew(Content content) {
		LocalDateTime now = LocalDateTime.now();
		String sql = """
			INSERT INTO contents (course_id, title, body, content_type, seq, status, created_at, modified_at)
			VALUES (?, ?, ?, ?, ?, ?, ?, ?)
			""";
		long id = jdbcTemplate.insert(sql, preparedStatement -> {
			preparedStatement.setLong(1, content.getCourseId());
			preparedStatement.setString(2, content.getTitle());
			preparedStatement.setString(3, content.getBody());
			preparedStatement.setString(4, content.getContentType().name());
			preparedStatement.setInt(5, content.getSeq());
			preparedStatement.setString(6, ACTIVE);
			preparedStatement.setTimestamp(7, Timestamp.valueOf(now));
			preparedStatement.setTimestamp(8, Timestamp.valueOf(now));
		});
		return Content.createWithId(
			id,
			content.getCourseId(),
			content.getTitle(),
			content.getBody(),
			content.getContentType(),
			content.getSeq()
		);
	}

	private Content saveExisting(Content content) {
		String sql = """
			UPDATE contents
			SET title = ?, body = ?, content_type = ?, seq = ?, modified_at = ?
			WHERE id = ? AND status = ?
			""";
		int updated = jdbcTemplate.update(sql, preparedStatement -> {
			preparedStatement.setString(1, content.getTitle());
			preparedStatement.setString(2, content.getBody());
			preparedStatement.setString(3, content.getContentType().name());
			preparedStatement.setInt(4, content.getSeq());
			preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
			preparedStatement.setLong(6, content.getId());
			preparedStatement.setString(7, ACTIVE);
		});
		if (updated == 0) {
			throw new LxpException(ErrorCode.NOT_FOUND_CONTENT);
		}
		return content;
	}

	private Content mapRow(ResultSet resultSet) throws SQLException {
		return ContentEntity.restore(
			resultSet.getLong("id"),
			resultSet.getLong("course_id"),
			resultSet.getString("title"),
			resultSet.getString("body"),
			ContentType.valueOf(resultSet.getString("content_type")),
			resultSet.getInt("seq"),
			EntityStatus.valueOf(resultSet.getString("status")),
			resultSet.getTimestamp("created_at").toLocalDateTime(),
			resultSet.getTimestamp("modified_at").toLocalDateTime()
		).toDomain();
	}
}
