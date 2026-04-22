package com.lxp.view;

import java.util.List;
import java.util.Objects;

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
		String safeBody = Objects.requireNonNullElse(body, "");
		if (!safeBody.isBlank()) {
			System.out.println(safeBody);
		}
		System.out.println();
	}

	static void printMenu(List<? extends MenuCommand> commands) {
		System.out.println(LINE_S);
		System.out.println();
		for (MenuCommand command : commands) {
			System.out.println(formatMenuLine(command));
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

	private static String formatMenuLine(MenuCommand command) {
		String label = command.getLabel();
		return "  %d. %s".formatted(command.getValue(), label);
	}
}
