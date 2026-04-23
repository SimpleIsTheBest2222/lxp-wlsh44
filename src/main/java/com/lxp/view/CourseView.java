package com.lxp.view;

import java.util.ArrayList;
import java.util.List;

import com.lxp.controller.CourseController;
import com.lxp.controller.request.CourseRegisterRequest;
import com.lxp.controller.request.ContentRegisterRequest;
import com.lxp.controller.response.CourseRegisterResponse;
import com.lxp.view.command.CourseCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseView implements MenuStrategy<CourseCommand> {

	private final MenuRenderer menuRenderer;
	private final InputView inputView;
	private final OutputView outputView;
	private final CourseController courseController;
	private final CourseListView courseListView;

	public void run() {
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		return new MenuScreen("강의 관리", "", List.of(CourseCommand.values()));
	}

	@Override
	public CourseCommand parse(int input) {
		return CourseCommand.from(input);
	}

	@Override
	public boolean handle(CourseCommand command) {
		switch (command) {
			case REGISTER -> {
				registerCourse();
			}
			case LIST -> {
				courseController.findAll();
				courseListView.run();
			}
			case BACK -> {
				return false;
			}
		}
		return true;
	}

	private void registerCourse() {
		outputView.printHeader("강의 등록");
		outputView.printBody("  강의 정보를 입력하세요.");

		outputView.printLabel("  강의 제목    : ");
		String title = inputView.readLine();

		outputView.printLabel("  가격         : ");
		int price = inputView.readInt();

		outputView.printLabel("  난이도       : ");
		String level = inputView.readLine();

		outputView.printLabel("  강의 설명    : ");
		String description = inputView.readLine();

		outputView.printEmptyLine();
		outputView.printSectionLine();

		List<ContentRegisterRequest> contents = registerContents();
		CourseRegisterRequest request = new CourseRegisterRequest(title, description, price, level, contents);
		CourseRegisterResponse response = courseController.register(request);

		outputView.printSuccess("  강의가 등록되었습니다. id: " + response.id());
		outputView.printSectionLine();
	}

	private List<ContentRegisterRequest> registerContents() {
		outputView.printBody("  콘텐츠를 추가하세요. (제목에 0 입력 시 완료)");
		outputView.printSectionLine();
		outputView.printEmptyLine();

		List<ContentRegisterRequest> contents = new ArrayList<>();
		int seq = 1;
		while (true) {
			outputView.printLabel("  콘텐츠 제목  : ");
			String title = inputView.readLine();
			if ("0".equals(title)) {
				break;
			}

			outputView.printLabel("  콘텐츠 내용  : ");
			String body = inputView.readLine();
			outputView.printEmptyLine();

			contents.add(new ContentRegisterRequest(title, body, seq++));
		}
		return contents;
	}
}
