package com.cico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.model.Fees;
import com.cico.payload.FeesResponse;
import com.cico.payload.PageResponse;
import com.cico.service.IFeesService;
import com.cico.util.AppConstants;


@RestController
@RequestMapping("/fees")
@CrossOrigin("*")
public class FeesController {
	@Autowired
	private IFeesService feesService;

	@PostMapping("/createStudentFees")
	public ResponseEntity<Fees> createStudentFees(@RequestParam("studentId") Integer studentId,@RequestParam("courseId") Integer courseId,@RequestParam("finalFees") Double finalFees,@RequestParam("date") String date)
	{
		Fees createStudentFees = feesService.createStudentFees(studentId,courseId,finalFees,date);
		return ResponseEntity.status(HttpStatus.CREATED).body(createStudentFees);
	}
	
	@GetMapping("/feesListApi")
	public PageResponse<FeesResponse> feesListApi(@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size){
		return feesService.feesList(page,size);
		
	}
	@GetMapping("/findByFeesId")
	public ResponseEntity<FeesResponse> findByFeesId(@RequestParam("feesId") Integer feesId)
	{
		FeesResponse findByFeesId = feesService.findByFeesId(feesId);
		return new ResponseEntity<FeesResponse>(findByFeesId,HttpStatus.OK);
	}
	
	@GetMapping("/searchByName")
	public ResponseEntity<List<FeesResponse>> searchByName(@RequestParam("fullName") String fullName)
	{
		List<FeesResponse> searchByName = feesService.searchByName(fullName);
		return new ResponseEntity<List<FeesResponse>>(searchByName,HttpStatus.OK);
	}
	@GetMapping("/findFeesByDates")
	public ResponseEntity<List<FeesResponse>> findFeesByDates(@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate)
	{
		 List<FeesResponse> findFeesByDates = feesService.findFeesByDates(startDate,endDate);
		return new ResponseEntity<List<FeesResponse>>(findFeesByDates,HttpStatus.OK);
	}
	@GetMapping("/feesCompletedList")
	public PageResponse<FeesResponse> feesCompleteList(@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size)
	{
		 
		return feesService.feesCompleteList(page,size);
	}
	
}