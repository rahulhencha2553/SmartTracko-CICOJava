package com.cico.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Chapter;
import com.cico.model.ChapterContent;
import com.cico.model.Subject;

public interface IChapterService {

	ResponseEntity<?> addChapter(Integer subjectId,String chapterName,MultipartFile image) throws Exception;

	void addExamToChapter(Integer subjectId, String examName);

	Chapter updateChapter(Integer chapterId,Integer subjectId,String chapterName);

	Map<String, Object> getChapterById(Integer chapterId);

	ResponseEntity<?> deleteChapter(Integer chapterId, Integer subjectId);

	void updateChapterStatus(Integer chapterId);

	List<Chapter> getAllChapters(Integer subjectId);

	List<Chapter> getChaptersBySubject(Integer subjectId);

	Chapter addContentToChapter(Integer chapterId, String title, String subTitle, String content);

	ChapterContent updateChapterContent(Integer chapterId, String title, String subTitle, String content,Integer contentId);

	ChapterContent getChapterContent(Integer chapterId, Integer chapterContentId);

	void deleteChapterContent(Integer chapterId, Integer contentId);

}
