package com.cico.payload;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class DiscussionFormResponse {
   
	private Integer id;
	private LocalDateTime createdDate;
	private String content;
	private Integer studentId;
	private String studentName;
	private String studentProfilePic;
	private String file;
	private String courseName;
    private List<LikeResponse>likes;
    private List<CommentResponse>comments;

}
