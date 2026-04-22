package com.lxp.controller;

import com.lxp.exception.LxpException;
import com.lxp.view.InputView;
import com.lxp.view.OutputView;

public class MainController {

    private final InputView inputView;
    private final OutputView outputView;
    private final CourseController courseController;
    private final InstructorController instructorController;

    public MainController(InputView inputView, OutputView outputView,
            CourseController courseController, InstructorController instructorController) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.courseController = courseController;
        this.instructorController = instructorController;
    }

    public void run() {
        while (true) {
            outputView.printMainMenu();
            outputView.printPrompt();
            try {
                int choice = inputView.readInt();
                if (choice == 1) {
                    courseController.run();
                } else if (choice == 2) {
                    instructorController.run();
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
}
