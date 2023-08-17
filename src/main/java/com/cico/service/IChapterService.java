package com.cico.service;

import java.util.List;

import com.cico.model.Chapter;

public interface IChapterService {

	void addChapter(Integer subjectId,String chapterName);

	void addExamToChapter(Integer subjectId, String examName);

	void updateChapter(Chapter chapter);

	Chapter getChapterById(Integer chapterId);

	void deleteChapter(Integer chapterId);

	void updateChapterStatus(Integer chapterId);

	List<Chapter> getAllChapters(Integer subjectId);

	List<Chapter> getChaptersBySubject(Integer subjectId);

	void addContentToChapter(Integer chapterId, String title, String subTitle, String content);

}
