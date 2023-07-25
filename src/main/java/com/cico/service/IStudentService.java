package com.cico.service;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import com.cico.model.Student;
import com.cico.payload.StudentLoginResponse;


public interface IStudentService {
public Student getStudentByUserId(String userId);

public Student getStudentByDeviceId(String deviceId);

public StudentLoginResponse login(String userId, String password, String fcmId, String deviceId, String deviceType);

public Map<String, Object> approveDevice(String userId, String deviceId);

public Map<String, Object> checkInCheckOut(String latitude,String longitude,String time,String type,String date,MultipartFile studentImage,MultipartFile attachment,String workReport,HttpHeaders header);

public Map<String, Object> dashboard(HttpHeaders header);

public Map<String, Object> studentMispunchRequest(HttpHeaders header, String time, String date, String workReport);

public Map<String, Object> getStudentProfileApi(HttpHeaders header);

public Map<String, Object> studentEarlyCheckoutRequest(HttpHeaders header, String latitude, String longitude, String time, String date,
		String type, String workReport, MultipartFile studentImage);

public Map<String, Object> getStudentCheckInCheckOutHistory(HttpHeaders header, String startDate, String endDate,
		Integer limit, Integer offset);
		
public Map<String, Object> studentChangePassword(HttpHeaders header, String oldPassword, String newPassword);

public Map<String, Object> updateStudentProfile(HttpHeaders header, String fullName, String mobile, String dob, String email,
		MultipartFile profilePic);

public Map<String, Object> getTodayAttendance(Integer studentId);

public Map<String, Object> studentAttendanceMonthFilter(HttpHeaders header, Integer monthNo);

public Map<String, Object> getCalenderData(Integer id, Integer month, Integer year);
}
