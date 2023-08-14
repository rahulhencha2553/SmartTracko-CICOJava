package com.cico.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceAlreadyExistException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Chapter;
import com.cico.model.Subject;
import com.cico.repository.ChapterRepository;
import com.cico.repository.SubjectRepository;
import com.cico.repository.TechnologyStackRepository;
import com.cico.service.ISubjectService;
import com.cico.util.AppConstants;


@Service
public class SubjectServiceImpl implements ISubjectService { 

	@Autowired
	private SubjectRepository subRepo;

	@Autowired
	private ChapterRepository chapterRepo;
	
	@Autowired
	private TechnologyStackRepository technologyStackRepository;

	@Override
	public ResponseEntity<?> addSubject(String subjectName,Integer imageId) {
		Map<String, Object> response = new HashMap<>();
		Subject subject = subRepo.findBySubjectNameAndIsDeleted(subjectName, false);
		if (Objects.nonNull(subject))
			throw new ResourceAlreadyExistException("Subject already exist");

		subject = new Subject();
		subject.setSubjectName(subjectName);
		subject.setTechnologyStack(technologyStackRepository.findById(imageId).get());
		Subject save = subRepo.save(subject);
		if(Objects.nonNull(save)) {
			response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
			response.put("subject", save);
			return new ResponseEntity<>(response,HttpStatus.CREATED);
		}
		response.put(AppConstants.MESSAGE, AppConstants.FAILED);
		return new ResponseEntity<>(response,HttpStatus.OK);
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
