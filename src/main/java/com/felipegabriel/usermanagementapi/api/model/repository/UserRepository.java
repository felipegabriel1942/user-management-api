package com.felipegabriel.usermanagementapi.api.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.felipegabriel.usermanagementapi.api.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	@Query("select u from User u where u.email = ?1")
	Optional<User> findByEmail(String email);
	
	@Query("select u from User u where u.login = ?1")
	Optional<User> findByLogin(String login);
	
	@Query("select u from User u where u.admin = true")
	List<User> findAllAdmins();
}
