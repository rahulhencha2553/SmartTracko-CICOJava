package com.cico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cico.model.TaskQuestion;

public interface TaskQuestionRepository extends JpaRepository<TaskQuestion, Long>{

	Optional<TaskQuestion> findByQuestionId(Long questionId);

}
