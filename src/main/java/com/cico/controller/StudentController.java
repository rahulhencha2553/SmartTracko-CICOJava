package com.cico.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cico.payload.ApiResponse;
import com.cico.payload.StudentLoginResponse;
import com.cico.service.IStudentService;
import com.cico.util.AppConstants;

@RequestMapping("/student")
@RestController
@CrossOrigin("*")
public class StudentController {

//	@Autowired
//	AuthenticationManager manager;

	@Autowired
	private IStudentService studentService;

	@PostMapping("/studentLoginApi")
	public ResponseEntity<?> loginStudent(@RequestParam("userId") String userId,
			@RequestParam("password") String password, @RequestParam("fcmId") String fcmId,
			@RequestParam("deviceId") String deviceId, @RequestParam("deviceType") String deviceType) {
			
		return studentService.login(userId, password, fcmId, deviceId, deviceType);

	}

	@PostMapping("/studentDeviceIdApprovalApi")
	public ResponseEntity<?> approveDevice(@RequestParam("userId") String userId,
			@RequestParam("deviceId") String deviceId) {

		return studentService.approveDevice(userId, deviceId);

	}

	@PostMapping("/studentCheckInCheckOutApi")
	public ResponseEntity<?> checkInCheckOut(@RequestParam("lat") String latitude,
			@RequestParam("long") String longitude, @RequestParam("time") String time,
			@RequestParam("type") String type, @RequestParam("date") String date,
			@RequestPart("studentImage") MultipartFile studentImage,
			@RequestPart(name="attachment",required = false) MultipartFile attachment, @RequestParam("workReport") String workReport,
			@RequestHeader HttpHeaders header) {

		return studentService.checkInCheckOut(latitude, longitude, time, type, date,
				studentImage, attachment, workReport, header);
		 
	}

	@GetMapping("/studentDashboardApi")
	public ResponseEntity<?> studentDashboard(@RequestHeader HttpHeaders header) {
		return studentService.dashboard(header);
	}

	@PostMapping("/studentMispunchRequestApi")
	public ResponseEntity<?> studentMispunchRequest(
			@RequestHeader(name=AppConstants.AUTHORIZATION) HttpHeaders header, @RequestParam("time") String time,
			@RequestParam("date") String date, @RequestParam("workReport") String workReport,@RequestPart(name="attachment",required = false) MultipartFile attechment) {
		
		return studentService.studentMispunchRequest(header, time, date,workReport,attechment);
	}

	@PostMapping("/studentEarlyCheckoutRequestApi")
	public ResponseEntity<?> studentEarlyCheckoutRequest(
			@RequestHeader(name=AppConstants.AUTHORIZATION) HttpHeaders header, @RequestParam("lat") String latitude,
			@RequestParam("long") String longitude, @RequestParam("time") String time,
			@RequestParam("date") String date, @RequestParam("type") String type,
			@RequestParam("workReport") String workReport, @RequestPart("studentImage") MultipartFile studentImage, @RequestPart(name="attachment",required =  false) MultipartFile attachment) {

		return studentService.studentEarlyCheckoutRequest(header, latitude,
				longitude, time, date, type, workReport, studentImage,attachment);
	}

	@GetMapping("/getStudentCheckInCheckOutHistory")
	public ResponseEntity<?> getStudentCheckInCheckOutHistory(
			@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		
		return studentService.getStudentCheckInCheckOutHistory(header, startDate, endDate, page,
				size);
	}

	@GetMapping("/getStudentProfileApi")
	public ResponseEntity<?> getStudentProfileApi(@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header) {
		return studentService.getStudentProfileApi(header);
	}

	@PostMapping("/studentChangePasswordApi")
	public ResponseEntity<?> studentChangePassword(@RequestHeader HttpHeaders header,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
		
		return studentService.studentChangePassword(header, oldPassword,
				newPassword);

	}

	@PostMapping("/updateStudentProfileApi")
	public ResponseEntity<?> updateStudentProfileApi(@RequestHeader HttpHeaders header,
			@RequestParam("full_name") String fullName, @RequestParam("mobile") String mobile,
			@RequestParam("dob") String dob, @RequestParam("email") String email,
			@RequestPart("profile_pic") MultipartFile profilePic) {
		
		return studentService.updateStudentProfile(header, fullName, mobile, dob,
				email, profilePic);
	}

	@GetMapping("/getTodayAttendance/{studentId}")
	public ResponseEntity<Map<String, Object>> getTodayAttendance(@PathVariable Integer studentId) {
		Map<String, Object> todayAttendance = studentService.getTodayAttendance(studentId);
		return ResponseEntity.ok(todayAttendance);
	}

	@GetMapping("/studentAttendanceMonthFilter")
	public ResponseEntity<Map<String, Object>> studentAttendanceMonthFilter(
			@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("monthNo") Integer monthNo) {
		Map<String, Object> studentAttendanceMonthFilter = studentService.studentAttendanceMonthFilter(header, monthNo);
		return ResponseEntity.ok(studentAttendanceMonthFilter);
	}
	
	@GetMapping("/getStudentCalenderData")
	public ResponseEntity<Map<String, Object>> getCalenderData(@Param("id") Integer id, @Param("month") Integer month,
			@Param("year") Integer year) {
		Map<String, Object> response = studentService.getCalenderData(id, month, year);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
