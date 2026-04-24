package com.lxp.transaction;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.domain.Course;
import com.lxp.domain.Instructor;
import com.lxp.domain.enums.Level;
import com.lxp.repository.CourseRepository;
import com.lxp.repository.InstructorRepository;
import com.lxp.repository.jdbc.JdbcCourseRepository;
import com.lxp.repository.jdbc.JdbcInstructorRepository;
import com.lxp.repository.jdbc.JdbcTemplate;
import com.lxp.repository.jdbc.MysqlIntegrationSupport;

@DisplayName("Transaction MySQL 통합 테스트")
class TransactionMysqlIntegrationTest extends MysqlIntegrationSupport {

	private CourseRepository courseRepository;
	private InstructorRepository instructorRepository;
	private SampleTransactionalService service;

	@BeforeEach
	void setUp() {
		resetDatabase();
		JdbcTemplate jdbcTemplate = jdbcTemplate();
		this.courseRepository = new JdbcCourseRepository(jdbcTemplate);
		this.instructorRepository = new JdbcInstructorRepository(jdbcTemplate);

		SampleTransactionalService target = new SampleTransactionalServiceImpl(instructorRepository, courseRepository);
		this.service = new TransactionProxyFactory(new JdbcTransactionManager(connectionProvider()))
			.createProxy(SampleTransactionalService.class, target);
	}

	@Test
	@DisplayName("성공 - 트랜잭션 메서드가 끝나면 함께 커밋된다")
	void commit() {
		service.saveAll();

		assertThat(instructorRepository.findAll()).hasSize(1);
		assertThat(courseRepository.findAll()).hasSize(1);
	}

	@Test
	@DisplayName("실패 - 트랜잭션 메서드 중 예외가 발생하면 함께 롤백된다")
	void rollback() {
		assertThatThrownBy(service::saveAllAndFail)
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("rollback");

		assertThat(instructorRepository.findAll()).isEmpty();
		assertThat(courseRepository.findAll()).isEmpty();
	}

	private interface SampleTransactionalService {
		void saveAll();

		void saveAllAndFail();
	}

	private static class SampleTransactionalServiceImpl implements SampleTransactionalService {

		private final InstructorRepository instructorRepository;
		private final CourseRepository courseRepository;

		private SampleTransactionalServiceImpl(
			InstructorRepository instructorRepository,
			CourseRepository courseRepository
		) {
			this.instructorRepository = instructorRepository;
			this.courseRepository = courseRepository;
		}

		@Override
		@Transaction
		public void saveAll() {
			Instructor instructor = instructorRepository.save(Instructor.create("김남준", "자바 강사"));
			courseRepository.save(Course.create(instructor.getId(), "Java 입문", "기초 문법", 10000, Level.LOW));
		}

		@Override
		@Transaction
		public void saveAllAndFail() {
			Instructor instructor = instructorRepository.save(Instructor.create("김남준", "자바 강사"));
			courseRepository.save(Course.create(instructor.getId(), "Java 입문", "기초 문법", 10000, Level.LOW));
			throw new IllegalStateException("rollback");
		}
	}
}
