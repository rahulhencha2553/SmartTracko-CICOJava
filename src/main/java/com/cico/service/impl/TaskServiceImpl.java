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
import com.cico.model.Course;
import com.cico.model.StudentTaskSubmittion;
import com.cico.model.Subject;
import com.cico.model.Task;
import com.cico.model.TaskQuestion;
import com.cico.payload.TaskFilterRequest;
import com.cico.payload.TaskRequest;
import com.cico.repository.StudentRepository;
import com.cico.repository.StudentTaskSubmittionRepository;
import com.cico.repository.SubjectRepository;
import com.cico.repository.TaskQuestionRepository;
import com.cico.repository.TaskRepo;
import com.cico.service.ITaskService;
import com.cico.util.SubmissionStatus;

@Service
public class TaskServiceImpl implements ITaskService {

	@Autowired
	TaskRepo taskRepo;

	@Autowired
	FileServiceImpl fileService;
	
	@Value("${questionImages}")
	private String QUESTION_IMAGES_DIR;

	@Value("${attachmentFiles}")
	private String ATTACHMENT_FILES_DIR;

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
			String f = fileService.uploadFileInFolder(file, ATTACHMENT_FILES_DIR);
			submittion.setSubmittionFileName(f);
			System.out.println("111111111111111"+f);
		}
		submittion.setTaskId(taskId);
		submittion.setStatus(SubmissionStatus.Unreviewed);
		submittion.setSubmissionDate(LocalDateTime.now());
		submittion.setTaskDescription(taskDescription);
		StudentTaskSubmittion object = studentTaskSubmittionRepository.save(submittion);
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
		Optional<Task> task = taskRepo.findByTaskIdAndIsActive(taskId, true);

		if (task.isPresent()) {
			String fileName = fileService.uploadFileInFolder(attachment, ATTACHMENT_FILES_DIR);
			task.get().setTaskAttachment(fileName);
			taskRepo.save(task.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<?> deleteTaskQuestion(Integer taskId, Long questionId) {
		TaskQuestion taskQuestion = taskQuestionRepository.findByQuestionId(questionId).get();
		Task task = taskRepo.findByTaskIdAndIsActive(taskId, true).get();
		List<TaskQuestion> newTaskQuestionList = new ArrayList<>(task.getTaskQuestion());
		taskRepo.deleteTaskQuestionByTaskId(taskId, taskQuestion, newTaskQuestionList);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllSubmitedTasks() {
		return new ResponseEntity<>(studentTaskSubmittionRepository.findAll(),HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getSubmitedTaskForStudent(Integer studentId) {
		List<StudentTaskSubmittion> submitedTaskForStudent = studentTaskSubmittionRepository.getSubmitedTaskForStudent(studentId);
		return new ResponseEntity<>(submitedTaskForStudent,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateSubmitedTaskStatus(Integer submissionId, String status, String review) {
		if(status.equals(SubmissionStatus.Reviewing.toString())) {
			 studentTaskSubmittionRepository.updateSubmitTaskStatus(submissionId,SubmissionStatus.Reviewing,review);
		}else if(status.equals(SubmissionStatus.Accepted.toString())) {
			studentTaskSubmittionRepository.updateSubmitTaskStatus(submissionId,SubmissionStatus.Accepted,review);
		}else if(status.equals(SubmissionStatus.Rejected.toString())){
			studentTaskSubmittionRepository.updateSubmitTaskStatus(submissionId,SubmissionStatus.Rejected,review);
		}
		return new ResponseEntity<>(studentTaskSubmittionRepository.findBySubmissionId(submissionId),HttpStatus.CREATED);
	}

}
