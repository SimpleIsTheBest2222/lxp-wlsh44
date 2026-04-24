package com.lxp.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcTemplate {

	private final ConnectionProvider connectionProvider;

	public int update(String sql, PreparedStatementSetter statementSetter) {
		try (
			Connection connection = connectionProvider.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql)
		) {
			statementSetter.setValues(preparedStatement);
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new LxpException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	public long insert(String sql, PreparedStatementSetter statementSetter) {
		try (
			Connection connection = connectionProvider.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
		) {
			statementSetter.setValues(preparedStatement);
			preparedStatement.executeUpdate();
			return generatedKey(preparedStatement);
		} catch (SQLException e) {
			throw new LxpException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	public <T> List<T> query(String sql, PreparedStatementSetter statementSetter, RowMapper<T> rowMapper) {
		try (
			Connection connection = connectionProvider.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql)
		) {
			statementSetter.setValues(preparedStatement);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				List<T> rows = new ArrayList<>();
				while (resultSet.next()) {
					rows.add(rowMapper.map(resultSet));
				}
				return rows;
			}
		} catch (SQLException e) {
			throw new LxpException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	public <T> Optional<T> queryForObject(
		String sql,
		PreparedStatementSetter statementSetter,
		RowMapper<T> rowMapper
	) {
		List<T> rows = query(sql, statementSetter, rowMapper);
		if (rows.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(rows.get(0));
	}

	private long generatedKey(PreparedStatement preparedStatement) throws SQLException {
		try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				return generatedKeys.getLong(1);
			}
			throw new LxpException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
