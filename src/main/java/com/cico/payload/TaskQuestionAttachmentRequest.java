package com.cico.payload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Task;
import com.cico.model.TaskQuestion;

import lombok.Data;

@Data
public class TaskQuestionAttachmentRequest {
	
	private MultipartFile taskAttachment;
	private List<TaskQuestion> question;
	private Task task;
}
