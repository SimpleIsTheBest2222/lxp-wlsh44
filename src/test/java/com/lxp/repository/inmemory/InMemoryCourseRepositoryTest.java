package com.lxp.repository.inmemory;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.domain.Course;
import com.lxp.domain.enums.Level;

@DisplayName("InMemoryCourseRepository 테스트")
class InMemoryCourseRepositoryTest {

	private final InMemoryCourseRepository courseRepository = new InMemoryCourseRepository();

	@Test
	@DisplayName("성공 - id가 없는 강의를 저장하면 id를 할당한다")
	void save_newCourse() {
		Course course = Course.create("Java 입문", "기초 문법", 10000, Level.LOW);

		Course savedCourse = courseRepository.save(course);

		assertThat(savedCourse.getId()).isEqualTo(1L);
		assertThat(courseRepository.findAll()).hasSize(1);
	}

	@Test
	@DisplayName("성공 - id로 강의를 조회한다")
	void findById() {
		Course savedCourse = courseRepository.save(Course.create("Java 입문", "기초 문법", 10000, Level.LOW));

		var foundCourse = courseRepository.findById(savedCourse.getId());

		assertThat(foundCourse).isPresent();
		assertThat(foundCourse.get().getTitle()).isEqualTo("Java 입문");
	}

	@Test
	@DisplayName("성공 - 강의를 삭제하면 soft delete 처리되어 조회되지 않는다")
	void deleteById() {
		Course savedCourse = courseRepository.save(Course.create("Java 입문", "기초 문법", 10000, Level.LOW));

		courseRepository.deleteById(savedCourse.getId());

		assertThat(courseRepository.findById(savedCourse.getId())).isEmpty();
		assertThat(courseRepository.findAll()).isEmpty();
	}
}
