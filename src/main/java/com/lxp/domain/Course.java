package com.lxp.domain;

import java.time.LocalDateTime;

import com.lxp.common.validate.Assert;
import com.lxp.domain.enums.Level;
import com.lxp.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Course {
	private static final int MAXIMUM_TITLE_LENGTH = 50;
	private static final int MAXIMUM_DESCRIPTION_LENGTH = 200;

	private Long id;
	private String title;
	private String description;
	private int price;
	private Level level;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	public static Course create(String title, String description, int price, Level level) {
		Course course = new Course();

		course.title = validateTitle(title);
		course.description = validateDescription(description);
		course.price = validatePrice(price);
		course.level = validateLevel(level);

		return course;
	}

	public static Course createWithId(Long id, String title, String description, int price, Level level,
		LocalDateTime createdAt, LocalDateTime modifiedAt) {
		Course course = new Course();

		course.id = validateId(id);
		course.title = validateTitle(title);
		course.description = validateDescription(description);
		course.price = validatePrice(price);
		course.level = validateLevel(level);
		course.createdAt = createdAt;
		course.modifiedAt = modifiedAt;

		return course;
	}

	public void update(String title, String description, Integer price, Level level) {
		if (title != null) {
			this.title = validateTitle(title);
		}
		if (description != null) {
			this.description = validateDescription(description);
		}
		if (price != null) {
			this.price = validatePrice(price);
		}
		if (level != null) {
			this.level = validateLevel(level);
		}
	}

	private static Long validateId(Long id) {
		Assert.notNull(id);
		return id;
	}

	private static String validateTitle(String title) {
		Assert.notNull(title);
		Assert.isTrue(!title.isBlank() && title.length() <= MAXIMUM_TITLE_LENGTH, ErrorCode.COURSE_TITLE_OUT_OF_RANGE);
		return title;
	}

	private static String validateDescription(String description) {
		Assert.notNull(description);
		Assert.isTrue(!description.isBlank() && description.length() <= MAXIMUM_DESCRIPTION_LENGTH,
			ErrorCode.COURSE_DESCRIPTION_OUT_OF_RANGE);
		return description;
	}

	private static int validatePrice(int price) {
		Assert.isTrue(price >= 0, ErrorCode.COURSE_PRICE_NEGATIVE);
		return price;
	}

	private static Level validateLevel(Level level) {
		Assert.notNull(level);
		return level;
	}
}
