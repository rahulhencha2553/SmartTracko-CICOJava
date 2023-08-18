package com.cico.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cico.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {


	@Transactional
	@Modifying
	@Query("Update Course c Set isDeleted=1 Where c.courseId=:courseId")
	public int deleteCourse(@Param("courseId") Integer courseId);

	public Page<Course> findAllByIsDeleted(boolean b,PageRequest p);
	
//	@Query("SELECT c From Course c JOIN FETCH c.batches b Where c.courseId=:courseId AND b.isDeleted = 0")
//	public Optional<Course> findById(@Param("courseId") Integer courseId);
}