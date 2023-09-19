package com.cico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cico.model.AssignmentSubmission;
import com.cico.util.SubmissionStatus;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long>{
	@Query("SELECT a FROM AssignmentSubmission a WHERE a.student.studentId = :studentId")
	List<AssignmentSubmission> getSubmitAssignmentByStudentId(@Param("studentId") Integer studentId);
	
	@Query("SELECT a FROM AssignmentSubmission a WHERE a.assignmentId= :assignmentId  AND a.taskId =:taskId")
	List<AssignmentSubmission> getSubmitAssignmentByAssignmentId(@Param("assignmentId") Long assignmentId,@Param("taskId")Long taskId);

	@Transactional
    @Modifying
	@Query("UPDATE AssignmentSubmission a SET a.status=:status , a.review=:review WHERE a.submissionId=:submissionId")
	void updateSubmitAssignmentStatus(@Param("submissionId") Long submissionId,@Param("status") SubmissionStatus status,@Param("review") String review);
    
	@Query("SELECT  a FROM AssignmentSubmission a  WHERE a.assignmentId = :id")
	List<AssignmentSubmission> findByAssignmentId(@Param("id") Long id);
	
	@Query("SELECT a FROM AssignmentSubmission a WHERE a.assignmentId = :assignmentId AND a.taskId = :taskId")
	AssignmentSubmission findByAssignmentIdAndQuestionId(@Param("assignmentId") Long assignmentId, @Param("taskId") Long taskId);


}
