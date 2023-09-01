package com.cico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cico.model.TaskSubmission;

@Repository
public interface TaskSubmissionRepository extends JpaRepository<TaskSubmission, Integer> {

	  @Query("SELECT a FROM TaskSubmission a WHERE a.taskId =:taskId")
	  List<TaskSubmission> getSubmitAssignmentByAssignmentId(@Param("taskId")Long taskId);

}
