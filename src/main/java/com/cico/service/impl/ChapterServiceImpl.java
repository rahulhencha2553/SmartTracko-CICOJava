package com.cico.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Chapter;
import com.cico.model.ChapterContent;
import com.cico.model.Question;
import com.cico.model.Subject;
import com.cico.repository.ChapterContentRepository;
import com.cico.repository.ChapterRepository;
import com.cico.repository.ExamRepo;
import com.cico.repository.QuestionRepo;
import com.cico.repository.SubjectRepository;
import com.cico.service.IChapterService;

@Service
public class ChapterServiceImpl implements IChapterService {

	@Autowired
	ChapterRepository chapterRepo;

	@Autowired
	private QuestionRepo questionRepo;

	@Autowired
	ExamRepo examRepo;

	@Autowired
	SubjectRepository subjectRepo;
	@Autowired
	ChapterContentRepository chapterContentRepository;

	@Autowired
	FileServiceImpl fileServiceImpl;

	@Override
	public ResponseEntity<?> addChapter(Integer subjectId, String chapterName, MultipartFile image) throws Exception {
		Chapter obj = chapterRepo.findByChapterNameAndIsDeleted(chapterName, false);
		if (Objects.nonNull(obj)) {
			throw new Exception("Chapter already present with name..");
		}
		Subject subject = subjectRepo.findById(subjectId).get();

		Chapter chapter = new Chapter();
		chapter.setChapterName(chapterName.trim());
		chapter.setIsCompleted(false);
		Chapter obj1 = chapterRepo.save(chapter);
		subject.getChapters().add(obj1);
		subjectRepo.save(subject);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateChapter(Integer chapterId, String chapterName) throws Exception {
		Chapter chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

		if (chapter.getChapterName().equals(chapterName.trim())) {
			throw new Exception("Chapter already present with name..");
		}

		chapter.setChapterName(chapterName.trim());
		return new ResponseEntity<>(chapterRepo.save(chapter), HttpStatus.OK);
	}

	@Override
	public Map<String, Object> getChapterById(Integer chapterId) {
		Map<String, Object> response = new HashMap<>();
		Chapter chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
		chapter.setChapterContent(chapter.getChapterContent().stream().filter(obj->obj.getIsDeleted()==false).collect(Collectors.toList()));
		response.put("chapter", chapter);
		response.put("questionLength",questionRepo.findAllByChapterAndIsDeleted(chapter, false).size());
		return response;
	}

	@Override
	public ResponseEntity<?> deleteChapter(Integer chapterId) {
		Chapter chapter = chapterRepo.findByChapterIdAndIsDeleted(chapterId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
		chapter.setIsDeleted(true);
		chapterRepo.save(chapter);
		return new ResponseEntity<>(HttpStatus.OK);
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
		List<Chapter> chapters = subject.getChapters().stream().filter(obj -> obj.getIsDeleted() == false)
				.collect(Collectors.toList());
		if (chapters.isEmpty())
			new ResourceNotFoundException("No chapter available");
		return chapters;
	}

	@Override
	public List<Chapter> getChaptersBySubject(Integer subjectId) {
		Subject subject = subjectRepo.findBySubjectIdAndIsDeleted(subjectId, false)
				.orElseThrow(() -> new ResourceNotFoundException("Subject Not Found"));
		List<Chapter> chapters = subject.getChapters().stream().filter(obj -> obj.getIsDeleted() == false)
				.collect(Collectors.toList());
		if (chapters.isEmpty())
			throw new ResourceNotFoundException("No Chapter available for the Subject: " + subject.getSubjectName());
		return subject.getChapters();
	}

	@Override
	public Chapter addContentToChapter(Integer chapterId, String title, String subTitle, String content) {
		Chapter chapter = chapterRepo.findById(chapterId)
				.orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
		List<ChapterContent> chapters = chapter.getChapterContent();
		ChapterContent chapterContent = new ChapterContent();
		chapterContent.setContent(content);
		chapterContent.setSubTitle(subTitle);
		chapterContent.setTitle(title);
		chapters.add(chapterContent);
		return chapterRepo.save(chapter);
	}

	@Override
	public ChapterContent updateChapterContent(String title, String subTitle, String content,
			Integer contentId) {
		 Optional<ChapterContent> obj = this.chapterContentRepository.findById(contentId);
		 ChapterContent chapter = obj.get();
		if (title != null)
			chapter.setTitle(title);
		else
			chapter.setTitle(chapter.getTitle());
		if (subTitle != null)
			chapter.setSubTitle(subTitle);
		else
			chapter.setSubTitle(chapter.getSubTitle());
		if (content != null)
			chapter.setContent(content);
		else
			chapter.setContent(chapter.getContent());

		return this.chapterContentRepository.save(chapter);
	}

	@Override
	public ChapterContent getChapterContent(Integer chapterContentId) throws Exception {
		 Optional<ChapterContent> obj = this.chapterContentRepository.findById(chapterContentId);
		if (obj.isPresent()) {
			throw new Exception("Chapter content not found");
		}
		return obj.get();
	}

	@Override
	public void deleteChapterContent(Integer contentId) {
		this.chapterContentRepository.updateContent(contentId);
	}

}
