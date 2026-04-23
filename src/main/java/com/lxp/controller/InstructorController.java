package com.lxp.controller;

import java.util.List;

import com.lxp.controller.request.InstructorDeleteRequest;
import com.lxp.controller.request.InstructorRegisterRequest;
import com.lxp.controller.request.InstructorUpdateRequest;
import com.lxp.controller.response.InstructorDeleteResponse;
import com.lxp.controller.response.InstructorDetailResponse;
import com.lxp.controller.response.InstructorListResponse;
import com.lxp.controller.response.InstructorRegisterResponse;
import com.lxp.controller.response.InstructorSummaryResponse;
import com.lxp.controller.response.InstructorUpdateResponse;
import com.lxp.domain.Instructor;
import com.lxp.service.InstructorService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorController {

	private final InstructorService instructorService;

	public InstructorRegisterResponse register(InstructorRegisterRequest request) {
		Instructor instructor = instructorService.register(request);
		return new InstructorRegisterResponse(instructor.getId());
	}

	public InstructorListResponse findAll() {
		List<InstructorSummaryResponse> instructors = instructorService.findAll().stream()
			.map(instructor -> new InstructorSummaryResponse(instructor.getId(), instructor.getName()))
			.toList();
		return new InstructorListResponse(instructors);
	}

	public InstructorSummaryResponse findById(Long id) {
		Instructor instructor = instructorService.findById(id);
		return new InstructorSummaryResponse(instructor.getId(), instructor.getName());
	}

	public InstructorDetailResponse findDetailById(Long id) {
		Instructor instructor = instructorService.findDetailById(id);
		return new InstructorDetailResponse(instructor.getId(), instructor.getName(), instructor.getIntroduction());
	}

	public InstructorUpdateResponse update(InstructorUpdateRequest request) {
		Instructor instructor = instructorService.update(request);
		return new InstructorUpdateResponse(instructor.getId());
	}

	public InstructorDeleteResponse delete(InstructorDeleteRequest request) {
		Instructor instructor = instructorService.delete(request);
		return new InstructorDeleteResponse(instructor.getId());
	}
}
