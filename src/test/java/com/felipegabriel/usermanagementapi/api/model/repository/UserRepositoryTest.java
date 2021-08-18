package com.felipegabriel.usermanagementapi.api.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.felipegabriel.usermanagementapi.api.model.entity.User;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {
	
	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	public void shouldFindUserByEmail() {
		User user = User.builder().email("test@email.com").build();
		entityManager.persist(user);
		
		Optional<User> result = userRepository.findByEmail("test@email.com");
			
		assertThat(result).isPresent();
	}
	
	@Test
	public void shouldFindUserByLogin() {
		User user = User.builder().login("test").build();
		entityManager.persist(user);
		
		Optional<User> result = userRepository.findByLogin("test");
			
		assertThat(result).isPresent();
	}
	
	@Test
	public void shouldFindAllAdmins() {
		User user1 = User.builder().admin(true).build();
		User user2 = User.builder().admin(true).build();
		User user3 = User.builder().admin(false).build();
		
		List<User> users = Arrays.asList(user1, user2, user3);
		
		users.forEach(entityManager::persist);
		
		List<User> result = userRepository.findAllAdmins();
		
		assertThat(result.size()).isEqualTo(2);
		
	}
}
