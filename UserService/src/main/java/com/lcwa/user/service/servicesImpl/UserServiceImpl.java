package com.lcwa.user.service.servicesImpl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lcwa.user.service.entities.Hotel;
import com.lcwa.user.service.entities.Rating;
import com.lcwa.user.service.entities.Users;
import com.lcwa.user.service.repositories.UserRepository;
import com.lcwa.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public Users saveUser(Users user) {
		// generate unique user id
		String randomUserId = UUID.randomUUID().toString();
		user.setUserId(randomUserId);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public List<Users> getAllUser() {
		// implement RATING SERVICE CALL: USING REST TEMPLATE
		return userRepository.findAll();
	}

	// get single user
	@Override
	public Users getUser(String userId) {
		// get user from database with the help of user repository
		Users user = userRepository.findById(userId).get();
		// fetch rating of the above user from RATING SERVICE
		// http://localhost:8083/ratings/users/47e38dac-c7d0-4c40-8582-11d15f185fad

		Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(),
				Rating[].class);
		logger.info("{} ", ratingsOfUser);
		List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();
		List<Rating> ratingList = ratings.stream().map(rating -> {
			// api call to hotel service to get the hotel
			// http://localhost:8082/hotels/1cbaf36d-0b28-4173-b5ea-f1cb0bc0a791
			ResponseEntity<Hotel> forEntity = restTemplate
					.getForEntity("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);
//			Hotel hotel = hotelService.getHotel(rating.getHotelId());
			// logger.info("response status code: {} ",forEntity.getStatusCode());
			// set the hotel to rating
			rating.setHotel(forEntity.getBody());
			// return the rating
			return rating;
		}).collect(Collectors.toList());

		user.setRatings(ratingList);

		return user;
	}

}
