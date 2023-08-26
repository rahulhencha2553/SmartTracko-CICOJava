package com.cico.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Assignment;
import com.cico.model.TaskQuestion;
import com.cico.payload.ApiResponse;
import com.cico.payload.AssignmentQuestionRequest;
import com.cico.payload.AssignmentRequest;
import com.cico.payload.TaskQuestionRequest;
import com.cico.repository.AssignmentRepository;
import com.cico.repository.CourseRepository;
import com.cico.repository.SubjectRepository;
import com.cico.service.IAssignmentService;
import com.cico.util.AppConstants;

@Service
public class AssignmentServiceImpl implements IAssignmentService {

	@Autowired
	private AssignmentRepository assignmentRepository;

	@Autowired
	private CourseRepository courseRepo;

	@Autowired
	private SubjectRepository subjectRepo;

	@Value("${questionImages}")
	private String QUESTION_IMAGES_DIR;
	
	@Value("${attachmentFiles}")
	private String ATTACHMENT_FILES_DIR;
	
	@Autowired
	private FileServiceImpl fileServiceImpl;

	@Override
	public Assignment getAssignment(Long id) {
		return assignmentRepository.findByIdAndIsActive(id, true)
				.orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));
	}

	@Override
	public ResponseEntity<?> createAssignment(AssignmentRequest assignmentRequest) {
		System.out.println(assignmentRequest);
		Assignment assignment = new Assignment();
		assignment.setTitle(assignmentRequest.getTitle());

		assignment.setCourse(courseRepo.findById(assignmentRequest.getCourseId()).get());

		if (assignmentRequest.getSubjectId() != null)
			assignment.setSubject(subjectRepo.findById(assignmentRequest.getSubjectId()).get());

		assignment.setCreatedDate(LocalDateTime.now());
		Assignment savedAssignment = assignmentRepository.save(assignment);
		return new ResponseEntity<>(savedAssignment, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> addQuestionInAssignment(AssignmentQuestionRequest questionRequest) {
		System.out.println(questionRequest);
		Assignment assignment = assignmentRepository.findByIdAndIsActive(questionRequest.getAssignmentId(), true)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));
		List<TaskQuestion> taskQuestionList = new ArrayList<>();
		for (TaskQuestionRequest request: questionRequest.getAssignmentQuestion()) {
			TaskQuestion taskQue = new TaskQuestion();
			taskQue.setQuestion(request.getQuestion());
			taskQue.setVideoUrl(request.getVideoUrl());
			List<String> imagesName = new ArrayList<>();
			for (MultipartFile image : request.getQuestionImages()) {
				imagesName.add(fileServiceImpl.uploadFileInFolder(image, QUESTION_IMAGES_DIR));
			}
			taskQue.setQuestionImages(imagesName);
		}
		assignment.setAssignmentQuestion(taskQuestionList);
		Assignment save = assignmentRepository.save(assignment);
		if(Objects.nonNull(save))
			return new ResponseEntity<>(save,HttpStatus.CREATED);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
