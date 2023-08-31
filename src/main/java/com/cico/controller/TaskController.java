package com.cico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	public ResponseEntity<Task> getTaskById(@RequestParam("taskId") Integer taskId) {
		Task task = this.taskService.getTaskById(taskId);
		return new ResponseEntity<Task>(task, HttpStatus.OK);
	}

	@GetMapping("/getAllTask")
	public ResponseEntity<List<Task>> getAllTask() {
		List<Task> tasks = taskService.getAllTask();
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
	}

	@PostMapping("/studentTaskSubmittion")
	public ResponseEntity<?> StudentTaskSubmittion(@RequestParam("taskId")Integer taskId,@RequestParam("studentId") Integer studentId,
			@RequestParam(name = "submittionFileName", required = false) MultipartFile file,
			@RequestParam("taskDescription") String taskDescription) {

		return taskService.studentTaskSubmittion(taskId,studentId, file, taskDescription);

	}

	@PostMapping("/addQuestionInTask")
	public ResponseEntity<?> addQuestionInTask(@RequestParam("taskId") Integer taskId,
			@RequestParam("question") String question, @RequestParam("videoUrl") String videoUrl,
			@RequestParam("questionImages") List<MultipartFile> questionImages) {
		return taskService.addQuestionInTask(question, videoUrl, questionImages, taskId);

	}

	@PostMapping("/addTaskAttachment")
	public ResponseEntity<?> addTaskAttachment(@RequestParam("taskId") Integer taskId,
			@RequestParam("attachment") MultipartFile attachment) {
		return this.taskService.addTaskAttachment(taskId, attachment);
	}

	@DeleteMapping("/deleteTaskQuestion")
	public ResponseEntity<?> deleteTaskQuestions(@RequestParam("questionId") Long questionId,
			@RequestParam("taskId") Integer taskId) {
		return taskService.deleteTaskQuestion(taskId,questionId);
	}
	
	@GetMapping("/getAllSubmitedTask")
	public ResponseEntity<?> getAllSubmitedTasks(){
		return taskService.getAllSubmitedTasks();
	}

}
