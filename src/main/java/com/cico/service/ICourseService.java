package com.cico.service;

import java.util.List;

import com.cico.model.Course;
import com.cico.payload.PageResponse;


public interface ICourseService {

	public Course createCourse(Integer technologyStackId,String courseName,String courseFees, String duration, String sortDescription);

	public Course findCourseById(Integer courseId);

	public PageResponse<Course> getAllCourses(Integer page, Integer size);

	public Course updateCourse(Integer courseId, Integer technologyStackId, String courseName, String courseFees,
			String duration, String sortDescription);

	

	public Boolean deleteCourseById(Integer courseId);

}
