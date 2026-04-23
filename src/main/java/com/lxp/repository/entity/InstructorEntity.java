package com.lxp.repository.entity;

import java.time.LocalDateTime;

import com.lxp.domain.Instructor;
import com.lxp.repository.entity.enums.EntityStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InstructorEntity extends BaseEntity {

	private Long id;
	private String name;
	private String introduction;

	public static InstructorEntity create(Long id, Instructor instructor, LocalDateTime now) {
		InstructorEntity instructorEntity = new InstructorEntity();
		instructorEntity.id = id;
		instructorEntity.name = instructor.getName();
		instructorEntity.introduction = instructor.getIntroduction();
		instructorEntity.initialize(now);
		return instructorEntity;
	}

	public static InstructorEntity restore(
		Long id,
		String name,
		String introduction,
		EntityStatus status,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt
	) {
		InstructorEntity instructorEntity = new InstructorEntity();
		instructorEntity.id = id;
		instructorEntity.name = name;
		instructorEntity.introduction = introduction;
		instructorEntity.restore(status, createdAt, modifiedAt);
		return instructorEntity;
	}

	public void update(Instructor instructor, LocalDateTime now) {
		this.name = instructor.getName();
		this.introduction = instructor.getIntroduction();
		modified(now);
	}

	public void delete(LocalDateTime now) {
		super.delete(now);
	}

	public Instructor toDomain() {
		return Instructor.createWithId(id, name, introduction);
	}
}
