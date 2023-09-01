package com.cico.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Task;
import com.cico.payload.TaskFilterRequest;
import com.cico.payload.TaskRequest;

public interface ITaskService {

	Task createTask(TaskRequest taskRequest);

	void updateTaskStatus(int taskId);

	List<Task> getFilteredTasks(TaskFilterRequest taskFilter);

	Task getTaskById(Integer taskId);

	List<Task> getAllTask();
	
	ResponseEntity<?> studentTaskSubmittion(Integer studentId, Integer studentId2, MultipartFile file, String taskDescription);

	ResponseEntity<?> addQuestionInTask(String question, String videoUrl, List<MultipartFile> questionImages,
			Integer taskId);

	ResponseEntity<?> addTaskAttachment(Integer taskId, MultipartFile attachment);

	ResponseEntity<?> deleteTaskQuestion(Integer taskId, Long questionId);

	ResponseEntity<?> getAllSubmitedTasks();


	ResponseEntity<?> getAllSubmissionTaskStatus();

	ResponseEntity<?> getSubmitedTaskForStudent(Integer studentId);

	ResponseEntity<?> updateSubmitedTaskStatus(Integer submissionId, String status, String review);

}
