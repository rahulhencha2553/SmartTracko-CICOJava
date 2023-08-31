package com.cico.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cico.model.Task;
import com.cico.model.TaskQuestion;

public interface TaskRepo extends JpaRepository<Task, Integer> {

	Task findByTaskNameAndIsActive(String taskName, boolean b);

	Object findByTaskName(String taskName);

	Optional<Task> findByTaskIdAndIsActive(Integer taskId, boolean b);
     
    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.taskQuestion = :newTaskQuestionList WHERE :taskQuestion NOT MEMBER OF t.taskQuestion AND t.taskId = :taskId")
    void deleteTaskQuestionByTaskId(@Param("taskId") int taskId, @Param("taskQuestion") TaskQuestion taskQuestion, @Param("newTaskQuestionList") List<TaskQuestion> newTaskQuestionList);


}
