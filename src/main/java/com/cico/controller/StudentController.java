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

import com.cico.payload.StudentLoginResponse;
import com.cico.service.IStudentService;
import com.cico.util.AppConstants;

@RequestMapping("/Student")
@RestController
@CrossOrigin("*")
public class StudentController {

//	@Autowired
//	AuthenticationManager manager;

	@Autowired
	private IStudentService studentService;

	@PostMapping("/studentLoginApi")
	public ResponseEntity<StudentLoginResponse> loginStudent(@RequestParam("userId") String userId,
			@RequestParam("password") String password, @RequestParam("fcmId") String fcmId,
			@RequestParam("deviceId") String deviceId, @RequestParam("deviceType") String deviceType) {

		StudentLoginResponse studentLogin = studentService.login(userId, password, fcmId, deviceId, deviceType);
		return ResponseEntity.ok(studentLogin);
	}

	@PostMapping("/studentDeviceIdApprovalApi")
	public ResponseEntity<Map<String, Object>> approveDevice(@RequestParam("userId") String userId,
			@RequestParam("deviceId") String deviceId) {

		Map<String, Object> approvalData = studentService.approveDevice(userId, deviceId);
		return ResponseEntity.ok(approvalData);
	}

	@PostMapping("/studentCheckInCheckOutApi")
	public ResponseEntity<Map<String, Object>> checkInCheckOut(@RequestParam("lat") String latitude,
			@RequestParam("long") String longitude, @RequestParam("time") String time,
			@RequestParam("type") String type, @RequestParam("date") String date,
			@RequestPart("studentImage") MultipartFile studentImage,
			@RequestPart("attachment") MultipartFile attachment, @RequestParam("workReport") String workReport,
			@RequestHeader HttpHeaders header) {

		Map<String, Object> checkInCheckOutData = studentService.checkInCheckOut(latitude, longitude, time, type, date,
				studentImage, attachment, workReport, header);
		return ResponseEntity.ok(checkInCheckOutData);
	}

	@GetMapping("/studentDashboardApi")
	public ResponseEntity<Map<String, Object>> studentDashboard(@RequestHeader HttpHeaders header) {
		Map<String, Object> dashboard = studentService.dashboard(header);
		return ResponseEntity.ok(dashboard);
	}

	@PostMapping("/studentMispunchRequestApi")
	public ResponseEntity<Map<String, Object>> studentMispunchRequest(
			@RequestHeader(name=AppConstants.AUTHORIZATION) HttpHeaders header, @RequestParam("time") String time,
			@RequestParam("date") String date, @RequestParam("workReport") String workReport) {
		Map<String, Object> studentMispunchRequest = studentService.studentMispunchRequest(header, time, date,
				workReport);
		return ResponseEntity.ok(studentMispunchRequest);
	}

	@PostMapping("/studentEarlyCheckoutRequestApi")
	public ResponseEntity<Map<String, Object>> studentEarlyCheckoutRequest(
			@RequestHeader(name=AppConstants.AUTHORIZATION) HttpHeaders header, @RequestParam("lat") String latitude,
			@RequestParam("long") String longitude, @RequestParam("time") String time,
			@RequestParam("date") String date, @RequestParam("type") String type,
			@RequestParam("workReport") String workReport, @RequestPart("studentImage") MultipartFile studentImage) {

		Map<String, Object> studentEarlyCheckoutData = studentService.studentEarlyCheckoutRequest(header, latitude,
				longitude, time, date, type, workReport, studentImage);

		return ResponseEntity.ok(studentEarlyCheckoutData);
	}

	@GetMapping("/getStudentCheckInCheckOutHistory")
	public ResponseEntity<Map<String, Object>> getStudentCheckInCheckOutHistory(
			@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		Map<String, Object> map = studentService.getStudentCheckInCheckOutHistory(header, startDate, endDate, page,
				size);
		return ResponseEntity.ok(map);
	}

	@GetMapping("/getStudentProfileApi")
	public ResponseEntity<Map<String, Object>> getStudentProfileApi(
			@RequestHeader(name = AppConstants.AUTHORIZATION) HttpHeaders header) {
		Map<String, Object> studentProfileApi = studentService.getStudentProfileApi(header);
		return ResponseEntity.ok(studentProfileApi);
	}

	@PostMapping("/studentChangePasswordApi")
	public ResponseEntity<Map<String, Object>> studentChangePassword(@RequestHeader HttpHeaders header,
			@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
		Map<String, Object> studentChangePassword = studentService.studentChangePassword(header, oldPassword,
				newPassword);
		return ResponseEntity.ok(studentChangePassword);
	}

	@PostMapping("/updateStudentProfileApi")
	public ResponseEntity<Map<String, Object>> updateStudentProfileApi(@RequestHeader HttpHeaders header,
			@RequestParam("full_name") String fullName, @RequestParam("mobile") String mobile,
			@RequestParam("dob") String dob, @RequestParam("email") String email,
			@RequestPart("profile_pic") MultipartFile profilePic) {
		Map<String, Object> updateStudentProfile = studentService.updateStudentProfile(header, fullName, mobile, dob,
				email, profilePic);
		return ResponseEntity.ok(updateStudentProfile);
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
