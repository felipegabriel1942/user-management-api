package com.felipegabriel.usermanagementapi.api.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipegabriel.usermanagementapi.api.dto.UserDTO;
import com.felipegabriel.usermanagementapi.api.model.entity.User;
import com.felipegabriel.usermanagementapi.api.service.UserService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
	
	static String USER_API = "/user";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UserService userService;
	
	@MockBean
	ModelMapper modelMapper;
	
	@Test
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
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(1));
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
