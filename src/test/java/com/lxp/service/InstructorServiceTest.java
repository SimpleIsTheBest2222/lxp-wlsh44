package com.lxp.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.controller.request.InstructorDeleteRequest;
import com.lxp.controller.request.InstructorRegisterRequest;
import com.lxp.controller.request.InstructorUpdateRequest;
import com.lxp.domain.Instructor;
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
		when(instructorRepository.save(any())).thenAnswer(invocation -> {
			Instructor saved = invocation.getArgument(0);
			return Instructor.createWithId(1L, saved.getName(), saved.getIntroduction());
		});

		// when
		Instructor response = instructorService.register(request);

		// then
		ArgumentCaptor<Instructor> captor = ArgumentCaptor.forClass(Instructor.class);
		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getName()).isEqualTo("김남준");
		assertThat(response.getIntroduction()).isEqualTo("자바 강사");
		verify(instructorRepository).save(captor.capture());
		assertThat(captor.getValue().getName()).isEqualTo("김남준");
		assertThat(captor.getValue().getIntroduction()).isEqualTo("자바 강사");
	}

	@Test
	@DisplayName("성공 - 전체 강사를 조회한다")
	void findAll() {
		// given
		List<Instructor> instructors = List.of(
			Instructor.createWithId(1L, "홍길동", "소개1"),
			Instructor.createWithId(2L, "김남준", "소개2")
		);
		when(instructorRepository.findAll()).thenReturn(instructors);

		// when
		List<Instructor> result = instructorService.findAll();

		// then
		assertThat(result)
			.extracting(Instructor::getId, Instructor::getName)
			.containsExactly(
				tuple(1L, "홍길동"),
				tuple(2L, "김남준")
			);
	}

	@Test
	@DisplayName("성공 - id로 강사를 조회한다")
	void findById() {
		// given
		when(instructorRepository.findById(1L)).thenReturn(Optional.of(Instructor.createWithId(1L, "홍길동", "소개1")));

		// when
		Instructor response = instructorService.findById(1L);

		// then
		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getName()).isEqualTo("홍길동");
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
		when(instructorRepository.findById(1L)).thenReturn(Optional.of(Instructor.createWithId(1L, "홍길동", "소개1")));

		// when
		Instructor response = instructorService.findDetailById(1L);

		// then
		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getName()).isEqualTo("홍길동");
		assertThat(response.getIntroduction()).isEqualTo("소개1");
	}

	@Test
	@DisplayName("성공 - 강사를 수정한다")
	void update() {
		// given
		InstructorUpdateRequest request = new InstructorUpdateRequest(1L, "김남준 교수", "스프링 강사");
		Instructor instructor = Instructor.createWithId(1L, "김남준", "자바 강사");
		when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
		when(instructorRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		Instructor response = instructorService.update(request);

		// then
		ArgumentCaptor<Instructor> captor = ArgumentCaptor.forClass(Instructor.class);
		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getName()).isEqualTo("김남준 교수");
		assertThat(response.getIntroduction()).isEqualTo("스프링 강사");
		verify(instructorRepository).save(captor.capture());
		assertThat(captor.getValue().getName()).isEqualTo("김남준 교수");
		assertThat(captor.getValue().getIntroduction()).isEqualTo("스프링 강사");
	}

	@Test
	@DisplayName("성공 - 강사를 삭제한다")
	void delete() {
		// given
		InstructorDeleteRequest request = new InstructorDeleteRequest(1L);
		when(instructorRepository.findById(1L)).thenReturn(Optional.of(Instructor.createWithId(1L, "김남준", "자바 강사")));

		// when
		Instructor response = instructorService.delete(request);

		// then
		assertThat(response.getId()).isEqualTo(1L);
		verify(instructorRepository).deleteById(1L);
	}
}
