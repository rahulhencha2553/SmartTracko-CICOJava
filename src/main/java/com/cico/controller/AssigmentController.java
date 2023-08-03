package com.cico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Assignment;
import com.cico.payload.ApiResponse;
import com.cico.service.IAssignmentService;
import com.cico.util.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/assignment")
@CrossOrigin("*")
public class AssigmentController {

	@Autowired
	private IAssignmentService service;
	
	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("/get")
	public ResponseEntity<String> get() {
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@PostMapping("/createAssignment")
	public ResponseEntity<ApiResponse> createAssignment(@RequestParam(name = "question") String question,
			@RequestParam(name = "hints", required = false) String[] hints,
			@RequestParam(name = "images", required = false) MultipartFile[] images)
			throws JsonMappingException, JsonProcessingException {
		 ApiResponse response = service.createAssignment(question, hints, images);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/getAssignment/{id}")
	public ResponseEntity<Assignment> getAssigment(@PathVariable("id") Integer id) {
		Assignment assignment = service.getAssignment(id);
		return new ResponseEntity<>(assignment,HttpStatus.OK);
	}

	@GetMapping("/getAllAssigment")
	public ResponseEntity<List<List<Assignment>>> AllAssigment() {
		List<List<Assignment>> allAssignment = service.getAllAssignment();
		return new ResponseEntity<>(allAssignment,HttpStatus.OK);
	}

	
	@DeleteMapping("deleteAssignment/{id}")
	public ResponseEntity<ApiResponse> deleteAssigment(@PathVariable("id") Integer id) {
		ApiResponse response =  service.deleteAssignment(id);
		return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
	}

	
//	// @PreAuthorize("hasRole('ADMIN')")
//	@PutMapping("/updateAssignment")
//	public ResponseEntity<ApiResponseDto> updateAssignment0(@RequestParam("id") Integer id,
//			@RequestParam(name = "question", required = false) String question,
//			@RequestParam(name = "imagesId", required = false) String[] imagesId,
//			@RequestParam(name = "images", required = false) MultipartFile[] images,
//			@RequestParam(name = "hints", required = false) String[] hints) {
//		Assignment updatedAssignment = service.updateAssignment(id, question, imagesId, images, hints);
//		return new ResponseEntity<ApiResponseDto>(new ApiResponseDto(200, "SUCCESS", updatedAssignment, null),
//				HttpStatus.OK);
//	}
//
//	@PutMapping("/updateAssignment")
//	public ResponseEntity<ApiResponseDto> updateAssignment1(@RequestParam("Assignment") Assignment assignment) {
//		Assignment updatedAssignment = service.updateAssignment1(assignment);
//		return new ResponseEntity<ApiResponseDto>(new ApiResponseDto(200, "SUCCESS", updatedAssignment, null),
//				HttpStatus.OK);
//	}
}
