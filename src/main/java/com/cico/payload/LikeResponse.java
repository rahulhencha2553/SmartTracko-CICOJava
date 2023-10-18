package com.cico.payload;

import java.time.LocalDate;

import lombok.Data;

@Data
public class LikeResponse {

	private Integer id;
	private LocalDate createdDate;
	private String studentName;
	private String studentProfilePic;
}
