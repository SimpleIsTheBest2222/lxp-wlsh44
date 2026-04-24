package com.lxp.transaction;

public interface TransactionManager {

	TransactionStatus begin();

	void commit(TransactionStatus transactionStatus);

	void rollback(TransactionStatus transactionStatus);
}
