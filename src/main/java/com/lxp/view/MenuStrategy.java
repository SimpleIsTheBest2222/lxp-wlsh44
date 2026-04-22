package com.lxp.view;

import com.lxp.view.command.MenuCommand;

public interface MenuStrategy<T extends MenuCommand> {

	MenuScreen screen();

	T parse(int input);

	boolean handle(T command);
}
