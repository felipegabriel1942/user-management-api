package com.felipegabriel.usermanagementapi.api.resource;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.Arrays;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipegabriel.usermanagementapi.api.dto.UserDTO;
import com.felipegabriel.usermanagementapi.api.model.entity.User;
import com.felipegabriel.usermanagementapi.api.service.UserService;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
	
	static String USER_API = "/user";
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private UserService userService;
	
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
		        .apply(springSecurity())
		        .build();
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"admin"})
	public void shouldCreateUser() throws Exception {
		UserDTO user = UserDTO.builder()
				.name("test")
				.login("login")
				.admin(false)
				.email("test@email.com")
				.password("123").build();
		
		User savedUser = createValidUser();
		savedUser.setId(1);
		
		BDDMockito
			.given(userService.save(Mockito.any(User.class)))
			.willReturn(savedUser);
		
		String json = new ObjectMapper().writeValueAsString(user);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(USER_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
			.andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
			.andExpect(MockMvcResultMatchers.jsonPath("login").value(user.getLogin()))
			.andExpect(MockMvcResultMatchers.jsonPath("admin").value(user.isAdmin()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()))
			.andExpect(MockMvcResultMatchers.jsonPath("password").value(user.getPassword()));
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"user"})
	public void shouldNotCreateWhenUserHasNotPermission() throws Exception {
		UserDTO user = UserDTO.builder()
				.name("test")
				.login("login")
				.admin(false)
				.email("test@email.com")
				.password("123").build();
		
		User savedUser = createValidUser();
		savedUser.setId(1);
		
		BDDMockito
			.given(userService.save(Mockito.any(User.class)))
			.willReturn(savedUser);
		
		String json = new ObjectMapper().writeValueAsString(user);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(USER_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"user"})
	public void shouldFindUserById() throws Exception {
		User user = User.builder().id(1).build();
		
		BDDMockito
			.given(userService.getById(Mockito.anyInt()))
			.willReturn(Optional.of(user));
		
		String json = new ObjectMapper().writeValueAsString(1);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(USER_API.concat("/1"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(1));
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"user"})
	public void shouldThrownAnErrorWhenUserNotFound() throws Exception {
		BDDMockito
			.given(userService.getById(Mockito.anyInt()))
			.willReturn(Optional.empty());
		
		String json = new ObjectMapper().writeValueAsString(1);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(USER_API.concat("/1"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"admin"})
	public void shouldDeleteUser() throws Exception {
		BDDMockito
			.given(userService.getById(Mockito.anyInt()))
			.willReturn(Optional.of(User.builder().id(1).build()));
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.delete(USER_API.concat("/1"));
		
		mockMvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"user"})
	public void shouldNotDeleteWhenUserHasNotPermission() throws Exception {
		BDDMockito
			.given(userService.getById(Mockito.anyInt()))
			.willReturn(Optional.of(User.builder().id(1).build()));
	
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.delete(USER_API.concat("/1"));
	
		mockMvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"admin"})
	public void shouldUpdateUser() throws Exception {
		String json = new ObjectMapper().writeValueAsString(createValidUser());
		
		User updatingUser = User.builder()
				.id(1)
				.name("test")
				.login("login")
				.admin(false)
				.email("test@email.com")
				.password("123").build();
		
		BDDMockito.given(userService.getById(1))
			.willReturn(Optional.of(updatingUser));
		
		User updatedUser = User.builder()
				.id(1)
				.name("name edited")
				.login("login edited")
				.admin(true)
				.email("login_edited@email.com")
				.password("321").build();
		
		BDDMockito.given(userService.update(updatingUser))
			.willReturn(updatedUser);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(USER_API.concat("/" + 1))
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
			.andExpect(MockMvcResultMatchers.jsonPath("name").value(updatedUser.getName()))
			.andExpect(MockMvcResultMatchers.jsonPath("login").value(updatedUser.getLogin()))
			.andExpect(MockMvcResultMatchers.jsonPath("admin").value(updatedUser.isAdmin()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(updatedUser.getEmail()));
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"user"})
	public void shouldNotUpdateWhenUserHasNotPermission () throws Exception {
		String json = new ObjectMapper().writeValueAsString(createValidUser());
		
		User updatingUser = User.builder()
				.id(1)
				.name("test")
				.login("login")
				.admin(false)
				.email("test@email.com")
				.password("123").build();
		
		BDDMockito.given(userService.getById(1))
			.willReturn(Optional.of(updatingUser));
		
		User updatedUser = User.builder()
				.id(1)
				.name("name edited")
				.login("login edited")
				.admin(true)
				.email("login_edited@email.com")
				.password("321").build();
		
		BDDMockito.given(userService.update(updatingUser))
			.willReturn(updatedUser);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(USER_API.concat("/" + 1))
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isForbidden());

	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"admin"})
	public void shouldUpdatePassword() throws Exception {
		UserDTO user = UserDTO.builder()
				.id(1)
				.password("123").build();
		
		String json = new ObjectMapper().writeValueAsString(user);
		
		User updatingUser = User.builder()
				.id(1)
				.password("123").build();
		
		BDDMockito.given(userService.getById(1))
			.willReturn(Optional.of(updatingUser));
		
		User updatedUser = User.builder()
				.id(1)
				.password("321").build();
		
		BDDMockito.given(userService.update(updatingUser))
			.willReturn(updatedUser);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(USER_API.concat("/update-password/" + 1))
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("password").value(updatedUser.getPassword()));
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"user"})
	public void shouldNotUpdatePasswordWhenUserHasNotPermission() throws Exception {
		UserDTO user = UserDTO.builder()
				.id(1)
				.password("123").build();
		
		String json = new ObjectMapper().writeValueAsString(user);
		
		User updatingUser = User.builder()
				.id(1)
				.password("123").build();
		
		BDDMockito.given(userService.getById(1))
			.willReturn(Optional.of(updatingUser));
		
		User updatedUser = User.builder()
				.id(1)
				.password("321").build();
		
		BDDMockito.given(userService.update(updatingUser))
			.willReturn(updatedUser);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(USER_API.concat("/update-password/" + 1))
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"user"})
	public void shouldGetUsers() throws Exception {
		User user = createValidUser();
		
		BDDMockito.given(userService.getUsers(Mockito.any(Pageable.class)))
			.willReturn(new PageImpl<User>(Arrays.asList(user), PageRequest.of(0, 5), 1));
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(USER_API.concat("/0/users"))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("totalElements").value(1))
			.andExpect(MockMvcResultMatchers.jsonPath("pageable.pageSize").value(5))
			.andExpect(MockMvcResultMatchers.jsonPath("pageable.pageNumber").value(0));
	}
	
	private  User createValidUser() {
		return User.builder()
				.name("test")
				.login("login")
				.admin(false)
				.email("test@email.com")
				.password("123").build();
	}
}
