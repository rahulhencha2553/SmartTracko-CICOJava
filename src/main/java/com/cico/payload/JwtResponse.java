package com.cico.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {
	String token;

	public JwtResponse(String token) {
		super();
		this.token = token;
	}
	
}
