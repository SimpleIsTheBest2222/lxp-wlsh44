package com.lxp.view.command;

public interface MenuCommand {

	int getValue();

	String getLabel();

	default boolean isSelectable() {
		return false;
	}
}
