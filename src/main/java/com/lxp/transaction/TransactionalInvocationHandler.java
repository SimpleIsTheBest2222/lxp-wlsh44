package com.lxp.transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionalInvocationHandler implements InvocationHandler {

	private final Object target;
	private final TransactionManager transactionManager;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
		if (!isTransactional(targetMethod)) {
			return invokeTarget(targetMethod, args);
		}

		TransactionStatus transactionStatus = transactionManager.begin();
		try {
			Object result = invokeTarget(targetMethod, args);
			transactionManager.commit(transactionStatus);
			return result;
		} catch (Throwable throwable) {
			transactionManager.rollback(transactionStatus);
			throw throwable;
		}
	}

	private Object invokeTarget(Method targetMethod, Object[] args) throws Throwable {
		try {
			return targetMethod.invoke(target, args);
		} catch (InvocationTargetException exception) {
			throw exception.getTargetException();
		}
	}

	private boolean isTransactional(Method targetMethod) {
		return targetMethod.isAnnotationPresent(Transaction.class)
			|| target.getClass().isAnnotationPresent(Transaction.class);
	}
}
