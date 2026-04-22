package com.lxp.controller;

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
import com.lxp.service.InstructorService;

@ExtendWith(MockitoExtension.class)
@DisplayName("InstructorController 테스트")
class InstructorControllerTest {

	@Mock
	private InstructorService instructorService;

	@InjectMocks
	private InstructorController instructorController;

	@Test
	@DisplayName("성공 - 강사를 등록하면 id를 반환한다")
	void register() {
		// given
		InstructorRegisterRequest request = new InstructorRegisterRequest("김남준", "자바 강사");
		when(instructorService.register(request))
			.thenReturn(new InstructorRegisterResponse(1L));

		// when
		InstructorRegisterResponse response = instructorController.register(request);

		// then
		assertThat(response.id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("성공 - 등록된 강사 목록을 조회한다")
	void findAll() {
		// given
		when(instructorService.findAll()).thenReturn(new InstructorListResponse(List.of(
			new com.lxp.controller.response.InstructorSummaryResponse(1L, "홍길동"),
			new com.lxp.controller.response.InstructorSummaryResponse(2L, "김남준")
		)));

		// when
		InstructorListResponse response = instructorController.findAll();

		// then
		assertThat(response.instructors())
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
		when(instructorService.findById(1L)).thenReturn(new InstructorSummaryResponse(1L, "홍길동"));

		// when
		InstructorSummaryResponse response = instructorController.findById(1L);

		// then
		assertThat(response.id()).isEqualTo(1L);
		assertThat(response.name()).isEqualTo("홍길동");
	}

	@Test
	@DisplayName("성공 - id로 강사 상세를 조회한다")
	void findDetailById() {
		// given
		when(instructorService.findDetailById(1L))
			.thenReturn(new InstructorDetailResponse(1L, "홍길동", "소개1"));

		// when
		InstructorDetailResponse response = instructorController.findDetailById(1L);

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
		when(instructorService.update(request)).thenReturn(new InstructorUpdateResponse(1L));

		// when
		InstructorUpdateResponse response = instructorController.update(request);

		// then
		assertThat(response.id()).isEqualTo(1L);
	}

	@Test
	@DisplayName("성공 - 강사를 삭제한다")
	void delete() {
		// given
		InstructorDeleteRequest request = new InstructorDeleteRequest(1L);
		when(instructorService.delete(request)).thenReturn(new InstructorDeleteResponse(1L));

		// when
		InstructorDeleteResponse response = instructorController.delete(request);

		// then
		assertThat(response.id()).isEqualTo(1L);
	}
}
