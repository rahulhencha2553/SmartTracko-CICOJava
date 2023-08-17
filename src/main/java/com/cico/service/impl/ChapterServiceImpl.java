package com.cico.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Chapter;
import com.cico.model.ChapterContent;
import com.cico.model.Exam;
import com.cico.model.Subject;
import com.cico.repository.ChapterRepository;
import com.cico.repository.ExamRepo;
import com.cico.repository.SubjectRepository;
import com.cico.service.IChapterService;

@Service
public class ChapterServiceImpl implements IChapterService {

	@Autowired
	ChapterRepository chapterRepo;

	@Autowired
	ExamRepo examRepo;

	@Autowired
	SubjectRepository subjectRepo;

	@Override
	public void addChapter(Integer subjectId, String chapterName) {
		Chapter chapter = chapterRepo.findByChapterNameAndIsDeleted(chapterName, false);
		if (Objects.nonNull(chapter))
			throw new ResourceAlreadyExistException("Chapter already exist");
		chapter = new Chapter();
		chapter.setChapterName(chapterName);
		chapter.setSubject(subjectRepo.findById(subjectId).get());
		chapter.setIsCompleted(false);
		Chapter obj = chapterRepo.save(chapter);
		Subject subject = subjectRepo.findById(subjectId)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
		subject.setChapters(Arrays.asList(obj));
		subjectRepo.save(subject);
	}

	@Override
	public void addExamToChapter(Integer chapterId, String examName) {
//		Chapter chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false)
//				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
//		List<Exam> exams = chapter.getExams();
//		exams.add(new Exam(examName));
//		chapter.setExams(exams);
//		chapterRepo.save(chapter);
	}

	@Override
	public void updateChapter(Chapter chapter) {
		chapterRepo.findByChapterIdAndIsDeleted(chapter.getChapterId(), false)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
		chapterRepo.save(chapter);

	}

	@Override
	public Chapter getChapterById(Integer chapterId) {
		return chapterRepo.findByChapterIdAndIsDeleted(chapterId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
	}

	@Override
	public void deleteChapter(Integer chapterId) {
		Chapter chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

		chapter.setIsDeleted(true);
		chapterRepo.save(chapter);

	}

	@Override
	public void updateChapterStatus(Integer chapterId) {
		Chapter chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

		if (chapter.getIsActive().equals(true))
			chapter.setIsActive(false);

		else
			chapter.setIsActive(true);

		chapterRepo.save(chapter);

	}

	@Override
	public List<Chapter> getAllChapters(Integer subjectId) {
		Subject subject = subjectRepo.findById(subjectId).get();
		List<Chapter> chapters = chapterRepo.findAllSubjectIdAndIsDeleted(subject, false);
		if (chapters.isEmpty())
			new ResourceNotFoundException("No chapter available");
		return chapters;
	//	return null;
	}

	@Override
	public List<Chapter> getChaptersBySubject(Integer subjectId) {
		Subject subject = subjectRepo.findBySubjectIdAndIsDeleted(subjectId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Subject Not Found"));

		if (subject.getChapters().isEmpty())
			throw new ResourceNotFoundException("No Chapter available for the Subject: " + subject.getSubjectName());

		return subject.getChapters();
	}

	@Override
	public void addContentToChapter(Integer chapterId, String title, String subTitle, String content) {
		Chapter chapter = chapterRepo.findById(chapterId)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
		List<ChapterContent> chapters = chapter.getChapterContent();
		ChapterContent chapterContent = new ChapterContent();
		chapterContent.setChapterId(chapterId);
		chapterContent.setContent(content);
		chapterContent.setSubTitle(subTitle);
		chapterContent.setTitle(title);
		chapters.add(chapterContent);
		chapterRepo.save(chapter);
	}

}
