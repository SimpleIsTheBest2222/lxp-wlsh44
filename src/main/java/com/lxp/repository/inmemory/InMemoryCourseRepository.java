package com.lxp.repository.inmemory;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.lxp.domain.Course;
import com.lxp.exception.ErrorCode;
import com.lxp.exception.LxpException;
import com.lxp.repository.CourseRepository;
import com.lxp.repository.entity.CourseEntity;

public class InMemoryCourseRepository implements CourseRepository {

	private final Map<Long, CourseEntity> data = new LinkedHashMap<>();
	private long nextId = 1L;

	@Override
	public Optional<Course> findById(Long id) {
		return Optional.ofNullable(data.get(id))
			.filter(course -> !course.isDeleted())
			.map(CourseEntity::toDomain);
	}

	@Override
	public List<Course> findAll() {
		return data.values().stream()
			.filter(course -> !course.isDeleted())
			.map(CourseEntity::toDomain)
			.toList();
	}

	@Override
	public Course save(Course course) {
		LocalDateTime now = LocalDateTime.now();

		if (course.getId() == null) {
			return saveNew(course, now);
		}

		CourseEntity existingCourse = Optional.ofNullable(data.get(course.getId()))
			.orElseThrow(() -> new LxpException(ErrorCode.NOT_FOUND_COURSE));

		return saveExisting(course, existingCourse, now);
	}

	private Course saveNew(Course course, LocalDateTime now) {
		CourseEntity savedCourse = CourseEntity.create(nextId++, course, now);
		data.put(savedCourse.getId(), savedCourse);
		return savedCourse.toDomain();
	}

	private Course saveExisting(Course course, CourseEntity existingCourse, LocalDateTime now) {
		existingCourse.update(course, now);
		return existingCourse.toDomain();
	}

	@Override
	public void deleteById(Long id) {
		Optional.ofNullable(data.get(id))
			.ifPresent(course -> course.delete(LocalDateTime.now()));
	}
}
