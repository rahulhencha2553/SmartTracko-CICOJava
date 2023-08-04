package com.cico.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Chapter;
import com.cico.model.Subject;
import com.cico.repository.ChapterRepository;
import com.cico.repository.SubjectRepository;
import com.cico.service.ISubjectService;


@Service
public class SubjectServiceImpl implements ISubjectService { 

	@Autowired
	SubjectRepository subRepo;

	@Autowired
	ChapterRepository chapterRepo;

	@Override
	public void addSubject(String subjectName) {
		Subject subject = subRepo.findBySubjectNameAndIsDeleted(subjectName, false);
		if (Objects.nonNull(subject))
			throw new ResourceAlreadyExistException("Subject already exist");

		subject = new Subject();
		subject.setSubjectName(subjectName);

		subRepo.save(subject);

	}

	@Override
	public void addChapterToSubject(Integer subjectId, String chapterName, String content) {
		Subject subject = subRepo.findBySubjectIdAndIsDeleted(subjectId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

		List<Chapter> chapters = subject.getChapters();

		for (Chapter chapter : chapters) {
			if (chapter.getChapterName().equals(chapterName))
				throw new ResourceAlreadyExistException(
						"Chapter: " + chapterName + " already exist in the Subject " + subject.getSubjectName());
		}

		Chapter chapter = new Chapter(chapterName, content);
		chapters.add(chapter);
		subject.setChapters(chapters);
		subRepo.save(subject);
	}

	@Override
	public void updateSubject(Subject subject) {
		subRepo.findBySubjectIdAndIsDeleted(subject.getSubjectId(), false)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
		subRepo.save(subject);

	}

	@Override
	public Map<String, Object> getSubjectById(Integer subjectId) {
		Subject subject = subRepo.findBySubjectIdAndIsDeleted(subjectId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
		int size = subject.getChapters().size();
		List<Chapter> chapters = subject.getChapters();

		long completedCount = chapters.stream().filter(Chapter::getIsCompleted).count();
		Map<String, Object> map = new HashMap();
		map.put("Subject", subject);
		map.put("Chapter Count", size);
		map.put("Completed Chapter Count", completedCount);

		return map;

	}

	@Override
	public void deleteSubject(Integer subjectId) {
		Subject subject = subRepo.findBySubjectIdAndIsDeleted(subjectId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

		subject.setIsDeleted(true);
		subRepo.save(subject);
	}

	@Override
	public void updateSubjectStatus(Integer subjectId) {
		Subject subject = subRepo.findBySubjectIdAndIsDeleted(subjectId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

		if (subject.getIsActive().equals(true))
			subject.setIsActive(false);

		else
			subject.setIsActive(true);

		subRepo.save(subject);
	}

	@Override
	public List<Subject> getAllSubjects() {
		List<Subject> subjects = subRepo.findByIsDeleted(false);
		if (subjects.isEmpty())
			new ResourceNotFoundException("No subject available");

		return subjects;
	}

}
