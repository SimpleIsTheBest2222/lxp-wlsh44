package com.lxp.view;

import java.util.Scanner;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class InputView {

	private static final Scanner SCANNER = new Scanner(System.in);

	static int readInt() {
		try {
			return Integer.parseInt(SCANNER.nextLine().trim());
		} catch (NumberFormatException e) {
			throw new LxpException(ErrorCode.INVALID_INPUT);
		}
	}

	static String readLine() {
		return SCANNER.nextLine().trim();
	}
}
