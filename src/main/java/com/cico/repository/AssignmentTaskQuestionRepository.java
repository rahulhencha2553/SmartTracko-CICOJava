package com.cico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cico.model.AssignmentTaskQuestion;

@Repository
public interface AssignmentTaskQuestionRepository extends JpaRepository<AssignmentTaskQuestion, Integer> {

	Optional<AssignmentTaskQuestion> findByQuestionId(Long questionId);
}
