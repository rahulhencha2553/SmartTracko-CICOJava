package com.cico.payload;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LikeResponse {

	private Integer id;
	private LocalDateTime createdDate;
	private String studentName;
	private String studentProfilePic;
}
