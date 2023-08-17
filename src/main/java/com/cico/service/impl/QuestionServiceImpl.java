package com.cico.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Chapter;
import com.cico.model.Exam;
import com.cico.model.Question;
import com.cico.repository.ChapterRepository;
import com.cico.repository.ExamRepo;
import com.cico.repository.QuestionRepo;
import com.cico.service.IFileService;
import com.cico.service.IQuestionService;

@Service
public class QuestionServiceImpl implements IQuestionService {

	@Autowired
	private QuestionRepo questionRepo;

	@Autowired
	private IFileService fileService;

	@Autowired
	private ExamRepo examRepo;
	@Autowired
	private ChapterRepository chapterRepository;

	@Value("${fileUploadPath}")
	private String IMG_UPLOAD_DIR;

	@Override
	public void addQuestion(Integer chapterId,String questionContent, List<String> option, MultipartFile image, String correctOption) {
		Question questionObj = questionRepo.findByQuestionContentAndIsDeleted(questionContent, false);
		if (Objects.nonNull(questionObj))
			throw new ResourceAlreadyExistException("Question already exist");
				
		questionObj = new Question();
		questionObj.setQuestionContent(questionContent);
		questionObj.setOptions(option);
		questionObj.setCorrectOption(correctOption);
		questionObj.setQuestionImage(image.getOriginalFilename());
		Chapter chapter = chapterRepository.findById(chapterId).get();
		questionObj.setChapter(chapter);
		String file = fileService.uploadFileInFolder(image, IMG_UPLOAD_DIR);
		questionObj.setQuestionImage(file);
		questionRepo.save(questionObj);
	}

	@Override
	public void updateQuestion(Question question) {
		questionRepo.findByQuestionIdAndIsDeleted(question.getQuestionId(), false)
				.orElseThrow(() -> new ResourceNotFoundException("Question not found"));
		questionRepo.save(question);

	}

	@Override
	public List<Question> getQuestionByChapterId(Integer chapterId) {
		 Chapter chapter = chapterRepository.findById(chapterId).get();
		  List<Question> questions = questionRepo.findAllByChapterAndIsDeleted(chapter, false);
		  return questions;
			
	}

	@Override
	public void deleteQuestion(Integer questionId) {
		Question question = questionRepo.findByQuestionIdAndIsDeleted(questionId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Question not found"));

		question.setIsDeleted(true);
		questionRepo.save(question);
	}

	@Override
	public void updateQuestionStatus(Integer questionId) {
		Question question = questionRepo.findByQuestionIdAndIsDeleted(questionId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Question not found"));

		if (question.getIsActive().equals(true))
			question.setIsActive(false);

		else
			question.setIsActive(true);

		questionRepo.save(question);

	}

	@Override
	public List<Question> getAllQuestions() {
		List<Question> questions = questionRepo.findByIsDeleted(false);
		if (questions.isEmpty())
			new ResourceNotFoundException("No question available");

		return questions;
	}

	@Override
	public List<Question> getQuestionsByExam(Integer examId) {
		Exam exam = examRepo.findByExamIdAndIsDeleted(examId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

		if (exam.getQuestions().isEmpty())
			throw new ResourceNotFoundException("No question available for Exam : " + exam.getExamName());

		return exam.getQuestions();
	}

}
