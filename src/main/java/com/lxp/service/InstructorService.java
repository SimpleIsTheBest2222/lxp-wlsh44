package com.lxp.service;

import java.util.List;

import com.lxp.controller.request.InstructorDeleteRequest;
import com.lxp.controller.request.InstructorRegisterRequest;
import com.lxp.controller.request.InstructorUpdateRequest;
import com.lxp.domain.Instructor;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.InstructorRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorService {

	private final InstructorRepository instructorRepository;

	public Instructor register(InstructorRegisterRequest request) {
		Instructor instructor = Instructor.create(request.name(), request.introduction());
		return instructorRepository.save(instructor);
	}

	public List<Instructor> findAll() {
		return instructorRepository.findAll();
	}

	public Instructor findById(Long id) {
		return getInstructorOrThrow(id);
	}

	public Instructor findDetailById(Long id) {
		return getInstructorOrThrow(id);
	}

	public Instructor update(InstructorUpdateRequest request) {
		Instructor instructor = getInstructorOrThrow(request.id());
		instructor.update(request.name(), request.introduction());
		return instructorRepository.save(instructor);
	}

	public Instructor delete(InstructorDeleteRequest request) {
		Instructor instructor = getInstructorOrThrow(request.id());
		instructorRepository.deleteById(request.id());
		return instructor;
	}

	private Instructor getInstructorOrThrow(Long id) {
		return instructorRepository.findById(id)
			.orElseThrow(() -> new LxpException(ErrorCode.NOT_FOUND_INSTRUCTOR));
	}
}
