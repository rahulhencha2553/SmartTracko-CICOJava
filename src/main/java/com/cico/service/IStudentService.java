package com.cico.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Student;
import com.cico.payload.ApiResponse;
import com.cico.payload.OnLeavesResponse;
import com.cico.payload.PageResponse;
import com.cico.payload.StudentResponse;
import com.cico.payload.TodayLeavesRequestResponse;

public interface IStudentService {
	public Student getStudentByUserId(String userId);

	public Student getStudentByInUseDeviceId(String deviceId);

	public ResponseEntity<?> login(String userId, String password, String fcmId, String deviceId, String deviceType);

	public ResponseEntity<ApiResponse> approveDevice(String userId, String deviceId);

	public ResponseEntity<?> checkInCheckOut(String latitude, String longitude, String time, String type, String date,
			MultipartFile studentImage, MultipartFile attachment, String workReport, HttpHeaders header);

	public ResponseEntity<?> dashboard(HttpHeaders header);

	public ResponseEntity<?> studentMispunchRequest(HttpHeaders header, String time, String date, String workReport,
			MultipartFile attachment);

	public ResponseEntity<?> getStudentProfileApi(HttpHeaders header);

	public ResponseEntity<?> studentEarlyCheckoutRequest(HttpHeaders header, String latitude, String longitude,
			String time, String date, String type, String workReport, MultipartFile studentImage,
			MultipartFile attachment);

	public ResponseEntity<?> getStudentCheckInCheckOutHistory(HttpHeaders header, String startDate, String endDate,
			Integer limit, Integer offset);

	public ResponseEntity<?> studentChangePassword(HttpHeaders header, String oldPassword, String newPassword);

	public ResponseEntity<?> updateStudentProfile(HttpHeaders header, String fullName, String mobile, String dob,
			String email, MultipartFile profilePic);

	public Map<String, Object> getTodayAttendance(Integer studentId);

	public Map<String, Object> studentAttendanceMonthFilter(HttpHeaders header, Integer monthNo);

	public Map<String, Object> getCalenderData(Integer id, Integer month, Integer year);

	public Map<String, Object> getStudentData(Integer studentId);

	public Map<String, Object>getTotalTodayAbsentStudent();

	public List<OnLeavesResponse> getTotalStudentInLeaves();
	
	public List<TodayLeavesRequestResponse>getTotalTodaysLeavesRequest();

	public Boolean approveStudentLeaveReqeust(Integer studentId,Integer leaveId, String status);

	public PageResponse<StudentResponse> getAllStudentData(Integer page, Integer size);

	public List<StudentResponse> searchStudentByName(String fullName);

	public StudentResponse getStudentById(Integer studentId);

	public Student registerStudent(Student student);

	public ResponseEntity<?> getStudentProfileForWeb(Integer studentId);

	public Student updateStudent(Student student);

	public ResponseEntity<?> getStudentOverAllAttendanceData(Integer studentId);


	public ResponseEntity<?> getTodaysPresentsAndEarlyCheckouts(String key);

	public ResponseEntity<?> getMonthwiseAttendence(Integer month);


}
