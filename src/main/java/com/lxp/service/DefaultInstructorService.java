package com.lxp.service;

import java.util.List;

import com.lxp.controller.request.InstructorDeleteRequest;
import com.lxp.controller.request.InstructorRegisterRequest;
import com.lxp.controller.request.InstructorUpdateRequest;
import com.lxp.domain.Instructor;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.InstructorRepository;
import com.lxp.transaction.Transaction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultInstructorService implements InstructorService {

	private final InstructorRepository instructorRepository;

	@Override
	@Transaction
	public Instructor register(InstructorRegisterRequest request) {
		Instructor instructor = Instructor.create(request.name(), request.introduction());
		return instructorRepository.save(instructor);
	}

	@Override
	public List<Instructor> findAll() {
		return instructorRepository.findAll();
	}

	@Override
	public Instructor findById(Long id) {
		return getInstructorOrThrow(id);
	}

	@Override
	public Instructor findDetailById(Long id) {
		return getInstructorOrThrow(id);
	}

	@Override
	@Transaction
	public Instructor update(InstructorUpdateRequest request) {
		Instructor instructor = getInstructorOrThrow(request.id());
		instructor.update(request.name(), request.introduction());
		return instructorRepository.save(instructor);
	}

	@Override
	@Transaction
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
