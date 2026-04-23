package com.lxp.config;

import com.lxp.domain.Content;
import com.lxp.domain.Course;
import com.lxp.domain.Instructor;
import com.lxp.domain.enums.ContentType;
import com.lxp.domain.enums.Level;
import com.lxp.repository.ContentRepository;
import com.lxp.repository.CourseRepository;
import com.lxp.repository.InstructorRepository;

public class MockDataInitializer {

	private final InstructorRepository instructorRepository;
	private final CourseRepository courseRepository;
	private final ContentRepository contentRepository;

	public MockDataInitializer(
		InstructorRepository instructorRepository,
		CourseRepository courseRepository,
		ContentRepository contentRepository
	) {
		this.instructorRepository = instructorRepository;
		this.courseRepository = courseRepository;
		this.contentRepository = contentRepository;
	}

	public void initialize() {
		if (!instructorRepository.findAll().isEmpty() || !courseRepository.findAll().isEmpty()) {
			return;
		}

		Instructor javaInstructor = instructorRepository.save(
			Instructor.create("김남준", "10년 경력의 자바 전문 강사입니다.")
		);
		Instructor springInstructor = instructorRepository.save(
			Instructor.create("홍길동", "실무 중심 스프링 백엔드 강사입니다.")
		);

		Course javaCourse = courseRepository.save(Course.create(
			javaInstructor.getId(),
			"자바의 정석",
			"자바 기초부터 심화까지 다루는 강의입니다.",
			100000,
			Level.LOW
		));
		Course springCourse = courseRepository.save(Course.create(
			springInstructor.getId(),
			"스프링 실전",
			"스프링 부트 기반 백엔드 실전 강의입니다.",
			149000,
			Level.MIDDLE
		));

		contentRepository.save(Content.create(
			javaCourse.getId(),
			"원시타입",
			"원시타입과 참조타입의 차이를 이해합니다.",
			ContentType.TEXT,
			1
		));
		contentRepository.save(Content.create(
			javaCourse.getId(),
			"for 문",
			"반복문의 기본 구조와 사용법을 익힙니다.",
			ContentType.TEXT,
			2
		));
		contentRepository.save(Content.create(
			javaCourse.getId(),
			"Stream",
			"스트림 API로 컬렉션을 처리하는 방법을 학습합니다.",
			ContentType.TEXT,
			3
		));
		contentRepository.save(Content.create(
			springCourse.getId(),
			"DI와 IoC",
			"스프링 컨테이너와 의존성 주입 개념을 이해합니다.",
			ContentType.TEXT,
			1
		));
		contentRepository.save(Content.create(
			springCourse.getId(),
			"JPA 기초",
			"엔티티 매핑과 영속성 컨텍스트를 학습합니다.",
			ContentType.TEXT,
			2
		));
	}
}
