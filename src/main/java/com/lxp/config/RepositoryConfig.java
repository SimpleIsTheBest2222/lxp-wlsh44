package com.lxp.config;

import com.lxp.repository.InstructorRepository;
import com.lxp.repository.inmemory.InMemoryInstructorRepository;

public class RepositoryConfig {

	private final InstructorRepository instructorRepository = new InMemoryInstructorRepository();

	public InstructorRepository instructorRepository() {
		return instructorRepository;
	}
}
