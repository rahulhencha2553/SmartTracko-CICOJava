package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cico.model.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	Optional<Chapter> findByChapterIdAndIsDeleted(Integer chapterId, boolean b);

	Chapter findByChapterNameAndIsDeleted(String chapterName, boolean b);

	List<Chapter> findByIsDeleted(Boolean b);

}
