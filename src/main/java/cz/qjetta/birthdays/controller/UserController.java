package cz.qjetta.birthdays.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cz.qjetta.birthdays.entities.UserInfo;
import cz.qjetta.birthdays.requests.AuthRequest;
import cz.qjetta.birthdays.requests.UserToRegisterRequest;
import cz.qjetta.birthdays.service.JwtService;
import cz.qjetta.birthdays.service.UserService;

@RestController
@RequestMapping
public class UserController {

	@Autowired
	private UserService service;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome in Birthday application!";
	}

	@PostMapping("/register")
	public ResponseEntity<String> addNewUser(
			@RequestBody UserToRegisterRequest userToRegister) {
		var userInfo = new UserInfo();
		userInfo.setName(userToRegister.getName());
		userInfo.setEmail(userToRegister.getEmail());
		userInfo.setPassword(userToRegister.getPassword());
		userInfo.setRoles("DEFAULT");
		service.addUser(userInfo);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public String authenticateAndGetToken(
			@RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(
						authRequest.getUsername(), authRequest.getPassword()));

		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername());
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}
	}

}