package com.lxp.view;

import java.util.Scanner;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InputView {

	private final Scanner scanner;

	public int readInt() {
		try {
			return Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			throw new LxpException(ErrorCode.INVALID_INPUT);
		}
	}

	public String readLine() {
		return scanner.nextLine().trim();
	}
}
