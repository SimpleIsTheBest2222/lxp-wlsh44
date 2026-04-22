package com.lxp.repository.inmemory;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.lxp.domain.Instructor;
import com.lxp.repository.InstructorRepository;
import com.lxp.repository.entity.InstructorEntity;

public class InMemoryInstructorRepository implements InstructorRepository {

	private final Map<Long, InstructorEntity> data = new LinkedHashMap<>();
	private long nextId = 1L;

	@Override
	public Optional<Instructor> findById(Long id) {
		return Optional.ofNullable(data.get(id))
			.filter(instructor -> !instructor.isDeleted())
			.map(InstructorEntity::toDomain);
	}

	@Override
	public List<Instructor> findAll() {
		return data.values().stream()
			.filter(instructor -> !instructor.isDeleted())
			.map(InstructorEntity::toDomain)
			.toList();
	}

	@Override
	public Instructor save(Instructor instructor) {
		LocalDateTime now = LocalDateTime.now();

		if (instructor.getId() == null) {
			return saveNew(instructor, now);
		}

		InstructorEntity existingInstructor = data.get(instructor.getId());
		if (existingInstructor == null) {
			return saveWithId(instructor, now);
		}

		return saveExisting(instructor, existingInstructor, now);
	}

	private Instructor saveNew(Instructor instructor, LocalDateTime now) {
		InstructorEntity savedInstructor = InstructorEntity.create(nextId++, instructor, now);
		data.put(savedInstructor.getId(), savedInstructor);
		return savedInstructor.toDomain();
	}

	private Instructor saveWithId(Instructor instructor, LocalDateTime now) {
		InstructorEntity savedInstructor = InstructorEntity.create(instructor.getId(), instructor, now);
		data.put(savedInstructor.getId(), savedInstructor);
		return savedInstructor.toDomain();
	}

	private Instructor saveExisting(Instructor instructor, InstructorEntity existingInstructor, LocalDateTime now) {
		existingInstructor.update(instructor, now);
		return existingInstructor.toDomain();
	}

	@Override
	public void deleteById(Long id) {
		Optional.ofNullable(data.get(id))
			.ifPresent(instructor -> instructor.delete(LocalDateTime.now()));
	}
}
