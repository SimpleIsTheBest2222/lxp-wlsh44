package com.lxp.transaction;

import java.sql.Connection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransactionSynchronizationManager {

	private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<>();

	public static void bind(Connection connection) {
		CONNECTION_HOLDER.set(connection);
	}

	public static boolean hasConnection() {
		return CONNECTION_HOLDER.get() != null;
	}

	public static Connection getConnection() {
		return CONNECTION_HOLDER.get();
	}

	public static boolean isCurrentConnection(Connection connection) {
		return connection != null && connection == CONNECTION_HOLDER.get();
	}

	public static void clear() {
		CONNECTION_HOLDER.remove();
	}
}
