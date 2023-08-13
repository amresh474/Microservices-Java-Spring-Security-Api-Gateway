package com.lcwa.gateway.util;

import java.util.Base64;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(Base64.getEncoder().encode("JWTSEcreKey".getBytes())).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			throw new Error(e.getMessage());
		}

	}

}
