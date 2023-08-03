package com.cico.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {
	String token;

	String userId;
	String name;
	String profilePic;
    String course;
	public JwtResponse(String token, String userId, String name, String profilePic, String course) {
		super();
		this.token = token;
		this.userId = userId;
		this.name = name;
		this.profilePic = profilePic;
		this.course = course;
	}
	
}
