package com.cico.repository;

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
}