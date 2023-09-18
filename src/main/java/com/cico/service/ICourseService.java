package com.cico.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cico.model.Course;
import com.cico.payload.ApiResponse;
import com.cico.payload.CourseRequest;
import com.cico.payload.PageResponse;


public interface ICourseService {

	public ResponseEntity<?> createCourse(CourseRequest request);

	public Course findCourseById(Integer courseId);

	public PageResponse<Course> getAllCourses(Integer page, Integer size);

	public ApiResponse updateCourse(Course course);

	public Boolean deleteCourseById(Integer courseId);

	public ResponseEntity<?> findAllCourses();

	public ResponseEntity<?> getAllCourseApi();

	public ApiResponse studentUpgradeCourse(Integer studnetId, Integer courseId);

}
