package com.example.jwt;

import com.example.jwt.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;

@SpringBootTest
class JwtApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {
	}
	
	@Test
	void tokenCreate() {
		var claims = new HashMap<String, Object>();
		claims.put("user_id", 923);
		
		// 10분만 유효한 토큰 만들기
		var expiredAt = LocalDateTime.now().plusSeconds(30);

		var jwtToken = jwtService.create(claims, expiredAt);

		System.out.println(jwtToken);
	}
	
	@Test
	void tokenValidation() {
		var token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo5MjMsImV4cCI6MTcwOTQ1ODIyOX0.8PCuTExnDfaX2H7Z4WT5a6hIL_Yvup4tZr4kG03YI3s";

		jwtService.validation(token);
	}

}
