package com.felipegabriel.usermanagementapi.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.felipegabriel.usermanagementapi.api.exception.BusinessException;
import com.felipegabriel.usermanagementapi.api.model.entity.User;
import com.felipegabriel.usermanagementapi.api.model.repository.UserRepository;
import com.felipegabriel.usermanagementapi.api.service.impl.UserServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {
	
	UserService userService;
	
	@MockBean
	UserRepository userRepository;
	
	@BeforeEach
	public void setUp() {
		this.userService = new UserServiceImpl(userRepository);
	}
	
	@Test
	public void shouldSaveAnUser() {
		User user = createValidUser();
		
		User mockedResult =  User.builder()
				.id(1)
				.name("test")
				.login("login")
				.admin(false)
				.email("test@email.com")
				.password("123").build();
		
		Mockito.when(userRepository.save(user))
			.thenReturn(mockedResult);
		
		User savedUser = userService.save(user);
		
		assertThat(savedUser.getId()).isNotNull();
		assertThat(user.getPassword()).isEqualTo(savedUser.getPassword());
		assertThat(user.getName()).isEqualTo(savedUser.getName());
		assertThat(user.isAdmin()).isEqualTo(savedUser.isAdmin());
		assertThat(user.getLogin()).isEqualTo(savedUser.getLogin());
		assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
	}
	
	@Test
	public void shouldNotSaveWhenThereIsAlrealdyUserWithEmailRegistered() {
		User user = User.builder().email("test@email.com").build();
		
		Optional<User> mockedResult =  Optional.of(User.builder()
				.id(1)
				.email("test@email.com")
				.build());
		
		Mockito
			.when(userRepository.findByEmail(user.getEmail()))
			.thenReturn(mockedResult);
		
		Throwable exception = Assertions.catchThrowable(() -> userService.save(user));
		
		assertThat(exception)
			.isInstanceOf(BusinessException.class)
			.hasMessage("There is already a registered user with this e-mail.");
		
		Mockito.verify(userRepository, Mockito.never()).save(user);
	}
	
	@Test
	public void shouldNotSaveWhenThereIsAlrealdyUserWithLoginRegistered() {
		User user = User.builder().login("test").build();
		
		Optional<User> mockedResult =  Optional.of(User.builder()
				.id(1)
				.login("test")
				.build());
		
		Mockito.when(userRepository.findByLogin(user.getLogin()))
			.thenReturn(mockedResult);
		
		Throwable exception = Assertions.catchThrowable(() -> userService.save(user));
		
		assertThat(exception)
			.isInstanceOf(BusinessException.class)
			.hasMessage("There is already a registered user with this login.");
		
		Mockito.verify(userRepository, Mockito.never()).save(user);
	}
	
	@Test
	public void shouldFindUserById() {
		Integer id = 1;
		User user = createValidUser();
		user.setId(1);
		
		Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
		
		Optional<User> foundUser = userService.getById(id);
		
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get().getId()).isEqualTo(id);
	}
	
	@Test
	public void shouldUpdateUser() {
		Integer id = 1;
		
		User updatingUser = User.builder().id(id).build();
		
		User updatedUser = createValidUser();
		updatedUser.setId(id);
		
		Mockito.when(userRepository.save(updatingUser)).thenReturn(updatedUser);
		
		User result = userService.update(updatingUser);
		
		assertThat(result.getId()).isEqualTo(updatedUser.getId());
		assertThat(result.getPassword()).isEqualTo(updatedUser.getPassword());
		assertThat(result.getName()).isEqualTo(updatedUser.getName());
		assertThat(result.isAdmin()).isEqualTo(updatedUser.isAdmin());
		assertThat(result.getLogin()).isEqualTo(updatedUser.getLogin());
		assertThat(result.getEmail()).isEqualTo(updatedUser.getEmail());
	}
	
	@Test
	public void shouldNotUpdateWhenUserIdIsNull() {
		User user = new User();
		
		assertThrows(IllegalArgumentException.class, () -> userService.update(user));
		
		Mockito.verify(userRepository, Mockito.never()).save(user);
	}
	
	@Test
	public void shouldDeleteUser() {
		User user = User.builder().id(1).build();
		
		assertDoesNotThrow(() -> userService.delete(user));
		
		Mockito.verify(userRepository, Mockito.times(1)).delete(user);
	}
	
	@Test
	public void shouldNotDeleteWhenUserIdIsNull() {
		User user = new User();
		
		assertThrows(IllegalArgumentException.class, () -> userService.delete(user));
		
		Mockito.verify(userRepository, Mockito.never()).delete(user);
	}
	
	@Test
	public void shouldGetAllRegisteredAdmins() {
		User admin = User.builder().id(1).admin(true).build();
		
		Mockito
			.when(userRepository.findAllAdmins())
			.thenReturn(Arrays.asList(admin));
		
		List<User> result = userService.getAllAdmins();	
		
		assertThat(result.size()).isEqualTo(1);
		
	}
	
	@Test
	public void shouldGetUsers() {
		User user = createValidUser();
		user.setId(1);
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		List<User> users = Arrays.asList(user);
		
		Page<User> page = new PageImpl<>(users, pageRequest, users.size());
		
		Mockito
			.when(userRepository.findAll(pageRequest))
			.thenReturn(page);
		
		Page<User> result = userService.getUsers(pageRequest);
		
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent()).isEqualTo(users);
		assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);
	}
	
	private User createValidUser() {
		return User.builder()
				.name("test")
				.login("login")
				.admin(false)
				.email("test@email.com")
				.password("123").build();
	}
}
