package com.lxp.view;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.view.command.MenuCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuRenderer {

	private final InputView inputView;
	private final OutputView outputView;

	public <T extends MenuCommand> void render(MenuStrategy<T> strategy) {
		boolean running = true;
		while (running) {
			MenuScreen screen = strategy.screen();

			outputView.printHeader(screen.title());
			outputView.printBody(screen.body());
			outputView.printMenu(screen.commands());
			outputView.printPrompt();

			try {
				T command = strategy.parse(inputView.readInt());
				running = strategy.handle(command);
			} catch (LxpException e) {
				outputView.printError(e.getErrorCode().getMessage());
			} catch (Exception e) {
				outputView.printError(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
				outputView.printError(e.getMessage());
			}
		}
	}
}
