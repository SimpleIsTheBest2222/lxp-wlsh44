package com.lxp.service;

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
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.InstructorRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorService {

	private final InstructorRepository instructorRepository;

	public InstructorRegisterResponse register(InstructorRegisterRequest request) {
		Instructor instructor = Instructor.create(request.name(), request.introduction());
		Instructor savedInstructor = instructorRepository.save(instructor);
		return new InstructorRegisterResponse(savedInstructor.getId());
	}

	public InstructorListResponse findAll() {
		List<InstructorSummaryResponse> instructors = instructorRepository.findAll().stream()
			.map(instructor -> new InstructorSummaryResponse(instructor.getId(), instructor.getName()))
			.toList();
		return new InstructorListResponse(instructors);
	}

	public InstructorSummaryResponse findById(Long id) {
		Instructor instructor = instructorRepository.findById(id)
			.orElseThrow(() -> new LxpException(ErrorCode.NOT_FOUND_INSTRUCTOR));
		return new InstructorSummaryResponse(instructor.getId(), instructor.getName());
	}

	public InstructorDetailResponse findDetailById(Long id) {
		Instructor instructor = instructorRepository.findById(id)
			.orElseThrow(() -> new LxpException(ErrorCode.NOT_FOUND_INSTRUCTOR));
		return new InstructorDetailResponse(instructor.getId(), instructor.getName(), instructor.getIntroduction());
	}

	public InstructorUpdateResponse update(InstructorUpdateRequest request) {
		Instructor instructor = instructorRepository.findById(request.id())
			.orElseThrow(() -> new LxpException(ErrorCode.NOT_FOUND_INSTRUCTOR));
		instructor.update(request.name(), request.introduction());
		Instructor savedInstructor = instructorRepository.save(instructor);
		return new InstructorUpdateResponse(savedInstructor.getId());
	}

	public InstructorDeleteResponse delete(InstructorDeleteRequest request) {
		instructorRepository.findById(request.id())
			.orElseThrow(() -> new LxpException(ErrorCode.NOT_FOUND_INSTRUCTOR));
		instructorRepository.deleteById(request.id());
		return new InstructorDeleteResponse(request.id());
	}
}
