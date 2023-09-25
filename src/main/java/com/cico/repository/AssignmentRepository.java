package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cico.model.Assignment;
import com.cico.model.Course;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
	
	Optional<Assignment> findByIdAndIsActive(Long id, boolean b);

	List<Assignment> findByIsActiveTrue();
   
	Course findByCourse(Optional<Course> findByCourseId);

	@Query("SELECT a FROM Assignment a WHERE a.course IN (SELECT c FROM Course c WHERE c.courseId = :courseId)")
	List<Assignment> findAllByCourseId(@Param("courseId") Integer courseId);
    
	@Query("SELECT a FROM Assignment a WHERE a.course.courseId = :courseId  AND a.subject.subjectId =:subjectId")
	List<Assignment> findAllByCourseIdAndSubjectId(@Param("courseId") Integer courseId,@Param("subjectId") Integer subjectId);


}
