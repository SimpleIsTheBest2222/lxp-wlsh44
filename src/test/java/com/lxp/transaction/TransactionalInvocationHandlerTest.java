package com.lxp.transaction;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TransactionalInvocationHandler 테스트")
class TransactionalInvocationHandlerTest {

	@Test
	@DisplayName("성공 - @Transaction 메서드 호출 시 트랜잭션을 시작하고 커밋한다")
	void invoke_transactionalMethod() {
		TransactionManager transactionManager = mock(TransactionManager.class);
		TransactionStatus transactionStatus = new TransactionStatus(null, true);
		when(transactionManager.begin()).thenReturn(transactionStatus);
		SampleService proxy = new TransactionProxyFactory(transactionManager)
			.createProxy(SampleService.class, new SampleServiceImpl());

		String result = proxy.save();

		assertThat(result).isEqualTo("saved");
		verify(transactionManager).begin();
		verify(transactionManager).commit(transactionStatus);
		verify(transactionManager, never()).rollback(any());
	}

	@Test
	@DisplayName("성공 - @Transaction 없는 메서드는 트랜잭션 없이 호출한다")
	void invoke_nonTransactionalMethod() {
		TransactionManager transactionManager = mock(TransactionManager.class);
		SampleService proxy = new TransactionProxyFactory(transactionManager)
			.createProxy(SampleService.class, new SampleServiceImpl());

		String result = proxy.find();

		assertThat(result).isEqualTo("found");
		verifyNoInteractions(transactionManager);
	}

	@Test
	@DisplayName("실패 - @Transaction 메서드에서 예외가 발생하면 롤백한다")
	void invoke_transactionalMethodRollback() {
		TransactionManager transactionManager = mock(TransactionManager.class);
		TransactionStatus transactionStatus = new TransactionStatus(null, true);
		when(transactionManager.begin()).thenReturn(transactionStatus);
		SampleService proxy = new TransactionProxyFactory(transactionManager)
			.createProxy(SampleService.class, new SampleServiceImpl());

		assertThatThrownBy(proxy::fail)
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("boom");
		verify(transactionManager).rollback(transactionStatus);
		verify(transactionManager, never()).commit(any());
	}

	private interface SampleService {
		String save();

		String find();

		String fail();
	}

	private static class SampleServiceImpl implements SampleService {

		@Override
		@Transaction
		public String save() {
			return "saved";
		}

		@Override
		public String find() {
			return "found";
		}

		@Override
		@Transaction
		public String fail() {
			throw new IllegalStateException("boom");
		}
	}
}
