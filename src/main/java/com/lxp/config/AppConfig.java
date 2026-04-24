package com.lxp.config;

import com.lxp.view.MainView;

public class AppConfig {

	private final RepositoryConfig repositoryConfig = new RepositoryConfig();
	private final MockDataInitializer mockDataInitializer = new MockDataInitializer(
		repositoryConfig.instructorRepository(),
		repositoryConfig.courseRepository(),
		repositoryConfig.contentRepository()
	);
	private final ServiceConfig serviceConfig = new ServiceConfig(repositoryConfig);
	private final ControllerConfig controllerConfig = new ControllerConfig(serviceConfig);
	private final ViewConfig viewConfig = new ViewConfig(controllerConfig);

	public AppConfig() {
		if (!repositoryConfig.isJdbcMode()) {
			mockDataInitializer.initialize();
		}
	}

	public MainView mainView() {
		return viewConfig.mainView();
	}
}
