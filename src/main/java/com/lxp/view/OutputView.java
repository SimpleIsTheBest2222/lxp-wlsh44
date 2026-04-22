package com.lxp.view;

public class OutputView {

	private static final String RESET = "\033[0m";
	private static final String CYAN = "\033[36m";
	private static final String GREEN = "\033[32m";
	private static final String YELLOW = "\033[33m";
	private static final String RED = "\033[31m";
	private static final String GRAY = "\033[90m";
	private static final String LINE_D = GRAY + "=".repeat(60) + RESET;
	private static final String LINE_S = GRAY + "-".repeat(60) + RESET;

	public void printMainMenu() {
		System.out.println(LINE_D);
		System.out.println(CYAN + "                          강의 관리 콘솔" + RESET);
		System.out.println(LINE_D);
		System.out.println();
		System.out.println("  1. 강의 관리");
		System.out.println("  2. 강사 관리");
		System.out.println("  3. 종료");
		System.out.println();
		System.out.println(LINE_S);
	}

	public void printCourseMenu() {
		System.out.println(LINE_D);
		System.out.println(CYAN + "                           강의 관리" + RESET);
		System.out.println(LINE_D);
		System.out.println();
		System.out.println("  1. 강의 등록");
		System.out.println("  2. 강의 조회");
		System.out.println("  3. 뒤로 가기");
		System.out.println();
		System.out.println(LINE_S);
	}

	public void printInstructorMenu() {
		System.out.println(LINE_D);
		System.out.println(CYAN + "                           강사 관리" + RESET);
		System.out.println(LINE_D);
		System.out.println();
		System.out.println("  1. 강사 등록");
		System.out.println("  2. 강사 조회");
		System.out.println("  3. 뒤로 가기");
		System.out.println();
		System.out.println(LINE_S);
	}

	public void printCourseListEmpty() {
		System.out.println(LINE_D);
		System.out.println(CYAN + "                           강의 목록" + RESET);
		System.out.println(LINE_D);
		System.out.println();
		System.out.println(GRAY + "  등록된 강의가 없습니다." + RESET);
		System.out.println();
		System.out.println(LINE_S);
		System.out.println("  1. 강의 선택");
		System.out.println("  2. 뒤로 가기");
		System.out.println(LINE_S);
	}

	public void printInstructorListEmpty() {
		System.out.println(LINE_D);
		System.out.println(CYAN + "                           강사 목록" + RESET);
		System.out.println(LINE_D);
		System.out.println();
		System.out.println(GRAY + "  등록된 강사가 없습니다." + RESET);
		System.out.println();
		System.out.println(LINE_S);
		System.out.println("  1. 강사 선택");
		System.out.println("  2. 뒤로 가기");
		System.out.println(LINE_S);
	}

	public void printNotImplemented() {
		System.out.println(LINE_S);
		System.out.println(GRAY + "  (서비스 구현 예정)" + RESET);
		System.out.println(LINE_S);
	}

	public void printError(String message) {
		System.out.println(RED + "  [오류] " + message + RESET);
	}

	public void printPrompt() {
		System.out.print(YELLOW + "> " + RESET);
	}
}
