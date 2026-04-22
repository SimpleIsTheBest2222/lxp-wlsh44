package com.lxp.controller;

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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorController {

	private final InstructorService instructorService;

	public InstructorRegisterResponse register(InstructorRegisterRequest request) {
		return instructorService.register(request);
	}

	public InstructorListResponse findAll() {
		return instructorService.findAll();
	}

	public InstructorSummaryResponse findById(Long id) {
		return instructorService.findById(id);
	}

	public InstructorDetailResponse findDetailById(Long id) {
		return instructorService.findDetailById(id);
	}

	public InstructorUpdateResponse update(InstructorUpdateRequest request) {
		return instructorService.update(request);
	}

	public InstructorDeleteResponse delete(InstructorDeleteRequest request) {
		return instructorService.delete(request);
	}
}
