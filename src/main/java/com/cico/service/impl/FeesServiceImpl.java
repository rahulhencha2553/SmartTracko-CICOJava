package com.cico.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cico.model.Fees;
import com.cico.repository.CourseRepository;
import com.cico.repository.FeesRepository;
import com.cico.repository.StudentRepository;
import com.cico.service.IFeesService;

@Service
public class FeesServiceImpl implements IFeesService {

	@Autowired
	private FeesRepository feesRepository;

	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private CourseRepository courseRepository;
	
	@Override
	public Fees createStudentFees(Integer studentId, Integer courseId, Double finalFees, String date) {
		// TODO Auto-generated method stub
		Fees fees=new Fees(null, null, finalFees, LocalDate.parse(date));
		fees.setStudent(studentRepository.findById(studentId).get());
		fees.setCourse(courseRepository.findById(courseId).get());
		fees.setCreatedDate(LocalDate.now());
		fees.setUpdatedDate(LocalDate.now());
		
		return  feesRepository.save(fees);
	}
	
	

}
