package br.com.alura.forum.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import br.com.alura.forum.model.Course;

public interface CourseRepository extends Repository<Course, Long>{
		
	Optional<Course> findByName(String name);
}
