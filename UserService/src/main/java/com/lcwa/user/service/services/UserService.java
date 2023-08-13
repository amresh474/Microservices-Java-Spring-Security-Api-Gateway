package com.lcwa.user.service.services;

import java.util.List;

import com.lcwa.user.service.entities.Users;

public interface UserService {
	// user operations

	// create
	Users saveUser(Users user);

	// get all user
	List<Users> getAllUser();

	// get single user of given userId

	Users getUser(String userId);

	// TODO: delete
	// TODO: update

}
