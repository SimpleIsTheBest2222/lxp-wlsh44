package com.lxp.view;

import com.lxp.view.command.MenuCommand;

public interface MenuStrategy<T extends MenuCommand> {

	String title();

	String body();

	T[] commands();

	T parse(int input);

	boolean handle(T command);

	default String menuPrefix() {
		return "";
	}
}
