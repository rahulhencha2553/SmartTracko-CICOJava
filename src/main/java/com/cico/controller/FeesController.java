package com.cico.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.model.Fees;
import com.cico.model.FeesPay;
import com.cico.payload.FeesResponse;
import com.cico.payload.PageResponse;
import com.cico.service.IFeesPayService;
import com.cico.service.IFeesService;
import com.cico.util.AppConstants;



@RestController
@RequestMapping("/fees")
@CrossOrigin("*")
public class FeesController {
	@Autowired
	private IFeesService feesService;
	@Autowired
	private IFeesPayService feesPayService;

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
	public ResponseEntity<List<FeesResponse>> searchByName(@RequestParam("fullName") String fullName,@RequestParam("status") String status)
	{
		System.out.println(status);
		List<FeesResponse> searchByName = feesService.searchByName(fullName,status);
		return new ResponseEntity<List<FeesResponse>>(searchByName,HttpStatus.OK);
	}
	
	@GetMapping("/findFeesByDates")
	public ResponseEntity<List<FeesResponse>> findFeesByDates(@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,@RequestParam("status") String status)
	{
		System.out.println(status);
		 List<FeesResponse> findFeesByDates = feesService.findFeesByDates(startDate,endDate,status);
		return new ResponseEntity<List<FeesResponse>>(findFeesByDates,HttpStatus.OK);
	}
	
	@GetMapping("/feesCompletedList")
	public PageResponse<FeesResponse> feesCompleteList(@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size)
	{
		 
		return feesService.feesCompleteList(page,size);
	}
	
	@PostMapping("/feesPay")
	public ResponseEntity<FeesPay> feesPay(@RequestParam("feesId") Integer feesId,@RequestParam("feesPayAmount") Double feesPayAmount,@RequestParam("payDate") String payDate,@RequestParam("recieptNo") String recieptNo,@RequestParam("description") String description)
	{
		FeesPay feesPay = feesPayService.feesPayService(feesId,feesPayAmount,payDate,recieptNo,description);
		return ResponseEntity.status(HttpStatus.CREATED).body(feesPay);
	}
	
	@GetMapping("/feesPayList")
	public PageResponse<FeesResponse> feesPayList(@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size)
	{
		return feesPayService.feesPayList(page,size);
	}
	
	@PutMapping("/updateFeesApi")
	public ResponseEntity<Fees> updateFeesApi(@RequestBody Fees fees)
	{
		Fees updateFees = feesService.updateFees(fees);
		return new ResponseEntity<>(updateFees,HttpStatus.OK);
	}

   @GetMapping("/getFeesCollectionMonthAndYearWise")
   public ResponseEntity<?>getFeesCollectionMonthAndYearWise(@RequestParam("year")Integer year){
	    ResponseEntity<?> feesCollectionMonthAndYearWise = feesService.getFeesCollectionMonthAndYearWise(year);
     return new ResponseEntity<>(feesCollectionMonthAndYearWise,HttpStatus.OK);
   }

   @GetMapping("/getTotalFeesCollection")
   public ResponseEntity<?>getTotalfeesCollection(){
	    ResponseEntity<?> feesCollectionMonthAndYearWise = feesService.getTotalfeesCollection();
     return new ResponseEntity<>(feesCollectionMonthAndYearWise,HttpStatus.OK);
   }

	
	@GetMapping("/getAllTransectionsByStudentId")
	public ResponseEntity<?> getAllTransectionsOfStudent(@RequestParam("studentId") Integer studentId){
		return feesPayService.getAllTransectionByStudentId(studentId);
	}

}
