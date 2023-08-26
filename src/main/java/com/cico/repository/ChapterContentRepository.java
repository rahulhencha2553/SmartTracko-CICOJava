package com.cico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cico.model.Chapter;
import com.cico.model.ChapterContent;

@Repository
public interface ChapterContentRepository extends JpaRepository<ChapterContent, Integer> {

	@Query("SELECT c FROM ChapterContent c WHERE c.isDeleted = 0 AND c.chapterId = :chapterId AND c.id = :contentId")
	ChapterContent findByChapterIdAndId(@Param("chapterId") Integer chapterId, @Param("contentId") Integer contentId);


	@Transactional
	@Modifying
	@Query("UPDATE ChapterContent c SET c.isDeleted = 1 WHERE c.chapterId =:chapterId AND c.id =:contentId")
	void updateContent(@Param("chapterId") Integer chapterId, @Param("contentId") Integer contentId);
   
	
}
