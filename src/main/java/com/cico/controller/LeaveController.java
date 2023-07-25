package com.cico.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cico.model.LeaveType;
import com.cico.model.Leaves;
import com.cico.payload.PageResponse;
import com.cico.service.ILeaveService;
import com.cico.util.AppConstants;



@RestController
@RequestMapping("/Leave")
@CrossOrigin("*")
public class LeaveController {

	@Autowired
	private ILeaveService leaveService;

	@GetMapping("/getLeavesType")
	public ResponseEntity<List<LeaveType>> getAllLeavesType() {
		List<LeaveType> leaveTypes = leaveService.getAllLeavesType();
		return ResponseEntity.ok(leaveTypes);
	}

	@PostMapping("/addStudentLeave")
	public ResponseEntity<Map<String, Object>> addStudentLeave(@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("leaveTypeId") Integer leaveTypeId, @RequestParam("leaveStartDate") String leaveStartDate,
			@RequestParam(value = "leaveEndDate") String leaveEndDate, @RequestParam("leaveReason") String leaveReason,
			@RequestParam("leaveDayType") String leaveDayType, @RequestParam("halfDayType") String halfDayType) {
		Map<String, Object> addStudentLeave = leaveService.addStudentLeave(header, leaveTypeId, leaveStartDate,
				leaveEndDate, leaveReason, leaveDayType, halfDayType);

		return ResponseEntity.ok(addStudentLeave);
	}

	@GetMapping("/getStudentLeaves")
	public ResponseEntity<PageResponse<Leaves>> getStudentLeaves(@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam(name="page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page, @RequestParam(name="size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		PageResponse<Leaves> studentLeaves = leaveService.getStudentLeaves(header, page, size);
		return ResponseEntity.ok(studentLeaves);
	}

	@PostMapping("/deleteStudentLeave")
	public ResponseEntity<Map<String, Object>> deleteStudentLeave(@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("leaveId") Integer leaveId) {
		Map<String, Object> deleteStudentLeave = leaveService.deleteStudentLeave(header, leaveId);
		return ResponseEntity.ok(deleteStudentLeave);
	}

	@PostMapping("/retractStudentLeave")
	public ResponseEntity<Map<String, Object>> retractStudentLeave(@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("leaveId") Integer leaveId) {
		Map<String, Object> retractStudentLeave = leaveService.retractStudentLeave(header, leaveId);
		return ResponseEntity.ok(retractStudentLeave);
	}

	@GetMapping("/studentLeaveMonthFilter")
	public ResponseEntity<Map<String, Object>> studentLeaveMonthFilter(@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("monthNo") Integer monthNo) {
		Map<String, Object> studentLeaveMonthFilter = leaveService.studentLeaveMonthFilter(header, monthNo);
		return ResponseEntity.ok(studentLeaveMonthFilter);
	}
}

