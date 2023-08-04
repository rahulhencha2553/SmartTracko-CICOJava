package com.cico.service.impl;

import java.util.List;
import java.util.Objects;

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
import com.cico.service.IExamService;
import com.cico.service.IFileService;

@Service
public class ExamServiceImpl implements IExamService {

	@Autowired
	private ExamRepo examRepo;

	@Autowired
	ChapterRepository chapterRepo;

	@Autowired
	private QuestionRepo questionRepo;

	@Autowired
	private IFileService fileService;

	@Value("${fileUploadPath}")
	private String IMG_UPLOAD_DIR;

//	@Override
//	public void addExam(String examName) {
//		Exam exam = examRepo.findByExamNameAndIsDeleted(examName, false);
//
//		if (Objects.nonNull(exam))
//			throw new ResourceAlreadyExistException("Exam already exist");
//
//		exam = new Exam();
//		exam.setExamName(examName);
//		examRepo.save(exam);
//
//	}

	@Override
	public void addQuestionsToExam(Integer examId, String question, List<String> options, MultipartFile image) {
		Exam exam = examRepo.findByExamIdAndIsDeleted(examId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

		Question questionObj = questionRepo.findByQuestionAndIsDeleted(question, false);
		if (Objects.nonNull(questionObj))
			throw new ResourceAlreadyExistException("Question already exist");

		questionObj = new Question(question, options);

		if (image != null) {
			questionObj.setQuestionImage(image.getOriginalFilename());
			fileService.uploadFileInFolder(image, IMG_UPLOAD_DIR);
		}

		List<Question> questions2 = exam.getQuestions();

		questions2.add(questionObj);

		exam.setQuestions(questions2);

		examRepo.save(exam);

	}

	@Override
	public void updateExam(Exam exam) {
		examRepo.findByExamIdAndIsDeleted(exam.getExamId(), false)
				.orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
		examRepo.save(exam);

	}

	@Override
	public Exam getExamById(Integer examId) {
		return examRepo.findByExamIdAndIsDeleted(examId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Exam not found"));
	}

	@Override
	public void deleteExam(Integer examId) {
		Exam exam = examRepo.findByExamIdAndIsDeleted(examId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

		exam.setIsDeleted(true);
		examRepo.save(exam);

	}

	@Override
	public void updateExamStatus(Integer examId) {
		Exam exam = examRepo.findByExamIdAndIsDeleted(examId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

		if (exam.getIsActive().equals(true))
			exam.setIsActive(false);

		else
			exam.setIsActive(true);

		examRepo.save(exam);

	}

	@Override
	public List<Exam> getAllExams() {
		List<Exam> exams = examRepo.findByIsDeleted(false);
		if (exams.isEmpty())
			new ResourceNotFoundException("No exam available");

		return exams;
	}

	@Override
	public List<Exam> getExamsByChapter(Integer chapterId) {
		Chapter chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

		if (chapter.getExams().isEmpty())
			throw new ResourceNotFoundException("No exam available for Chapter : " + chapter.getChapterName());

		return chapter.getExams();

	}

}
