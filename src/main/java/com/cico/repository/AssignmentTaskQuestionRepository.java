package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cico.model.AssignmentTaskQuestion;

@Repository
public interface AssignmentTaskQuestionRepository extends JpaRepository<AssignmentTaskQuestion, Integer> {

	Optional<AssignmentTaskQuestion> findByQuestionId(Long questionId);

	@Modifying
    @Transactional
	@Query("UPDATE AssignmentTaskQuestion a set a.isActive = 0 WHERE a.questionId =:questionId AND a.assignmentId =:assignmentId")
	void deleteQuestionByIdAndId(Long questionId, Long assignmentId);

	 List<AssignmentTaskQuestion> findByAssignmentIdAndIsActiveTrue(Long id);
}
