package com.lxp.view;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.view.command.MainCommand;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuRenderer 테스트")
class MenuRendererTest {

	@Mock
	private MenuStrategy<MainCommand> strategy;

	@Mock
	private InputView inputView;

	@Mock
	private OutputView outputView;

	@InjectMocks
	private MenuRenderer menuRenderer;

	@Test
	@DisplayName("성공 - 화면을 출력하고 입력을 처리한다")
	void render_success() {
		// given
		MenuScreen screen = new MenuScreen("메인 화면", "본문", List.of(MainCommand.EXIT));

		when(strategy.screen()).thenReturn(screen);
		when(strategy.parse(3)).thenReturn(MainCommand.EXIT);
		when(strategy.handle(MainCommand.EXIT)).thenReturn(false);
		when(inputView.readInt()).thenReturn(3);

		// when
		menuRenderer.render(strategy);

		// then
		verify(outputView).printHeader("메인 화면");
		verify(outputView).printBody("본문");
		verify(outputView).printMenu(screen.commands());
		verify(outputView).printPrompt();
		verify(strategy).parse(3);
		verify(strategy).handle(MainCommand.EXIT);
	}

	@Test
	@DisplayName("실패 - LxpException 발생 시 오류 메시지를 출력하고 다음 입력을 받는다")
	void render_lxpException() {
		// given
		MenuScreen screen = new MenuScreen("메인 화면", "", List.of(MainCommand.EXIT));

		when(strategy.screen()).thenReturn(screen);
		when(strategy.parse(1)).thenThrow(new LxpException(ErrorCode.INVALID_INPUT));
		when(strategy.parse(3)).thenReturn(MainCommand.EXIT);
		when(strategy.handle(MainCommand.EXIT)).thenReturn(false);
		when(inputView.readInt()).thenReturn(1, 3);

		// when
		menuRenderer.render(strategy);

		// then
		verify(outputView).printError(ErrorCode.INVALID_INPUT.getMessage());
		verify(strategy).handle(MainCommand.EXIT);
	}
}
