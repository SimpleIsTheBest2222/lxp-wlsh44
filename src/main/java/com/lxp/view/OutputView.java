package com.lxp.view;

import com.lxp.view.command.MenuCommand;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class OutputView {

	static final String RESET = "\033[0m";
	static final String CYAN = "\033[36m";
	static final String YELLOW = "\033[33m";
	static final String RED = "\033[31m";
	static final String GRAY = "\033[90m";
	static final String LINE_D = GRAY + "=".repeat(60) + RESET;
	static final String LINE_S = GRAY + "-".repeat(60) + RESET;

	static void printHeader(String title) {
		System.out.printf("""
			%s
			%s%s%s
			%s
			""", LINE_D, CYAN, alignTitle(title), RESET, LINE_D);
	}

	static void printBody(String body) {
		if (!body.isBlank()) {
			System.out.println(body);
		}
		System.out.println();
	}

	static void printMenu(MenuCommand[] commands) {
		printMenu(commands, "");
	}

	static void printMenu(MenuCommand[] commands, String selectLabelPrefix) {
		System.out.println(LINE_S);
		System.out.println();
		for (MenuCommand command : commands) {
			System.out.println(formatMenuLine(command, selectLabelPrefix));
		}
		System.out.println();
		System.out.println(LINE_S);
	}

	static void printNotImplemented() {
		String message = "  (서비스 구현 예정)";
		System.out.printf("""
			%s
			%s%s%s
			%s
			""", LINE_S, GRAY, message, RESET, LINE_S);
	}

	static void printError(String message) {
		System.out.println(RED + "  [오류] " + message + RESET);
	}

	static void printPrompt() {
		System.out.print(YELLOW + "> " + RESET);
	}

	private static String alignTitle(String title) {
		return "%33s".formatted(title);
	}

	private static String formatMenuLine(MenuCommand command, String selectLabelPrefix) {
		String label = command.getLabel();
		if (!selectLabelPrefix.isBlank() && "선택".equals(label)) {
			label = selectLabelPrefix + " " + label;
		}
		return "  %d. %s".formatted(command.getValue(), label);
	}
}
