package com.lxp.repository.inmemory;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lxp.domain.Instructor;

@DisplayName("InMemoryInstructorRepository 테스트")
class InMemoryInstructorRepositoryTest {

	private final InMemoryInstructorRepository instructorRepository = new InMemoryInstructorRepository();

	@Test
	@DisplayName("성공 - id가 없는 강사를 저장하면 id를 할당한다")
	void save_newInstructor() {
		// given
		Instructor instructor = Instructor.create("김남준", "자바 강사");

		// when
		Instructor savedInstructor = instructorRepository.save(instructor);

		// then
		assertThat(savedInstructor.getId()).isEqualTo(1L);
		assertThat(instructorRepository.findAll()).hasSize(1);
	}

	@Test
	@DisplayName("성공 - 전체 강사를 조회한다")
	void findAll() {
		// given
		instructorRepository.save(Instructor.create("홍길동", "소개1"));
		instructorRepository.save(Instructor.create("김남준", "소개2"));

		// when
		var instructors = instructorRepository.findAll();

		// then
		assertThat(instructors)
			.extracting("id", "name")
			.containsExactly(
				tuple(1L, "홍길동"),
				tuple(2L, "김남준")
			);
	}

	@Test
	@DisplayName("성공 - id로 강사를 조회한다")
	void findById() {
		// given
		Instructor savedInstructor = instructorRepository.save(Instructor.create("김남준", "소개"));

		// when
		var foundInstructor = instructorRepository.findById(savedInstructor.getId());

		// then
		assertThat(foundInstructor).isPresent();
		assertThat(foundInstructor.get().getId()).isEqualTo(savedInstructor.getId());
		assertThat(foundInstructor.get().getName()).isEqualTo(savedInstructor.getName());
	}

	@Test
	@DisplayName("성공 - 강사를 삭제하면 soft delete 처리되어 조회되지 않는다")
	void deleteById() {
		// given
		Instructor savedInstructor = instructorRepository.save(Instructor.create("김남준", "소개"));

		// when
		instructorRepository.deleteById(savedInstructor.getId());

		// then
		assertThat(instructorRepository.findById(savedInstructor.getId())).isEmpty();
		assertThat(instructorRepository.findAll()).isEmpty();
	}
}
