package com.cico.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Assignment;
import com.cico.model.Course;
import com.cico.model.Subject;
import com.cico.model.AssignmentSubmission;
import com.cico.model.TaskQuestion;
import com.cico.payload.ApiResponse;
import com.cico.payload.AssignmentQuestionRequest;
import com.cico.payload.AssignmentRequest;
import com.cico.payload.AssignmentSubmissionRequest;
import com.cico.payload.TaskQuestionRequest;
import com.cico.repository.AssignmentRepository;
import com.cico.repository.AssignmentSubmissionRepository;
import com.cico.repository.CourseRepository;
import com.cico.repository.StudentRepository;
import com.cico.repository.SubjectRepository;
import com.cico.repository.TaskQuestionRepository;
import com.cico.service.IAssignmentService;
import com.cico.util.AppConstants;
import com.cico.util.SubmissionStatus;

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

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private TaskQuestionRepository taskQuestionRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private AssignmentSubmissionRepository submissionRepository;

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
	public ResponseEntity<?> getAllAssignments() {
		return new ResponseEntity<>(assignmentRepository.findByIsActiveTrue(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAssignmentQuesById(Long questionId,Long assignmentId) {
		Map<String, Object> response = new HashMap<>();
		TaskQuestion taskQuestion = taskQuestionRepository.findByQuestionId(questionId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));
		Assignment assignment = assignmentRepository.findByIdAndIsActive(assignmentId,true).get();
		response.put("question", taskQuestion);
		response.put("attachment", assignment.getTaskAttachment());
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> submitAssignment(MultipartFile file, AssignmentSubmissionRequest readValue) {
		AssignmentSubmission submission = new AssignmentSubmission();
		submission.setStudent(studentRepository.findByStudentId(readValue.getStudentId()));
		submission.setAssignmentId(readValue.getAssignmentId());
		submission.setTaskId(readValue.getTaskId());
		submission.setDescription(readValue.getDescription());
		submission.setSubmissionDate(LocalDateTime.now());
		submission.setStatus(SubmissionStatus.Unreviewed);;
		if(Objects.nonNull(file)) {
			String fileName = fileServiceImpl.uploadFileInFolder(file, ATTACHMENT_FILES_DIR);
			submission.setSubmitFile(fileName);
		}
		return new ResponseEntity<>(submissionRepository.save(submission),HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> getSubmitedAssignmetByStudentId(Integer studentId) {
		List<AssignmentSubmission> assignmentByStudentId = submissionRepository.getSubmitAssignmentByStudentId(studentId);
		return new  ResponseEntity<>(assignmentByStudentId,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllSubmitedAssginments() {
		return new ResponseEntity<>(submissionRepository.findAll(),HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateSubmitedAssignmentStatus(Long submissionId,String status,String review) {
		if(status.equals(SubmissionStatus.Reviewing.toString())) {
			 submissionRepository.updateSubmitAssignmentStatus(submissionId,SubmissionStatus.Reviewing,review);
		}else if(status.equals(SubmissionStatus.Accepted.toString())) {
			 submissionRepository.updateSubmitAssignmentStatus(submissionId,SubmissionStatus.Accepted,review);
		}else if(status.equals(SubmissionStatus.Rejected.toString())){
			 submissionRepository.updateSubmitAssignmentStatus(submissionId,SubmissionStatus.Rejected,review);
		}
		return new ResponseEntity<>(submissionRepository.findById(submissionId).get(),HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> addQuestionInAssignment2(String question, String videoUrl,
			List<MultipartFile> questionImages, Long assignmentId) {
		System.out.println(question);
		System.out.println(videoUrl);
		System.out.println(questionImages.toString());
		Optional<Assignment> assignment = assignmentRepository.findByIdAndIsActive(assignmentId, true);
		if (Objects.nonNull(assignment)) {
			TaskQuestion taskQuestion = new TaskQuestion();
			taskQuestion.setQuestion(question);
			taskQuestion.setVideoUrl(videoUrl);
			List<String> list = new ArrayList<>();
			questionImages.forEach((t) -> {
				String fileName = fileServiceImpl.uploadFileInFolder(t, QUESTION_IMAGES_DIR);
				list.add(fileName);
			});
			taskQuestion.setQuestionImages(list);
			List<TaskQuestion> assignmentQuestion = assignment.get().getAssignmentQuestion();
			assignmentQuestion.add(taskQuestion);
		}
		return new ResponseEntity<>(assignmentRepository.save(assignment.get()), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> deleteTaskQuestion(Long questionId, Long assignmentId) {
		  assignmentRepository.deleteQuestionByIdAndId(assignmentId, questionId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addQuestionInAssignment(AssignmentQuestionRequest questionRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<?> addAssignment(Long assignmentId, MultipartFile attachment) {

		Assignment assignment = assignmentRepository.findById(assignmentId).get();
		if(Objects.nonNull(assignment)) {
			String fileName = fileServiceImpl.uploadFileInFolder(attachment,QUESTION_IMAGES_DIR );
			assignment.setTaskAttachment(fileName);
		}
	
		assignmentRepository.save(assignment);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
