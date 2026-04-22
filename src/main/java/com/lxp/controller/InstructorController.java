package com.lxp.controller;

import com.lxp.exception.LxpException;
import com.lxp.view.InputView;
import com.lxp.view.OutputView;

public class InstructorController {

	private final InputView inputView;
	private final OutputView outputView;

	public InstructorController(InputView inputView, OutputView outputView) {
		this.inputView = inputView;
		this.outputView = outputView;
	}

	public void run() {
		while (true) {
			outputView.printInstructorMenu();
			outputView.printPrompt();
			try {
				int choice = inputView.readInt();
				if (choice == 1) {
					outputView.printNotImplemented();
				} else if (choice == 2) {
					runInstructorList();
				} else if (choice == 3) {
					return;
				} else {
					outputView.printError("1~3 사이의 번호를 입력해 주세요.");
				}
			} catch (LxpException e) {
				outputView.printError(e.getErrorCode().getMessage());
			}
		}
	}

	private void runInstructorList() {
		outputView.printInstructorListEmpty();
		outputView.printPrompt();
		try {
			int choice = inputView.readInt();
			if (choice != 1 && choice != 2) {
				outputView.printError("1~2 사이의 번호를 입력해 주세요.");
			}
		} catch (LxpException e) {
			outputView.printError(e.getErrorCode().getMessage());
		}
	}
}
