package com.felipegabriel.usermanagementapi.api.resource;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import com.felipegabriel.usermanagementapi.api.dto.EmailDTO;
import com.felipegabriel.usermanagementapi.api.model.entity.User;
import com.felipegabriel.usermanagementapi.api.service.EmailService;
import com.felipegabriel.usermanagementapi.api.service.UserService;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class EmailControllerTest {
	
	static String EMAIL_API = "/email";
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private EmailService emailService;
	
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
	public void shouldSendEmail() throws Exception {
		EmailDTO email = EmailDTO.builder()
				.destination("test@test.yahoo.com.br")
				.subject("test")
				.text("test").build();
		
		
		String json = new ObjectMapper().writeValueAsString(email);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(EMAIL_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"roles"})
	public void shouldNotSendEmailWhenUserHasNotPermission() throws Exception {
		EmailDTO email = EmailDTO.builder()
				.destination("test@test.yahoo.com.br")
				.subject("test")
				.text("test").build();
		
		String json = new ObjectMapper().writeValueAsString(email);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(EMAIL_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"admin"})
	public void shouldSendEmailToAdmins() throws Exception {
		EmailDTO email = EmailDTO.builder()
				.subject("test")
				.text("test").build();
		
		String json = new ObjectMapper().writeValueAsString(email);
		
		BDDMockito
			.given(userService.getAllAdmins())
			.willReturn(Arrays.asList(createValidUser()));
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(EMAIL_API.concat("/admins"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"user"})
	public void shouldNotSendEmailToAdminsWhenUserHasNotPermission() throws Exception {
		EmailDTO email = EmailDTO.builder()
				.subject("test")
				.text("test").build();
		
		String json = new ObjectMapper().writeValueAsString(email);
		
		BDDMockito
			.given(userService.getAllAdmins())
			.willReturn(Arrays.asList(createValidUser()));
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(EMAIL_API.concat("/admins"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mockMvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isForbidden());
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
