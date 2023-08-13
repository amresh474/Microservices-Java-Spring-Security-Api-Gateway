package com.lcwa.user.service.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;

import com.lcwa.user.service.config.JwtTokenProvider;
import com.lcwa.user.service.entities.JWTAuth;
import com.lcwa.user.service.entities.LoginDto;
import com.lcwa.user.service.entities.Users;
import com.lcwa.user.service.services.UserService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

	@Autowired
	private UserService userService;

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	// create
	@PostMapping("/auth/register")
	public ResponseEntity<Users> createUser(@RequestBody Users user) {
		Users user1 = userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(user1);
	}

	// single user get

	@GetMapping("/users/{userId}")
//	@CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
//	@Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
	@RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
	public ResponseEntity<Users> getSingleUser(@PathVariable String userId) {
		logger.info("Get Single Users Handler: UserController");
		Users user = userService.getUser(userId);
		return ResponseEntity.ok(user);
	}

	/* creating fall back method for circuit breaker */

	public ResponseEntity<Users> ratingHotelFallback(String userId, Exception ex) {
//        logger.info("Fallback is executed because service is down : ", ex.getMessage());
		ex.printStackTrace();
		Users user = Users.builder().email("dummy@gmail.com").name("Dummy")
				.about("This user is created dummy because some service is down").userId("141234").build();
		return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
	}

	// all user get
	@GetMapping("/users")
	public ResponseEntity<List<Users>> getAllUser() {
		List<Users> allUser = userService.getAllUser();
		return ResponseEntity.ok(allUser);
	}

	// POST login user
	@PostMapping("/auth/login")
	public ResponseEntity<JWTAuth> loginUSer(@RequestBody LoginDto lonDto) {
		 logger.info("Fallback is executed because service is down : ",lonDto);
		org.springframework.security.core.Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(lonDto.getEmail(), lonDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		// get the token
		String token = jwtTokenProvider.generateToken(authentication);
		JWTAuth jwtAuthDto = new JWTAuth();
		jwtAuthDto.setToken(token);
		jwtAuthDto.setEmail(authentication.getName());
		return new ResponseEntity<>(jwtAuthDto, HttpStatus.OK);
	}

}
