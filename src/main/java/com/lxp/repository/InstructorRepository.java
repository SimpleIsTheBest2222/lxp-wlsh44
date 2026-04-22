package com.lxp.repository;

import java.util.List;
import java.util.Optional;

import com.lxp.domain.Instructor;

public interface InstructorRepository {

	Optional<Instructor> findById(Long id);

	List<Instructor> findAll();

	Instructor save(Instructor instructor);

	void deleteById(Long id);
}
