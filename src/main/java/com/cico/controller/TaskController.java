package com.cico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.model.Task;
import com.cico.payload.ApiResponse;
import com.cico.payload.TaskRequest;
import com.cico.service.ITaskService;

@RequestMapping("/task")
@RestController
@CrossOrigin("*")
public class TaskController {

	@Autowired
	ITaskService taskService;

	@PostMapping("/createTask")
	private ResponseEntity<Task> createTask(@RequestBody TaskRequest taskRequest) {
		Task task = taskService.createTask(taskRequest);
		return new ResponseEntity<Task>(task, HttpStatus.OK);

	}

	@PutMapping("/updateTaskStatus")
	private ResponseEntity<ApiResponse> updateTaskStatus(@RequestParam("taskId") int taskId) {
		taskService.updateTaskStatus(taskId);

		return ResponseEntity.ok(new ApiResponse(true, "Task Created", HttpStatus.OK));

	}
	@GetMapping("/getTaskById")
   public ResponseEntity<Task>getTaskById(@RequestParam("taskId")Integer taskId){
      Task task=  this.taskService.getTaskById(taskId);   
      return new ResponseEntity<Task>(task,HttpStatus.OK);
   }
}
