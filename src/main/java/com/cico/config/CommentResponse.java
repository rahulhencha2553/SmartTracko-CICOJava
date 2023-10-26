package com.cico.config;

import lombok.Data;

@Data
public class CommentResponse {

	public String type;
	public Integer id;
	public String createdDate;
	public String content;
	public String studentName;
	public String studentProfilePic;
	public Integer discussionFormId;
}
