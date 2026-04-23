package com.lxp.repository.entity;

import java.time.LocalDateTime;

import com.lxp.domain.Course;
import com.lxp.domain.enums.Level;
import com.lxp.repository.entity.enums.EntityStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseEntity extends BaseEntity {

	private Long id;
	private String title;
	private String description;
	private int price;
	private Level level;

	public static CourseEntity create(Long id, Course course, LocalDateTime now) {
		CourseEntity courseEntity = new CourseEntity();
		courseEntity.id = id;
		courseEntity.title = course.getTitle();
		courseEntity.description = course.getDescription();
		courseEntity.price = course.getPrice();
		courseEntity.level = course.getLevel();
		courseEntity.initialize(now);
		return courseEntity;
	}

	public static CourseEntity restore(
		Long id,
		String title,
		String description,
		int price,
		Level level,
		EntityStatus status,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt
	) {
		CourseEntity courseEntity = new CourseEntity();
		courseEntity.id = id;
		courseEntity.title = title;
		courseEntity.description = description;
		courseEntity.price = price;
		courseEntity.level = level;
		courseEntity.restore(status, createdAt, modifiedAt);
		return courseEntity;
	}

	public void update(Course course, LocalDateTime now) {
		this.title = course.getTitle();
		this.description = course.getDescription();
		this.price = course.getPrice();
		this.level = course.getLevel();
		modified(now);
	}

	public void delete(LocalDateTime now) {
		super.delete(now);
	}

	public Course toDomain() {
		return Course.createWithId(id, title, description, price, level, getCreatedAt(), getModifiedAt());
	}
}
