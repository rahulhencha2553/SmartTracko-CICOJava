package com.cico.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.model.Course;
import com.cico.payload.ApiResponse;
import com.cico.payload.PageResponse;
import com.cico.service.ICourseService;
import com.cico.util.AppConstants;

@RestController
@RequestMapping("/course")
@CrossOrigin("*")
public class CourseController {

	@Autowired
	private ICourseService courseService;
	
	@PostMapping("/addCourseApi")
	public ResponseEntity<Course> createCourse(@RequestParam("technologyStackId") Integer technologyStackId,@RequestParam("courseName") String courseName, @RequestParam("courseFees") String courseFees,@RequestParam("duration") String duration,@RequestParam("sortDescription") String sortDescription)
	{
		Course course = courseService.createCourse(technologyStackId,courseName,courseFees,duration,sortDescription);
		return ResponseEntity.status(HttpStatus.CREATED).body(course);
	}
	
	@GetMapping("/findCourseByIdApi")
	public ResponseEntity<Course> getCourseById(@RequestParam("courseId") Integer courseId)
	{
		Course course = courseService.findCourseById(courseId);
		return ResponseEntity.ok(course);
	}
	
	@GetMapping("/findAllCourseApi")
	public ResponseEntity<PageResponse<Course>> getAllCourses(@RequestParam(name="page",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER)Integer page, @RequestParam(name="size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE)Integer size)
	{
		 PageResponse<Course> allCourses = courseService.getAllCourses(page, size);
		return ResponseEntity.ok(allCourses);
	}
	
	@PutMapping("/updateCourseApi")
	public ResponseEntity<Course> updateCourse(@RequestParam("courseId") Integer courseId,@RequestParam("technologyStackId") Integer technologyStackId,@RequestParam("courseName") String courseName, @RequestParam("courseFees") String courseFees,@RequestParam("duration") String duration,@RequestParam("sortDescription") String sortDescription)
	{
		Course course = courseService.updateCourse(courseId,technologyStackId,courseName,courseFees,duration,sortDescription);
		return ResponseEntity.status(HttpStatus.CREATED).body(course);
	}
	
	@PutMapping("/deleteCourseByIdApi")
	public ResponseEntity<ApiResponse> deleteCourseById(@RequestParam("courseId") Integer courseId)
	{
		Boolean deleteCourseById = courseService.deleteCourseById(courseId);
		if(deleteCourseById!=null)
		return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, AppConstants.DELETE_SUCCESS,HttpStatus.OK));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(Boolean.FALSE, "Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR));
	}

}
