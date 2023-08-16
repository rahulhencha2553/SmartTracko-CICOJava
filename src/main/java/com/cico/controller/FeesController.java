package com.cico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.model.Fees;
import com.cico.service.IFeesService;


@RestController
@RequestMapping("/fees")
public class FeesController {
	@Autowired
	private IFeesService feesService;

	@PostMapping("/createStudentFees")
	public ResponseEntity<Fees> createStudentFees(@RequestParam("studentId") Integer studentId,@RequestParam("courseId") Integer courseId,@RequestParam("finalFees") Double finalFees,@RequestParam("date") String date)
	{
		Fees createStudentFees = feesService.createStudentFees(studentId,courseId,finalFees,date);
		return ResponseEntity.status(HttpStatus.CREATED).body(createStudentFees);
	}
}
