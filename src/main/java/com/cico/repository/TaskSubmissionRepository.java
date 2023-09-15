package com.cico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cico.model.TaskSubmission;
import com.cico.util.SubmissionStatus;

@Repository
public interface TaskSubmissionRepository extends JpaRepository<TaskSubmission, Integer> {

	@Query("SELECT a FROM TaskSubmission a WHERE a.taskId =:taskId")
	List<TaskSubmission> getSubmittedTaskByTaskId(@Param("taskId") Long taskId);

	@Query("SELECT s FROM TaskSubmission s WHERE s.student.studentId=:studentId")
	List<TaskSubmission> getSubmitedTaskForStudent(@Param("studentId") Integer studentId);

	@Transactional
	@Modifying
	@Query("UPDATE TaskSubmission a SET a.status=:status , a.review=:review WHERE a.id=:id")
	void updateSubmitTaskStatus(@Param("id") Integer submissionId, @Param("status") SubmissionStatus status,
			@Param("review") String review);

	@Query("SELECT s FROM TaskSubmission s WHERE s.id=:id")
	TaskSubmission findBySubmissionId(@Param("id") Integer submissionId);
}
