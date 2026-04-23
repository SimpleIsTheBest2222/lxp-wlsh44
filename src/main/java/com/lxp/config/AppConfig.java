package com.lxp.config;

import com.lxp.view.MainView;

public class AppConfig {

	private final RepositoryConfig repositoryConfig = new RepositoryConfig();
	private final ServiceConfig serviceConfig = new ServiceConfig(repositoryConfig);
	private final ControllerConfig controllerConfig = new ControllerConfig(serviceConfig);
	private final ViewConfig viewConfig = new ViewConfig(controllerConfig);

	public MainView mainView() {
		return viewConfig.mainView();
	}
}
