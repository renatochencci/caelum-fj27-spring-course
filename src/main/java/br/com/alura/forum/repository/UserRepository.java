package br.com.alura.forum.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.alura.forum.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmail(String email);
	//Optional<User> findById(Long userId);
	
}
