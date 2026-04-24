package com.lxp.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.jdbc.ConnectionProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcTransactionManager implements TransactionManager {

	private final ConnectionProvider connectionProvider;

	@Override
	public TransactionStatus begin() {
		if (TransactionSynchronizationManager.hasConnection()) {
			return TransactionStatus.participating(TransactionSynchronizationManager.getConnection());
		}

		try {
			Connection connection = connectionProvider.getConnection();
			connection.setAutoCommit(false);
			TransactionSynchronizationManager.bind(connection);
			return TransactionStatus.started(connection);
		} catch (SQLException e) {
			throw new LxpException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void commit(TransactionStatus transactionStatus) {
		if (!transactionStatus.newTransaction()) {
			return;
		}

		try {
			transactionStatus.connection().commit();
		} catch (SQLException e) {
			throw new LxpException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			close(transactionStatus.connection());
		}
	}

	@Override
	public void rollback(TransactionStatus transactionStatus) {
		if (!transactionStatus.newTransaction()) {
			return;
		}

		try {
			transactionStatus.connection().rollback();
		} catch (SQLException e) {
			throw new LxpException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			close(transactionStatus.connection());
		}
	}

	private void close(Connection connection) {
		try {
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			throw new LxpException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			TransactionSynchronizationManager.clear();
		}
	}
}
