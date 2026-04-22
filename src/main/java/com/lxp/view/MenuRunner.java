package com.lxp.view;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.view.command.MenuCommand;

public class MenuRunner {

	public <T extends MenuCommand> void run(MenuStrategy<T> strategy) {
		boolean running = true;
		while (running) {
			OutputView.printHeader(strategy.title());
			OutputView.printBody(strategy.body());
			OutputView.printMenu(strategy.commands(), strategy.menuPrefix());
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
