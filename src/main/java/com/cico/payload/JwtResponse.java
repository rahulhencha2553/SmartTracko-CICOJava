package com.cico.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {
	String token;
	int studentId;
	String studentName;
	String profilePic;
    String course;
	public JwtResponse(String token, int studentId, String studentName, String profilePic, String course) {
		super();
		this.token = token;
		this.studentId = studentId;
		this.studentName = studentName;
		this.profilePic = profilePic;
		this.course = course;
	}
    
}
