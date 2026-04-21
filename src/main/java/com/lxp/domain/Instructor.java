package com.lxp.domain;

import java.util.Optional;

import com.lxp.common.validate.Assert;
import com.lxp.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Instructor {
	private static final int MAXIMUM_NAME_RANGE = 10;
	private static final int MAXIMUM_INTRODUCTION_RANGE = 100;

	private Long id;
	private String name;
	private String introduction;

	public static Instructor create(String name, String introduction) {
		Instructor instructor = new Instructor();

		instructor.name = validateName(name);
		instructor.introduction = validateIntroduction(introduction);

		return instructor;
	}

	public static Instructor createWithId(Long id, String name, String introduction) {
		Instructor instructor = new Instructor();

		instructor.id = validateId(id);
		instructor.name = validateName(name);
		instructor.introduction = validateIntroduction(introduction);

		return instructor;
	}

	private static Long validateId(Long id) {
		Assert.notNull(id);
		return id;
	}

	private static String validateName(String name) {
		Assert.notNull(name);
		Assert.isTrue(!name.isEmpty() && name.length() <= MAXIMUM_NAME_RANGE,
			ErrorCode.INSTRUCTOR_NAME_OUT_OF_RANGE);
		return name;
	}

	private static String validateIntroduction(String introduction) {
		String nonNullIntroduction = Optional.ofNullable(introduction)
			.orElse("");
		Assert.isTrue(nonNullIntroduction.length() <= MAXIMUM_INTRODUCTION_RANGE,
			ErrorCode.INSTRUCTOR_INTRODUCTION_OUT_OF_RANGE);
		return nonNullIntroduction;
	}

	public void update(String name, String introduction) {
		if (name != null) {
			this.name = validateName(name);
		}
		if (introduction != null) {
			this.introduction = validateIntroduction(introduction);
		}
	}
}
