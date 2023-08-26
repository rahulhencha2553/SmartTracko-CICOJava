package com.cico.service;

import org.springframework.http.ResponseEntity;

import com.cico.model.Assignment;
import com.cico.payload.ApiResponse;
import com.cico.payload.AssignmentQuestionRequest;
import com.cico.payload.AssignmentRequest;

public interface IAssignmentService {

	Assignment getAssignment(Long id);

	ResponseEntity<?> createAssignment(AssignmentRequest assignmentRequest);

	ResponseEntity<?> addQuestionInAssignment(AssignmentQuestionRequest questionRequest);

}
