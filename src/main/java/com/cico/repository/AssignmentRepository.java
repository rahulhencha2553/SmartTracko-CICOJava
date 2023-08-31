package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cico.model.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

	Optional<Assignment> findByIdAndIsActive(Long id, boolean b);

	List<Assignment> findByIsActiveTrue();

	void deleteQuestionByIdAndId(Long questionId, Long assignmentId);

}
