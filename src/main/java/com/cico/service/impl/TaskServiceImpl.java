package com.cico.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Course;
import com.cico.model.Subject;
import com.cico.model.Task;
import com.cico.payload.TaskFilterRequest;
import com.cico.payload.TaskRequest;
import com.cico.repository.SubjectRepository;
import com.cico.repository.TaskRepo;
import com.cico.service.ITaskService;

@Service
public class TaskServiceImpl implements ITaskService {

	@Autowired
	TaskRepo taskRepo;

	@Value("${questionImages}")
	String imageUploadPath;

	@Autowired
	FileServiceImpl fileService;

	@Autowired
	CourseServiceImpl courseService;

	@Autowired
	SubjectServiceImpl subjectService;

	@Autowired
	SubjectRepository subjectRepo;

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

}
