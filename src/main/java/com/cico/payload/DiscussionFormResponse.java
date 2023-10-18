package com.cico.payload;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class DiscussionFormResponse {
   
	private Integer id;
	private LocalDate createdDate;
	private String content;
	private String studentName;
	private String studentProfilePic;
    private List<LikeResponse>likes;
    private List<CommentResponse>comments;

}
