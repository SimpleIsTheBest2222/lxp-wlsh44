package com.lxp.transaction;

import java.sql.Connection;

public record TransactionStatus(
	Connection connection,
	boolean newTransaction
) {

	public static TransactionStatus participating(Connection connection) {
		return new TransactionStatus(connection, false);
	}

	public static TransactionStatus started(Connection connection) {
		return new TransactionStatus(connection, true);
	}
}
