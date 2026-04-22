package com.lxp.view;

import java.util.List;

import com.lxp.controller.InstructorController;
import com.lxp.controller.request.InstructorDeleteRequest;
import com.lxp.controller.request.InstructorUpdateRequest;
import com.lxp.controller.response.InstructorDeleteResponse;
import com.lxp.controller.response.InstructorDetailResponse;
import com.lxp.controller.response.InstructorUpdateResponse;
import com.lxp.view.command.InstructorDetailCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstructorDetailView implements MenuStrategy<InstructorDetailCommand> {

	private final MenuRenderer menuRenderer;
	private final InputView inputView;
	private final OutputView outputView;
	private final InstructorController instructorController;

	private Long instructorId;

	public void run(Long instructorId) {
		this.instructorId = instructorId;
		menuRenderer.render(this);
	}

	@Override
	public MenuScreen screen() {
		InstructorDetailResponse response = instructorController.findDetailById(instructorId);
		return new MenuScreen("강사 상세", createBody(response), List.of(InstructorDetailCommand.values()));
	}

	@Override
	public InstructorDetailCommand parse(int input) {
		return InstructorDetailCommand.from(input);
	}

	@Override
	public boolean handle(InstructorDetailCommand command) {
		switch (command) {
			case UPDATE -> {
				updateInstructor();
			}
			case DELETE -> {
				deleteInstructor();
				return false;
			}
			case BACK -> {
				return false;
			}
		}
		return true;
	}

	private String createBody(InstructorDetailResponse response) {
		return """
			  강사 id  : %d
			  이름     : %s
			  소개     : %s
			""".formatted(
			response.id(),
			response.name(),
			response.introduction()
		);
	}

	private void updateInstructor() {
		outputView.printHeader("강사 수정");
		outputView.printBody("  빈 값 입력 시 기존 값이 유지됩니다.");
		outputView.printSectionLine();
		System.out.println();

		outputView.printLabel("  이름    : ");
		String name = inputView.readLine();

		outputView.printLabel("  소개    : ");
		String introduction = inputView.readLine();

		System.out.println();
		outputView.printSectionLine();

		InstructorUpdateRequest request = new InstructorUpdateRequest(instructorId, name, introduction);
		InstructorUpdateResponse response = instructorController.update(request);
		outputView.printSuccess("  수정되었습니다. id: " + response.id());
		outputView.printSectionLine();
	}

	private void deleteInstructor() {
		InstructorDeleteRequest request = new InstructorDeleteRequest(instructorId);
		InstructorDeleteResponse response = instructorController.delete(request);
		outputView.printSectionLine();
		outputView.printSuccess("  삭제가 완료되었습니다. id: " + response.id());
		outputView.printSectionLine();
	}
}
