package com.lxp.view;

import java.util.List;

import com.lxp.view.command.MenuCommand;

public record MenuScreen(
	String title,
	String body,
	List<? extends MenuCommand> commands
) {
}
