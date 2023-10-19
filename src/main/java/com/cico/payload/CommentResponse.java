package com.cico.payload;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentResponse {

	private Integer id;
	private LocalDateTime createdDate;
	private String content;
	private String studentName;
	private String studentProfilePic;
}
