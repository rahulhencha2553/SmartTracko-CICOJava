package com.cico.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Assignment;
import com.cico.payload.AssignmentRequest;
import com.cico.payload.AssignmentSubmissionRequest;
import com.cico.service.IAssignmentService;
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

	@PostMapping("/createAssignment")
	public ResponseEntity<?> createAssignment(@RequestBody AssignmentRequest assignmentRequest) {
		return service.createAssignment(assignmentRequest);
	}

	@GetMapping("/getAssignment")
	public ResponseEntity<Assignment> getAssigment(@RequestParam("assignmentId") Long id) {
		Assignment assignment = service.getAssignment(id);
		return ResponseEntity.ok(assignment);
	}

	@PostMapping("/addQuestionInAssignment")
	public ResponseEntity<?> addQuestionInAssignment(@RequestParam Map<String, List<MultipartFile>> questionImages) {

		// System.out.println(assignmentQuestionData);
		for (Map.Entry<String, List<MultipartFile>> entry : questionImages.entrySet()) {
			String questionIndex = entry.getKey();
			List<MultipartFile> imageList = entry.getValue();

			// Process imageList based on question index
			for (int imageIndex = 0; imageIndex < imageList.size(); imageIndex++) {
				MultipartFile imageFile = imageList.get(imageIndex);
				// Process the image file (e.g., save to storage, etc.)
			}
		}
		return null;
	}

	@GetMapping("/getAllAssignments")
	public ResponseEntity<?> getAllAssignments() {
		return service.getAllAssignments();
	}

	@GetMapping("/getAssignmentQuesById")
	public ResponseEntity<?> getAssignmentQuestion(@RequestParam("questionId") Long questionId,
			@RequestParam("assignmentId") Long assignmentId) {
		return service.getAssignmentQuesById(questionId, assignmentId);
	}

	@PostMapping("/submitAssignment")
	public ResponseEntity<?> submitAssignmentByStudent(@RequestParam("file") MultipartFile file,
			@RequestParam("assignmentSubmissionRequest") String assignmentSubmissionRequest)
			throws JsonMappingException, JsonProcessingException {
		AssignmentSubmissionRequest readValue = objectMapper.readValue(assignmentSubmissionRequest,
				AssignmentSubmissionRequest.class);
		return service.submitAssignment(file, readValue);
	}

	// This API for student Uses
	@GetMapping("/getSubmitedAssignmetByStudentId")
	public ResponseEntity<?> getSubmitedAssignmetByStudentId(@RequestParam("studentId") Integer studentId) {
		return service.getSubmitedAssignmetByStudentId(studentId);
	}

	// This API for Admin Uses
	@GetMapping("/getAllSubmitedAssginments")
	public ResponseEntity<?> getAllSubmitedAssginments() {
		return service.getAllSubmitedAssginments();
	}

	@PutMapping("/updateSubmitedAssignmentStatus")
	public ResponseEntity<?> updateSubmitedAssignmentStatus(@RequestParam("submissionId") Long submissionId,
			@RequestParam("status") String status,@RequestParam("review") String review) {
		return service.updateSubmitedAssignmentStatus(submissionId, status, review);
	}

}
