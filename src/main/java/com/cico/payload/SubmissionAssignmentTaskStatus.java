package com.cico.payload;

import lombok.Data;

@Data
public class SubmissionAssignmentTaskStatus {

	private Long assignmentId;
	private String taskTitle;
	private Long taskId;
	private Integer totalSubmitted;
	private Integer underReveiwed;
	private boolean status;
	private Integer reveiwed;
	private Integer taskCount;
}
