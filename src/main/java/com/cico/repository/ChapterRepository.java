package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.cico.model.Chapter;
import com.cico.model.Subject;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

//	@Query("SELECT c From Course c JOIN FETCH c.batches b Where c.courseId=:courseId AND b.isDeleted = 0")
//	public Optional<Course> findById(@Param("courseId") Integer courseId);

	@Query("SELECT ch FROM Chapter ch JOIN FETCH ch.chapterContent co WHERE ch.chapterId =:chapterId AND ch.isDeleted = 0 AND co.isDeleted =:b")
	 public Optional<Chapter> findByChapterIdAndIsDeleted(@Param("chapterId") Integer chapterId, boolean b);


	Chapter findByChapterNameAndIsDeleted(String chapterName, boolean b);

	@Query("SELECT c FROM Chapter c WHERE c.subject =:subject AND c.isDeleted = :b")
	List<Chapter> findAllSubjectIdAndIsDeleted(@Param("subject") Subject subject, @Param("b") Boolean b);

	@Query("SELECT c FROM Chapter c WHERE c.subject =:subject AND c.isDeleted = false")
	List<Chapter> findAllBySubject(Subject subject);

	@Query("SELECT c FROM Chapter c WHERE c.subject =:subject AND c.chapterId = :chapterId AND c.isDeleted = :b")
	Optional<Chapter> findByChapterIdAndsubjectIdAndIsDeleted(@Param("chapterId") Integer chapterId,
			@Param("subject") Subject subject, @Param("b") Boolean b);
  //@Query("SELECT s  FROM  Chapter s  WHERE s.chapterId =:chapterId AND s.subject =:subject AND s.isDeleted =:b")
  //public Optional<Chapter> findByChapterIdAndSubjectAndIsDeleted(@RequestParam("chapterId")Integer chapterId,@RequestParam("subject")Subject Subject,@RequestParam("b")boolean b);


	public Optional<Chapter> findByChapterIdAndSubjectAndIsDeleted(Integer chapterId, Subject subject, boolean b);
}
