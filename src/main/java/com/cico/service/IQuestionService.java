package com.cico.service;

import java.util.List;

import com.cico.model.Question;

public interface IQuestionService {

	//void addQuestion(String question, List<String> options, MultipartFile image);

	void updateQuestion(Question question);

	Question getQuestionById(Integer questionId);

	void deleteQuestion(Integer questionId);

	void updateQuestionStatus(Integer questionId);

	List<Question> getAllQuestions();

	List<Question> getQuestionsByExam(Integer examId);

}
