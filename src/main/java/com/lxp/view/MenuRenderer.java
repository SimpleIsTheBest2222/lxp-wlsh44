package com.lxp.view;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.view.command.MenuCommand;

public class MenuRenderer {

	public <T extends MenuCommand> void render(MenuStrategy<T> strategy) {
		boolean running = true;
		while (running) {
			MenuScreen screen = strategy.screen();

			OutputView.printHeader(screen.title());
			OutputView.printBody(screen.body());
			OutputView.printMenu(screen.commands());
			OutputView.printPrompt();

			try {
				T command = strategy.parse(InputView.readInt());
				running = strategy.handle(command);
			} catch (LxpException e) {
				OutputView.printError(e.getErrorCode().getMessage());
			} catch (Exception e) {
				OutputView.printError(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
				OutputView.printError(e.getMessage());
			}
		}
	}
}
