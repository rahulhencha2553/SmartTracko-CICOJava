package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cico.model.Question;

public interface QuestionRepo extends JpaRepository<Question, Integer> {

	Question findByQuestionAndIsDeleted(String question, boolean b);

	Optional<Question> findByQuestionIdAndIsDeleted(Integer questionId, boolean b);

	List<Question> findByIsDeleted(boolean b);

}
