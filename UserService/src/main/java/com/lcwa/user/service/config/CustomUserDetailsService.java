package com.lcwa.user.service.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lcwa.user.service.entities.Users;
import com.lcwa.user.service.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService  {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			Users user = userRepository.findByEmail(email);
			Set<String> roles = new HashSet<>();
			roles.add("ROLE_ADMIN");
			// TODO Auto-generated method stub
			return new User(user.getEmail(), user.getPassword(), userAuthorities(roles));
		} catch (Exception e) {
			throw e;
		}

	}

	private Collection<? extends GrantedAuthority> userAuthorities(Set<String> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
	}
}
