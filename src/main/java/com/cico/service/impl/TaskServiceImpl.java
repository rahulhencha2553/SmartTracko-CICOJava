package com.cico.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.AssignmentSubmission;
import com.cico.model.Course;
import com.cico.model.StudentTaskSubmittion;
import com.cico.model.Subject;
import com.cico.model.Task;
import com.cico.model.TaskQuestion;
import com.cico.model.TaskSubmission;
import com.cico.payload.SubmissionAssignmentTaskStatus;
import com.cico.payload.TaskFilterRequest;
import com.cico.payload.TaskRequest;
import com.cico.repository.AssignmentTaskQuestionRepository;
import com.cico.repository.StudentRepository;
import com.cico.repository.StudentTaskSubmittionRepository;
import com.cico.repository.SubjectRepository;
import com.cico.repository.TaskQuestionRepository;
import com.cico.repository.TaskRepo;
import com.cico.repository.TaskSubmissionRepository;
import com.cico.service.ITaskService;
import com.cico.util.SubmissionStatus;

@Service
public class TaskServiceImpl implements ITaskService {

	@Autowired
	TaskRepo taskRepo;

	@Value("${questionImages}")
	String imageUploadPath;

	@Autowired
	FileServiceImpl fileService;
	@Value("${questionImages}")
	private String QUESTION_IMAGES_DIR;

	@Autowired
	CourseServiceImpl courseService;

	@Autowired
	SubjectServiceImpl subjectService;

	@Autowired
	SubjectRepository subjectRepo;

	@Autowired
	private StudentTaskSubmittionRepository studentTaskSubmittionRepository;

	@Autowired
	private TaskQuestionRepository taskQuestionRepository;
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private AssignmentTaskQuestionRepository assignmentTaskQuestionRepo;
	@Autowired
	private TaskSubmissionRepository taskSubmissionRepository;
	

	@Override
	public Task createTask(TaskRequest taskRequest) {
		if (taskRepo.findByTaskName(taskRequest.getTaskName()) != null)
			throw new ResourceAlreadyExistException("Task already exist");

		Task task = new Task();
		task.setAttachmentStatus(taskRequest.getAttachmentStatus());
		task.setCourse(taskRequest.getCourse());
		task.setSubject(taskRequest.getSubject());
		task.setTaskName(taskRequest.getTaskName());

		return taskRepo.save(task);
	}

	@Override
	public void updateTaskStatus(int taskId) {
		Task task = taskRepo.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

		if (task.getIsActive().equals(true))
			task.setIsActive(false);
		else
			task.setIsActive(true);

		taskRepo.save(task);

	}

	@Override
	public List<Task> getFilteredTasks(TaskFilterRequest taskFilter) {
		Example<Task> example = null;

		Course course = courseService.findCourseById(taskFilter.getCourseId());
		Subject subject = subjectRepo.findById(taskFilter.getSubjectId()).get();

		Task task = new Task();
		task.setCourse(course);
		task.setSubject(subject);
		task.setIsActive(taskFilter.getStatus());

		example = Example.of(task);

		return taskRepo.findAll(example);

	}

	@Override
	public Task getTaskById(Integer taskId) {
		return taskRepo.findById(taskId)
				.orElseThrow(() -> new ResourceNotFoundException("TASK NOT FOUND WITH THIS ID"));
	}

	@Override
	public List<Task> getAllTask() {
		return taskRepo.findAll();
	}

	@Override
	public ResponseEntity<?> studentTaskSubmittion(Integer taskId, Integer studentId, MultipartFile file,
			String taskDescription) {
		StudentTaskSubmittion submittion = new StudentTaskSubmittion();

		submittion.setStudent(studentRepository.findByStudentId(studentId));
		if (Objects.nonNull(file)) {
			String f = fileService.uploadFileInFolder(file, imageUploadPath);
			submittion.setSubmittionFileName(f);
			System.out.println("111111111111111"+f);
		}
		submittion.setTaskId(taskId);
		submittion.setStatus(SubmissionStatus.Unreviewed);
		submittion.setSubmissionDate(LocalDateTime.now());
		submittion.setTaskDescription(taskDescription);
		StudentTaskSubmittion object = studentTaskSubmittionRepository.save(submittion);
		System.out.println(object);
		if (!Objects.isNull(object)) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<?> addQuestionInTask(String question, String videoUrl, List<MultipartFile> questionImages,
			Integer taskId) {
		Optional<Task> taskOptional = taskRepo.findByTaskIdAndIsActive(taskId, true);

		if (taskOptional.isPresent()) {
			TaskQuestion taskQuestion = new TaskQuestion();
			taskQuestion.setQuestion(question);
			taskQuestion.setVideoUrl(videoUrl);
			List<String> list = new ArrayList<>();

			questionImages.forEach((t) -> {
				String fileName = fileService.uploadFileInFolder(t, QUESTION_IMAGES_DIR);
				System.out.println(fileName);
				list.add(fileName);
			});

			taskQuestion.setQuestionImages(list);

			 Task task = taskOptional.get();
			 task.getTaskQuestion().add(taskQuestion);

			Task save = taskRepo.save(task);
			return new ResponseEntity<>(save, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addTaskAttachment(Integer taskId, MultipartFile attachment) {
		System.out.println(taskId);
		System.out.println(attachment);
		Optional<Task> task = taskRepo.findByTaskIdAndIsActive(taskId, true);

		if (task.isPresent()) {
			String fileName = fileService.uploadFileInFolder(attachment, QUESTION_IMAGES_DIR);
			task.get().setTaskAttachment(fileName);
			taskRepo.save(task.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> deleteTaskQuestion(Integer taskId, Long questionId) {
//		AssignmentTaskQuestion assignmentTaskQuestion = taskQuestionRepository.findByQuestionId(questionId).get();
//		Task task = taskRepo.findByTaskIdAndIsActive(taskId, true).get();
//		List<AssignmentTaskQuestion> newTaskQuestionList = new ArrayList<>(task.getAssignmentTaskQuestion());
//		taskRepo.deleteTaskQuestionByTaskId(taskId, assignmentTaskQuestion, newTaskQuestionList);
//		return new ResponseEntity<>(HttpStatus.OK);
		return null;
	}

	@Override
	public ResponseEntity<?> getAllSubmitedTasks() {
		return new ResponseEntity<>(taskSubmissionRepository.findAll(),HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllSubmissionTaskStatus() {
		
		  List<Task>tasks = taskRepo.findByIsActiveTrue();

		List<SubmissionAssignmentTaskStatus> assignmentTaskStatusList = new ArrayList<>();

		tasks.forEach(task -> {
			SubmissionAssignmentTaskStatus assignmentTaskStatus = new SubmissionAssignmentTaskStatus();
			int totalSubmitted = 0;
			int underReviewed = 0;
			int reviewed = 0;
			int taskCount = 0;
			List<TaskQuestion> questions = task.getTaskQuestion();
			for (TaskQuestion q : questions) {
				taskCount+= 1;
				     List<TaskSubmission> taskSubmission = taskSubmissionRepository.getSubmitAssignmentByAssignmentId(q.getQuestionId());
				     totalSubmitted += taskSubmission.size();
				for (TaskSubmission submission : taskSubmission) {
					if (submission.getStatus().equals(SubmissionStatus.Unreviewed)) {
						underReviewed += 1;
					} else if (submission.getStatus().equals(SubmissionStatus.Reviewing)
							|| submission.getStatus().equals(SubmissionStatus.Accepted)
							|| submission.getStatus().equals(SubmissionStatus.Rejected)) {
						reviewed += 1;
					}
				}
				assignmentTaskStatus.setTaskId(q.getQuestionId());
				assignmentTaskStatus.setTaskCount(taskCount);
				assignmentTaskStatus.setUnderReveiwed(underReviewed);
				assignmentTaskStatus.setReveiwed(reviewed);
				assignmentTaskStatus.setTotalSubmitted(totalSubmitted);
				assignmentTaskStatusList.add(assignmentTaskStatus);
			}
		});
		return ResponseEntity.ok(assignmentTaskStatusList);
	}



}
