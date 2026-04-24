package com.lxp.service;

import java.util.List;

import com.lxp.controller.request.InstructorDeleteRequest;
import com.lxp.controller.request.InstructorRegisterRequest;
import com.lxp.controller.request.InstructorUpdateRequest;
import com.lxp.domain.Instructor;
public interface InstructorService {

	Instructor register(InstructorRegisterRequest request);

	List<Instructor> findAll();

	Instructor findById(Long id);

	Instructor findDetailById(Long id);

	Instructor update(InstructorUpdateRequest request);

	Instructor delete(InstructorDeleteRequest request);
}
