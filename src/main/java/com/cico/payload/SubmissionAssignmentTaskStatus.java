package com.cico.payload;

import lombok.Data;

@Data
public class SubmissionAssignmentTaskStatus {

	private Long assignmentId;
	private String taskTitle;
	private Integer taskId;
	private Integer totalSubmitted;
	private Integer unReveiwed;
	private boolean status;
	private Integer reveiwed;
	private Integer taskCount;
	private String assignmentTitle;
}
