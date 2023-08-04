package com.cico.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Exam;

public interface IExamService {

	//void addExam(String examName);

	void addQuestionsToExam(Integer examId, String question, List<String> options, MultipartFile image);

	void updateExam(Exam exam);

	Exam getExamById(Integer examId);

	void deleteExam(Integer examId);

	void updateExamStatus(Integer examId);

	List<Exam> getAllExams();

	List<Exam> getExamsByChapter(Integer chapterId);

}
