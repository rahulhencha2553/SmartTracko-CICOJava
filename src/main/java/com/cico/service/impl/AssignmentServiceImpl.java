package com.cico.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Assignment;
import com.cico.model.AssignmentSubmission;
import com.cico.model.AssignmentTaskQuestion;
import com.cico.model.Course;
import com.cico.payload.AssignmentQuestionRequest;
import com.cico.payload.AssignmentRequest;
import com.cico.payload.AssignmentSubmissionRequest;
import com.cico.payload.SubmissionAssignmentTaskStatus;
import com.cico.repository.AssignmentRepository;
import com.cico.repository.AssignmentSubmissionRepository;
import com.cico.repository.AssignmentTaskQuestionRepository;
import com.cico.repository.CourseRepository;
import com.cico.repository.StudentRepository;
import com.cico.repository.SubjectRepository;
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
	private AssignmentTaskQuestionRepository assignmentTaskQuestionRepository;

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
		this.getAllSubmissionAssignmentTaskStatus();
		List<Assignment> assignments = assignmentRepository.findByIsActiveTrue();
		return new ResponseEntity<>(assignments, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAssignmentQuesById(Long questionId, Long assignmentId) {
		Map<String, Object> response = new HashMap<>();
		AssignmentTaskQuestion assignmentTaskQuestion = assignmentTaskQuestionRepository.findByQuestionId(questionId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.NO_DATA_FOUND));
		Assignment assignment = assignmentRepository.findByIdAndIsActive(assignmentId, true).get();
		response.put("question", assignmentTaskQuestion);
		response.put("attachment", assignment.getTaskAttachment());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> submitAssignment(MultipartFile file, AssignmentSubmissionRequest readValue) {
		AssignmentSubmission submission = new AssignmentSubmission();
		submission.setStudent(studentRepository.findByStudentId(readValue.getStudentId()));
		submission.setAssignmentId(readValue.getAssignmentId());
		submission.setTaskId(readValue.getTaskId());
		submission.setDescription(readValue.getDescription());
		submission.setSubmissionDate(LocalDateTime.now());
		submission.setStatus(SubmissionStatus.Unreviewed);
		;
		if (Objects.nonNull(file)) {
			String fileName = fileServiceImpl.uploadFileInFolder(file, ATTACHMENT_FILES_DIR);
			submission.setSubmitFile(fileName);
		}
		return new ResponseEntity<>(submissionRepository.save(submission), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> getSubmitedAssignmetByStudentId(Integer studentId) {
		List<AssignmentSubmission> assignmentByStudentId = submissionRepository
				.getSubmitAssignmentByStudentId(studentId);
		return new ResponseEntity<>(assignmentByStudentId, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllSubmitedAssginments() {
		List<AssignmentSubmission> obj = submissionRepository.findAll();
		return new ResponseEntity<>(obj, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> updateSubmitedAssignmentStatus(Long submissionId, String status, String review) {
		if (status.equals(SubmissionStatus.Reviewing.toString())) {
			submissionRepository.updateSubmitAssignmentStatus(submissionId, SubmissionStatus.Reviewing, review);
		} else if (status.equals(SubmissionStatus.Accepted.toString())) {
			submissionRepository.updateSubmitAssignmentStatus(submissionId, SubmissionStatus.Accepted, review);
		} else if (status.equals(SubmissionStatus.Rejected.toString())) {
			submissionRepository.updateSubmitAssignmentStatus(submissionId, SubmissionStatus.Rejected, review);
		}
		return new ResponseEntity<>(submissionRepository.findById(submissionId).get(), HttpStatus.CREATED);
	}

//	@Override
//	public ResponseEntity<?> addQuestionInAssignment2(String question, String videoUrl,
//			List<MultipartFile> questionImages, Long assignmentId) {
//		System.out.println(question);
//		System.out.println(videoUrl);
//		System.out.println(questionImages.toString());
//		Optional<Assignment> assignment = assignmentRepository.findByIdAndIsActive(assignmentId, true);
//		if (Objects.nonNull(assignment)) {
//			TaskQuestion taskQuestion = new TaskQuestion();
//			taskQuestion.setQuestion(question);
//			taskQuestion.setVideoUrl(videoUrl);
//			List<String> list = new ArrayList<>();
//			questionImages.forEach((t) -> {
//				String fileName = fileServiceImpl.uploadFileInFolder(t, QUESTION_IMAGES_DIR);
//				list.add(fileName);
//			});
//			taskQuestion.setQuestionImages(list);
//			List<TaskQuestion> assignmentQuestion = assignment.get().getAssignmentQuestion();
//			assignmentQuestion.add(taskQuestion);
//		}
//		return new ResponseEntity<>(assignmentRepository.save(assignment.get()), HttpStatus.OK);
//	}

	@Override
	public ResponseEntity<?> addQuestionInAssignment2(String question, String videoUrl,
			List<MultipartFile> questionImages, Long assignmentId) {

		Optional<Assignment> assignmentOptional = assignmentRepository.findByIdAndIsActive(assignmentId, true);

		if (assignmentOptional.isPresent()) {
			Assignment assignment = assignmentOptional.get();

			AssignmentTaskQuestion assignmentTaskQuestion = new AssignmentTaskQuestion();
			assignmentTaskQuestion.setQuestion(question);
			assignmentTaskQuestion.setVideoUrl(videoUrl);
			assignmentTaskQuestion.setAssignmentId(assignmentId);
			List<String> fileNames = questionImages.stream()
					.map(file -> fileServiceImpl.uploadFileInFolder(file, QUESTION_IMAGES_DIR))
					.collect(Collectors.toList());

			assignmentTaskQuestion.setQuestionImages(fileNames);

			assignment.getAssignmentQuestion().add(assignmentTaskQuestion);

			Assignment updatedAssignment = assignmentRepository.save(assignment);

			return new ResponseEntity<>(updatedAssignment, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
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
		if (Objects.nonNull(assignment)) {
			String fileName = fileServiceImpl.uploadFileInFolder(attachment, QUESTION_IMAGES_DIR);
			assignment.setTaskAttachment(fileName);
		}

		assignmentRepository.save(assignment);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllSubmissionAssignmentTaskStatus() {

		List<Assignment> assignments = assignmentRepository.findByIsActiveTrue();

		List<SubmissionAssignmentTaskStatus> assignmentTaskStatusList = new ArrayList<>();

		assignments.forEach(assignment -> {
			SubmissionAssignmentTaskStatus assignmentTaskStatus = new SubmissionAssignmentTaskStatus();
			assignmentTaskStatus.setAssignmentId(assignment.getId());
			int totalSubmitted = 0;
			int underReviewed = 0;
			int reviewed = 0;
			int taskCount = 0;
			List<AssignmentTaskQuestion> questions = assignment.getAssignmentQuestion();
			for (AssignmentTaskQuestion q : questions) {

				List<AssignmentSubmission> submissionAssignments = submissionRepository
						.getSubmitAssignmentByAssignmentId(assignment.getId(), q.getQuestionId());
				totalSubmitted += submissionAssignments.size();
				for (AssignmentSubmission submission : submissionAssignments) {
					if (submission.getStatus().equals(SubmissionStatus.Unreviewed)) {
						underReviewed += 1;
					} else if (submission.getStatus().equals(SubmissionStatus.Reviewing)
							|| submission.getStatus().equals(SubmissionStatus.Accepted)
							|| submission.getStatus().equals(SubmissionStatus.Rejected)) {
						reviewed += 1;
					}
				}
				// assignmentTaskStatus.setTaskId(q.getQuestionId());
				assignmentTaskStatus.setTaskCount(++taskCount);
				assignmentTaskStatus.setUnReveiwed(underReviewed);
				assignmentTaskStatus.setReveiwed(reviewed);
				assignmentTaskStatus.setTotalSubmitted(totalSubmitted);
				assignmentTaskStatus.setAssignmentTitle(assignment.getTitle());
				assignmentTaskStatusList.add(assignmentTaskStatus);
			}
		});
		this.getOverAllAssignmentTaskStatus();
		return ResponseEntity.ok(assignmentTaskStatusList);
	}

	@Override
	public ResponseEntity<?> getOverAllAssignmentTaskStatus() {

		List<Assignment> assignments = assignmentRepository.findByIsActiveTrue();
		int totalSubmitted = 0;
		int underReviewed = 0;
		int reviewed = 0;
		SubmissionAssignmentTaskStatus assignmentTaskStatus = new SubmissionAssignmentTaskStatus();
		for (Assignment assignment : assignments) {
			List<AssignmentTaskQuestion> questions = assignment.getAssignmentQuestion();
			for (AssignmentTaskQuestion q : questions) {
				List<AssignmentSubmission> submissionAssignments = submissionRepository
						.getSubmitAssignmentByAssignmentId(assignment.getId(), q.getQuestionId());
				totalSubmitted += submissionAssignments.size();

				for (AssignmentSubmission submission : submissionAssignments) {
					if (submission.getStatus().equals(SubmissionStatus.Unreviewed)) {
						underReviewed += 1;
					} else if (submission.getStatus().equals(SubmissionStatus.Reviewing)
							|| submission.getStatus().equals(SubmissionStatus.Accepted)
							|| submission.getStatus().equals(SubmissionStatus.Rejected)) {
						reviewed += 1;
					}
				}
			}
		}
		assignmentTaskStatus.setUnReveiwed(underReviewed);
		assignmentTaskStatus.setReveiwed(reviewed);
		assignmentTaskStatus.setTotalSubmitted(totalSubmitted);
		return ResponseEntity.ok(assignmentTaskStatus);
	}

	@Override
	public ResponseEntity<?> getAllLockedAndUnlockedAssignment(Integer studentId) {

		Map<String, Object> response = new HashMap<>();
		List<Assignment> lockedAssignment = new ArrayList<>();
		List<Assignment> unLockedAssignment = new ArrayList<>();

		List<Assignment> allAssignment = assignmentRepository
				.findAllByCourseId(studentRepository.findById(studentId).get().getCourse().getCourseId());

		if (!allAssignment.isEmpty()) {
			unLockedAssignment.add(allAssignment.get(0));
		}

		// unlocking the assignments
		int index = 0;
		if (!allAssignment.isEmpty()) {
			for (int i = 0; i < allAssignment.size(); i++) {

				List<AssignmentTaskQuestion> questions = allAssignment.get(i).getAssignmentQuestion();
				List<AssignmentSubmission> submittedAssignment = submissionRepository
						.findByAssignmentIdAndStudentId(allAssignment.get(i).getId(), studentId);

				int taskCount = 0;
				for (AssignmentSubmission submission : submittedAssignment) {
					if ("Accepted".equals(submission.getStatus().name())) {
						taskCount++;
					}
				}

				if (taskCount == questions.size()) {
					if (i < allAssignment.size() - 1) {
						unLockedAssignment.add(allAssignment.get(index + 1));
					}
				} else {
					for (int j = index; j < allAssignment.size(); j++) {
						lockedAssignment.add(allAssignment.get(i));
					}
					break;
				}
				index++;
			}
		}

		response.put("lockedAssignment", lockedAssignment);
		response.put("unLockedAssignment", unLockedAssignment);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAssignmentQuesSubmissionStatus(Long questionId, Long assignmentId, Integer studentId) {
		AssignmentSubmission submission = submissionRepository.findByAssignmentIdAndQuestionIdAndStudentId(assignmentId,
				questionId, studentId);
		if (Objects.nonNull(submission)) {
			return new ResponseEntity<>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(false, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> getAllSubmissionAssignmentTaskStatusByCourseId(Integer courseId) {

		Optional<Course> findByCourseId = courseRepo.findByCourseId(courseId);
		if (Objects.nonNull(findByCourseId)) {
			assignmentRepository.findAllByCourseId(courseId);
			List<Assignment> assignments = assignmentRepository.findAllByCourseId(courseId);
			int totalSubmitted = 0;
			int underReviewed = 0;
			int reviewed = 0;
			SubmissionAssignmentTaskStatus assignmentTaskStatus = new SubmissionAssignmentTaskStatus();
			for (Assignment assignment : assignments) {
				List<AssignmentTaskQuestion> questions = assignment.getAssignmentQuestion();
				for (AssignmentTaskQuestion q : questions) {
					List<AssignmentSubmission> submissionAssignments = submissionRepository
							.getSubmitAssignmentByAssignmentId(assignment.getId(), q.getQuestionId());
					totalSubmitted += submissionAssignments.size();

					for (AssignmentSubmission submission : submissionAssignments) {
						if (submission.getStatus().equals(SubmissionStatus.Unreviewed)) {
							underReviewed += 1;
						} else if (submission.getStatus().equals(SubmissionStatus.Reviewing)
								|| submission.getStatus().equals(SubmissionStatus.Accepted)
								|| submission.getStatus().equals(SubmissionStatus.Rejected)) {
							reviewed += 1;
						}
					}
				}
			}
			assignmentTaskStatus.setUnReveiwed(underReviewed);
			assignmentTaskStatus.setReveiwed(reviewed);
			assignmentTaskStatus.setTotalSubmitted(totalSubmitted);
			return ResponseEntity.ok(assignmentTaskStatus);
		}
		return ResponseEntity.notFound().build();
	}

}
