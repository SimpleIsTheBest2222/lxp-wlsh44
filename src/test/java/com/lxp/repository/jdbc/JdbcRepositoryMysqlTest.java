package com.lxp.repository.jdbc;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.domain.Content;
import com.lxp.domain.Course;
import com.lxp.domain.Instructor;
import com.lxp.domain.enums.ContentType;
import com.lxp.domain.enums.Level;

@DisplayName("JdbcRepository MySQL 테스트")
class JdbcRepositoryMysqlTest extends MysqlIntegrationSupport {

	private JdbcInstructorRepository instructorRepository;
	private JdbcCourseRepository courseRepository;
	private JdbcContentRepository contentRepository;

	@BeforeEach
	void setUp() {
		resetDatabase();
		JdbcTemplate jdbcTemplate = jdbcTemplate();
		this.instructorRepository = new JdbcInstructorRepository(jdbcTemplate);
		this.courseRepository = new JdbcCourseRepository(jdbcTemplate);
		this.contentRepository = new JdbcContentRepository(jdbcTemplate);
	}

	@Test
	@DisplayName("성공 - 강사를 저장하고 조회한다")
	void saveAndFindInstructor() {
		Instructor savedInstructor = instructorRepository.save(Instructor.create("김남준", "자바 강사"));

		Instructor foundInstructor = instructorRepository.findById(savedInstructor.getId()).orElseThrow();

		assertThat(foundInstructor.getId()).isEqualTo(savedInstructor.getId());
		assertThat(foundInstructor.getName()).isEqualTo("김남준");
		assertThat(foundInstructor.getIntroduction()).isEqualTo("자바 강사");
	}

	@Test
	@DisplayName("성공 - 강의를 soft delete 하면 조회되지 않는다")
	void deleteCourse_softDelete() {
		Instructor instructor = instructorRepository.save(Instructor.create("김남준", "자바 강사"));
		Course savedCourse = courseRepository.save(
			Course.create(instructor.getId(), "Java 입문", "기초 문법", 10000, Level.LOW)
		);

		courseRepository.deleteById(savedCourse.getId());

		assertThat(courseRepository.findById(savedCourse.getId())).isEmpty();
		assertThat(courseRepository.findAll()).isEmpty();
	}

	@Test
	@DisplayName("성공 - 콘텐츠를 강의 순서대로 조회한다")
	void findAllContent_orderBySeq() {
		Instructor instructor = instructorRepository.save(Instructor.create("김남준", "자바 강사"));
		Course course = courseRepository.save(
			Course.create(instructor.getId(), "Java 입문", "기초 문법", 10000, Level.LOW)
		);
		contentRepository.save(Content.create(course.getId(), "for 문", "설명", ContentType.TEXT, 2));
		contentRepository.save(Content.create(course.getId(), "원시타입", "설명", ContentType.TEXT, 1));

		List<Content> contents = contentRepository.findAll();

		assertThat(contents).extracting(Content::getTitle)
			.containsExactly("원시타입", "for 문");
	}
}
