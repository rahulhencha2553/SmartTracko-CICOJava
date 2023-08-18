package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cico.model.Chapter;
import com.cico.model.Subject;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	Optional<Chapter> findByChapterIdAndIsDeleted(Integer chapterId, boolean b);

	Chapter findByChapterNameAndIsDeleted(String chapterName, boolean b);
    
	@Query("SELECT c FROM Chapter c WHERE c.subject =:subject AND c.isDeleted = :b")
    List<Chapter> findAllSubjectIdAndIsDeleted(@Param("subject")Subject subject, @Param("b") Boolean b);
	
	List<Chapter>findAllBySubject(Subject subject);
	
	@Query("SELECT c FROM Chapter c WHERE c.subject =:subject AND c.chapterId = :chapterId AND c.isDeleted = :b")
	Optional<Chapter> findByChapterIdAndsubjectIdAndIsDeleted(@Param("chapterId")Integer chapterId,@Param("subject")Subject subject, @Param("b") Boolean b);

}
