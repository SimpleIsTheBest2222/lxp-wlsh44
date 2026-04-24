package com.lxp.transaction;

import static org.mockito.Mockito.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.repository.jdbc.ConnectionProvider;

@DisplayName("JdbcTransactionManager 테스트")
class JdbcTransactionManagerTest {

	@AfterEach
	void tearDown() {
		TransactionSynchronizationManager.clear();
	}

	@Test
	@DisplayName("성공 - 트랜잭션 시작 후 커밋하면 연결을 정리한다")
	void beginAndCommit() throws Exception {
		ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
		Connection connection = mock(Connection.class);
		when(connectionProvider.getConnection()).thenReturn(connection);
		JdbcTransactionManager transactionManager = new JdbcTransactionManager(connectionProvider);

		TransactionStatus transactionStatus = transactionManager.begin();
		transactionManager.commit(transactionStatus);

		verify(connection).setAutoCommit(false);
		verify(connection).commit();
		verify(connection).setAutoCommit(true);
		verify(connection).close();
	}

	@Test
	@DisplayName("성공 - 트랜잭션 시작 후 롤백하면 연결을 정리한다")
	void beginAndRollback() throws Exception {
		ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
		Connection connection = mock(Connection.class);
		when(connectionProvider.getConnection()).thenReturn(connection);
		JdbcTransactionManager transactionManager = new JdbcTransactionManager(connectionProvider);

		TransactionStatus transactionStatus = transactionManager.begin();
		transactionManager.rollback(transactionStatus);

		verify(connection).setAutoCommit(false);
		verify(connection).rollback();
		verify(connection).setAutoCommit(true);
		verify(connection).close();
	}

	@Test
	@DisplayName("성공 - 이미 트랜잭션이 있으면 기존 연결에 참여한다")
	void begin_participateExistingTransaction() throws Exception {
		ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
		Connection connection = mock(Connection.class);
		TransactionSynchronizationManager.bind(connection);
		JdbcTransactionManager transactionManager = new JdbcTransactionManager(connectionProvider);

		TransactionStatus transactionStatus = transactionManager.begin();
		transactionManager.commit(transactionStatus);

		verifyNoInteractions(connectionProvider);
		verify(connection, never()).commit();
		verify(connection, never()).close();
	}
}
