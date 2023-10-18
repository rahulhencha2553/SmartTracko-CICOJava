package com.cico.payload;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CommentResponse {

	private Integer id;
	private LocalDate createdDate;
	private String content;
	private String studentName;
	private String studentProfilePic;
}
