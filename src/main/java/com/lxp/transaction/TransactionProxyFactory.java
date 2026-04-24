package com.lxp.transaction;

import java.lang.reflect.Proxy;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionProxyFactory {

	private final TransactionManager transactionManager;

	public <T> T createProxy(Class<T> serviceType, T target) {
		return serviceType.cast(Proxy.newProxyInstance(
			serviceType.getClassLoader(),
			new Class<?>[]{serviceType},
			new TransactionalInvocationHandler(target, transactionManager)
		));
	}
}
