package com.cico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cico.model.Task;

public interface TaskRepo extends JpaRepository<Task, Integer> {

	Task findByTaskNameAndIsActive(String taskName, boolean b);

	Object findByTaskName(String taskName);

}
