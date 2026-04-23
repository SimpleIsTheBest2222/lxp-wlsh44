package com.lxp.repository;

import java.util.List;
import java.util.Optional;

import com.lxp.domain.Course;

public interface CourseRepository {

	Optional<Course> findById(Long id);

	List<Course> findAll();

	Course save(Course course);

	/**
	 * 강의를 soft delete 처리한다.
	 */
	void deleteById(Long id);
}
