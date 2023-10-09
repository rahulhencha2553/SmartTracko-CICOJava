package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cico.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

	@Query("SELECT s FROM Subject s LEFT JOIN FETCH s.chapters c WHERE s.subjectName = :subjectName AND s.isDeleted = :isDeleted AND (c.isDeleted IS NULL OR c.isDeleted = :isDeleted)")
	Subject findBySubjectNameAndIsDeleted(@Param("subjectName") String subjectName,@Param("isDeleted") Boolean isDeleted);

	@Query("SELECT  s FROM Subject s LEFT JOIN  s.chapters  c WHERE s.subjectId =:subjectId AND (c.isDeleted IS NULL OR c.isDeleted = :isDeleted) ")
	Optional<Subject> findBySubjectIdAndIsDeleted(@Param("subjectId") Integer subjectId,@Param("isDeleted") Boolean isDeleted);

	@Query("SELECT  s FROM Subject s LEFT JOIN s.chapters as c WHERE  s.isDeleted = :isDeleted AND (c.isDeleted IS NULL OR c.isDeleted = :isDeleted)  ")
	List<Subject> findByIsDeleted(@Param("isDeleted") Boolean isDeleted);

}
