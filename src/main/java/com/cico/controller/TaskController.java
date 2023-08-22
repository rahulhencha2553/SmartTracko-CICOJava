package com.cico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.payload.ApiResponse;
import com.cico.payload.TaskRequest;
import com.cico.service.ITaskService;

@RequestMapping("/task")
@RestController
public class TaskController {

	@Autowired
	ITaskService taskService;

	@PostMapping("/createTask")
	private ResponseEntity<ApiResponse> createTask(@RequestBody TaskRequest taskRequest) {
		taskService.createTask(taskRequest);

		return ResponseEntity.ok(new ApiResponse(true, "Task Created", HttpStatus.OK));

	}
	
	@PutMapping("/updateTaskStatus")
	private ResponseEntity<ApiResponse> updateTaskStatus(@RequestParam("taskId") int taskId) {
		taskService.updateTaskStatus(taskId);

		return ResponseEntity.ok(new ApiResponse(true, "Task Created", HttpStatus.OK));

	}
}
