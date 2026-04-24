package com.lxp.repository.jdbc;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.transaction.TransactionSynchronizationManager;

@DisplayName("JdbcTemplate 테스트")
class JdbcTemplateTest {

	@AfterEach
	void tearDown() {
		TransactionSynchronizationManager.clear();
	}

	@Test
	@DisplayName("성공 - 트랜잭션 연결이 있으면 새 연결을 만들지 않는다")
	void update_useTransactionalConnection() throws Exception {
		ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
		Connection connection = mock(Connection.class);
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		when(connection.prepareStatement("UPDATE sample SET name = ?")).thenReturn(preparedStatement);
		TransactionSynchronizationManager.bind(connection);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionProvider);

		jdbcTemplate.update("UPDATE sample SET name = ?", ps -> ps.setString(1, "test"));

		verifyNoInteractions(connectionProvider);
		verify(connection, never()).close();
		verify(preparedStatement).close();
	}
}
