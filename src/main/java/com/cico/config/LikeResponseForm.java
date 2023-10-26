package com.cico.config;

import java.util.ArrayList;
import java.util.List;

import com.cico.payload.LikeResponse;

import lombok.Data;

@Data
public class LikeResponseForm {
	
	public String type;
	// public LocalDate createdDate;
//	public String studentProfilePic;
	public Integer discussionFormId;
	public List<LikeResponse> likes = new ArrayList<>();

}
