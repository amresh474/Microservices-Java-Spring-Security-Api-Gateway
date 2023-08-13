package com.lcwa.user.service.config;

import java.util.Base64;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	public String generateToken(Authentication authentication) {
		String email = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + 3600000);// millisecond

		String token = Jwts.builder().setSubject(email).setIssuedAt(currentDate).setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode("JWTSEcreKey".getBytes())).compact();
		return token;
	}

	public String getEmailFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(Base64.getEncoder().encode("JWTSEcreKey".getBytes()))
				.parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(Base64.getEncoder().encode("JWTSEcreKey".getBytes())).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			throw new Error(e.getMessage());
		}

	}

}
