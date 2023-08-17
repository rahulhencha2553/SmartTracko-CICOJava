package com.cico.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Question;

public interface IQuestionService {

	void addQuestion(Integer chapterId,String questionContent, List<String> option, MultipartFile image, String correctOption);

	void updateQuestion(Question question);

	List<Question> getQuestionByChapterId(Integer questionId);

	void deleteQuestion(Integer questionId);

	void updateQuestionStatus(Integer questionId);

	List<Question> getAllQuestions();

	List<Question> getQuestionsByExam(Integer examId);

}
