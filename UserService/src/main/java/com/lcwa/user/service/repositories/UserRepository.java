package com.lcwa.user.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwa.user.service.entities.Users;

public interface UserRepository extends JpaRepository<Users, String> {

	// if you want to implement any custom method or query
	// write
	Users findByEmail(String email);

}
