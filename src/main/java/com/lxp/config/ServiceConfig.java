package com.lxp.config;

import com.lxp.service.InstructorService;

public class ServiceConfig {

	private final InstructorService instructorService;

	public ServiceConfig(RepositoryConfig repositoryConfig) {
		this.instructorService = new InstructorService(repositoryConfig.instructorRepository());
	}

	public InstructorService instructorService() {
		return instructorService;
	}
}
