package com.lxp.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.controller.request.InstructorDeleteRequest;
import com.lxp.controller.request.InstructorRegisterRequest;
import com.lxp.controller.request.InstructorUpdateRequest;
import com.lxp.controller.response.InstructorDeleteResponse;
import com.lxp.controller.response.InstructorDetailResponse;
import com.lxp.controller.response.InstructorListResponse;
import com.lxp.controller.response.InstructorRegisterResponse;
import com.lxp.controller.response.InstructorSummaryResponse;
import com.lxp.controller.response.InstructorUpdateResponse;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.InstructorRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("InstructorService 테스트")
class InstructorServiceTest {

	@Mock
	private InstructorRepository instructorRepository;

	@InjectMocks
	private InstructorService instructorService;

	@Test
	@DisplayName("성공 - 강사를 등록한다")
	void register() {
		// given
		InstructorRegisterRequest request = new InstructorRegisterRequest("김남준", "자바 강사");
		when(instructorRepository.save(any())).thenReturn(
			com.lxp.domain.Instructor.createWithId(1L, "김남준", "자바 강사")
		);

		// when
		InstructorRegisterResponse response = instructorService.register(request);

		// then
		assertThat(response.id()).isEqualTo(1L);
		verify(instructorRepository).save(any());
	}

	@Test
	@DisplayName("성공 - 전체 강사를 조회한다")
	void findAll() {
		// given
		List<com.lxp.domain.Instructor> instructors = List.of(
			com.lxp.domain.Instructor.createWithId(1L, "홍길동", "소개1"),
			com.lxp.domain.Instructor.createWithId(2L, "김남준", "소개2")
		);
		when(instructorRepository.findAll()).thenReturn(instructors);

		// when
		InstructorListResponse result = instructorService.findAll();

		// then
		assertThat(result.instructors())
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
		when(instructorRepository.findById(1L)).thenReturn(
			java.util.Optional.of(com.lxp.domain.Instructor.createWithId(1L, "홍길동", "소개1"))
		);

		// when
		InstructorSummaryResponse response = instructorService.findById(1L);

		// then
		assertThat(response.id()).isEqualTo(1L);
		assertThat(response.name()).isEqualTo("홍길동");
	}

	@Test
	@DisplayName("실패 - 존재하지 않는 강사 id면 예외가 발생한다")
	void findById_notFound() {
		// given
		when(instructorRepository.findById(99L)).thenReturn(java.util.Optional.empty());

		// when & then
		assertThatThrownBy(() -> instructorService.findById(99L))
			.isInstanceOf(LxpException.class)
			.hasMessage(ErrorCode.NOT_FOUND_INSTRUCTOR.getMessage());
	}

	@Test
	@DisplayName("성공 - id로 강사 상세를 조회한다")
	void findDetailById() {
		// given
		when(instructorRepository.findById(1L)).thenReturn(
			java.util.Optional.of(com.lxp.domain.Instructor.createWithId(1L, "홍길동", "소개1"))
		);

		// when
		InstructorDetailResponse response = instructorService.findDetailById(1L);

		// then
		assertThat(response.id()).isEqualTo(1L);
		assertThat(response.name()).isEqualTo("홍길동");
		assertThat(response.introduction()).isEqualTo("소개1");
	}

	@Test
	@DisplayName("성공 - 강사를 수정한다")
	void update() {
		// given
		InstructorUpdateRequest request = new InstructorUpdateRequest(1L, "김남준 교수", "스프링 강사");
		com.lxp.domain.Instructor instructor = com.lxp.domain.Instructor.createWithId(1L, "김남준", "자바 강사");
		when(instructorRepository.findById(1L)).thenReturn(java.util.Optional.of(instructor));
		when(instructorRepository.save(instructor))
			.thenReturn(com.lxp.domain.Instructor.createWithId(1L, "김남준 교수", "스프링 강사"));

		// when
		InstructorUpdateResponse response = instructorService.update(request);

		// then
		assertThat(response.id()).isEqualTo(1L);
		verify(instructorRepository).save(instructor);
	}

	@Test
	@DisplayName("성공 - 강사를 삭제한다")
	void delete() {
		// given
		InstructorDeleteRequest request = new InstructorDeleteRequest(1L);
		when(instructorRepository.findById(1L)).thenReturn(
			java.util.Optional.of(com.lxp.domain.Instructor.createWithId(1L, "김남준", "자바 강사"))
		);

		// when
		InstructorDeleteResponse response = instructorService.delete(request);

		// then
		assertThat(response.id()).isEqualTo(1L);
		verify(instructorRepository).deleteById(1L);
	}
}
