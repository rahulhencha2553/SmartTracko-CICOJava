package com.cico.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Course;
import com.cico.model.Subject;
import com.cico.payload.CourseRequest;
import com.cico.payload.PageResponse;
import com.cico.repository.CourseRepository;
import com.cico.repository.SubjectRepository;
import com.cico.repository.TechnologyStackRepository;
import com.cico.service.ICourseService;
import com.cico.util.AppConstants;

@Service
public class CourseServiceImpl implements ICourseService {

	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private TechnologyStackRepository repository;
	@Autowired
	private SubjectRepository subjectRepository;

	@Override
	public ResponseEntity<?> createCourse(CourseRequest request) {
		Map<String, Object> response = new HashMap<>();
		Course course = new Course(request.getCourseName(), request.getCourseFees(), request.getDuration(), request.getSortDescription(), null);
		List<Subject> subjects = course.getSubjects();
		for (Integer id : request.getSubjectIds()) {
			subjects.add( subjectRepository.findBySubjectIdAndIsDeleted(id, false).get());
		}
		course.setSubjects(subjects);
		course.setCreatedDate(LocalDate.now());
		course.setUpdatedDate(LocalDate.now());
		course.setTechnologyStack(repository.findById(request.getTechnologyStack()).get());
		Course savedCourse = courseRepository.save(course);
		if(Objects.nonNull(savedCourse)){
			response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
			response.put("course", savedCourse);
			return new ResponseEntity<>(response,HttpStatus.CREATED);
		}
		response.put(AppConstants.MESSAGE,AppConstants.FAILED);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	@Override
	public Course findCourseById(Integer courseId) {
		Optional<Course> findById = courseRepository.findById(courseId);
		if (!findById.isPresent()) {
			throw new ResourceNotFoundException("Course is not found from given Id");
		}

		return findById.get();
	}

	@Override
	public PageResponse<Course> getAllCourses(Integer page, Integer size) {
		PageRequest p = PageRequest.of(page, size, Sort.by(Direction.DESC, "courseId"));
		 Page<Course> coursePageList = courseRepository.findAllByIsDeleted(false,p);
		return new PageResponse<>(coursePageList.getContent(), coursePageList.getNumber(), coursePageList.getSize(), coursePageList.getNumberOfElements(), coursePageList.getTotalPages(), coursePageList.isLast());
	}

	@Override
	public Course updateCourse(Integer courseId, Integer technologyStackId, String courseName, String courseFees,
			String duration, String sortDescription) {
		Course course=new Course();
		Optional<Course> findById = courseRepository.findById(courseId);
		course=findById.get();
		if(technologyStackId!=null) {
			course.setTechnologyStack(repository.findById(technologyStackId).get());
		}else {
			course.setTechnologyStack(course.getTechnologyStack());
		}
		if(!courseName.isEmpty()) {
			course.setCourseName(courseName);	
		}else {
			course.setCourseName(course.getCourseName());
		}
		if(!courseFees.isEmpty()) {
			course.setCourseFees(courseFees);	
		}else {
			course.setCourseFees(course.getCourseFees());
		}
		if(!duration.isEmpty()) {
			course.setDuration(duration);	
		}else {
			course.setDuration(course.getDuration());
		}
		if(!sortDescription.isEmpty()) {
			course.setSortDescription(sortDescription);	
		}else {
			course.setSortDescription(course.getSortDescription());
		}
		course.setCreatedDate(course.getCreatedDate());
		course.setUpdatedDate(LocalDate.now());
		return courseRepository.save(course);
	}

	@Override
	public Boolean deleteCourseById(Integer courseId) {
		int deleteCourse = courseRepository.deleteCourse(courseId);
		if(deleteCourse!=0)
		return true;
		return false;
	}

	@Override
	public ResponseEntity<?> findAllCourses() {
		// TODO Auto-generated method stub
		List<Course> findAll = courseRepository.findAll();
		return  new ResponseEntity<>(findAll, HttpStatus.OK);
	}

}
