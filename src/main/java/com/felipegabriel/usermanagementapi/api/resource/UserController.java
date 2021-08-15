package com.felipegabriel.usermanagementapi.api.resource;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.felipegabriel.usermanagementapi.api.dto.UserDTO;
import com.felipegabriel.usermanagementapi.api.model.entity.User;
import com.felipegabriel.usermanagementapi.api.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final ModelMapper modelMapper;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDTO save(@RequestBody @Valid UserDTO userDTO) {
		User user = modelMapper.map(userDTO, User.class);
		user.setCreatedDate(LocalDateTime.now());
		user = userService.save(user);
		return modelMapper.map(user, UserDTO.class);
	}
	
	@GetMapping("{id}")
	public UserDTO getById(@PathVariable Integer id) {
		return userService.getById(id)
			.map(user -> modelMapper.map(user, UserDTO.class))
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found."));
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Integer id) { 
		User user = userService.getById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found."));
		userService.delete(user);
	}
	
	@PutMapping("{id}")
	public UserDTO update(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
		User user = userService.getById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found."));
		
		user.setEmail(userDTO.getEmail());
		user.setName(userDTO.getName());
		user.setPassword(userDTO.getPassword());
		user.setAdmin(userDTO.isAdmin());
		user.setUpdatedDate(LocalDateTime.now());
		
		user = userService.update(user);
		return modelMapper.map(user, UserDTO.class);
	}
}
