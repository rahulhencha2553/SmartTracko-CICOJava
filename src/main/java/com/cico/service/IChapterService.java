package com.cico.service;

import java.util.List;

import com.cico.model.Chapter;

public interface IChapterService {

	//void addChapter(String chapterName, String content);

	void addExamToChapter(Integer chapterId, String examName);

	void updateChapter(Chapter chapter);

	Chapter getChapterById(Integer chapterId);

	void deleteChapter(Integer chapterId);

	void updateChapterStatus(Integer chapterId);

	List<Chapter> getAllChapters();

	List<Chapter> getChaptersBySubject(Integer subjectId);

}
