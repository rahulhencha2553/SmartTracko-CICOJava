package com.cico.service;

import java.util.List;

import com.cico.model.Task;
import com.cico.payload.TaskFilterRequest;
import com.cico.payload.TaskRequest;

public interface ITaskService {

	Task createTask(TaskRequest taskRequest);

	void updateTaskStatus(int taskId);

	List<Task> getFilteredTasks(TaskFilterRequest taskFilter);

	Task getTaskById(Integer taskId);

}
