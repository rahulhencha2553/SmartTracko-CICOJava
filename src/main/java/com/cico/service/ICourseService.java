package com.cico.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cico.model.Course;
import com.cico.payload.CourseRequest;
import com.cico.payload.PageResponse;


public interface ICourseService {

	public ResponseEntity<?> createCourse(CourseRequest request);

	public Course findCourseById(Integer courseId);

	public PageResponse<Course> getAllCourses(Integer page, Integer size);

	public Course updateCourse(Integer courseId, Integer technologyStackId, String courseName, String courseFees,
			String duration, String sortDescription);

	

	public Boolean deleteCourseById(Integer courseId);

	public ResponseEntity<?> findAllCourses();

}
