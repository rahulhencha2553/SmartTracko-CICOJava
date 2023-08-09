package com.cico.service.impl;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Attendance;
import com.cico.model.Leaves;
import com.cico.model.OrganizationInfo;
import com.cico.model.QrManage;
import com.cico.model.Student;
import com.cico.model.StudentWorkReport;
import com.cico.payload.ApiResponse;
import com.cico.payload.AttendanceLogResponse;
import com.cico.payload.CheckinCheckoutHistoryResponse;
import com.cico.payload.CheckoutResponse;
import com.cico.payload.DashboardResponse;
import com.cico.payload.MispunchResponse;
import com.cico.payload.OnLeavesResponse;
import com.cico.payload.PageResponse;
import com.cico.payload.StudentCalenderResponse;
import com.cico.payload.StudentLoginResponse;
import com.cico.payload.StudentResponse;
import com.cico.payload.TodayLeavesRequestResponse;
import com.cico.repository.AttendenceRepository;
import com.cico.repository.LeaveRepository;
import com.cico.repository.OrganizationInfoRepository;
import com.cico.repository.QrManageRepository;
import com.cico.repository.StudentRepository;
import com.cico.repository.StudentWorkReportRepository;
import com.cico.security.JwtUtil;
import com.cico.service.IFileService;
import com.cico.service.IStudentService;
import com.cico.util.AppConstants;
import com.cico.util.HelperService;
import com.cico.util.Roles;

@Service
public class StudentServiceImpl implements IStudentService {

	public static final String ANDROID = "Android";
	public static final String IOS = "iOS";

	@Autowired
	private StudentRepository studRepo;

	@Autowired
	private QrManageRepository qrManageRepository;

	

	@Autowired
	private LeaveRepository leaveRepository;

	@Autowired
	private AttendenceRepository attendenceRepository;

	@Autowired
	private OrganizationInfoRepository organizationInfoRepository;

	@Autowired
	private StudentWorkReportRepository workReportRepository;

	@Autowired
	private JwtUtil util;

	@Autowired
	private HelperService helperService;

	@Autowired
	private IFileService fileService;

	@Autowired
	private ModelMapper mapper;

	@Value("${fileUploadPath}")
	private String IMG_UPLOAD_DIR;

	@Value("${workReportUploadPath}")
	private String WORK_UPLOAD_DIR;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public Student getStudentByUserId(String userId) {
		return studRepo.findByUserId(userId);
	}

	public Student getStudentByInUseDeviceId(String deviceId) {
		return studRepo.findByInUseDeviceId(deviceId);
	}

//	@Override
//	public ResponseEntity<?> login1(String userId, String password, String fcmId, String deviceId,
//			String deviceType) {
//		Map<String, Object> response = new HashMap<>();
//		if (deviceType.equals(ANDROID) || deviceType.equals(IOS)) {
//			Student studentByUserId = getStudentByUserId(userId);
//			Student studentByInUseDeviceId = getStudentByInUseDeviceId(deviceId);
//			StudentLoginResponse studentResponse = new StudentLoginResponse();
//			
//			if (studentByUserId != null && studentByUserId.getPassword().equals(password)) {
//				
//				if(!studentByUserId.getInUseDeviceId().equals(deviceId)&&!studentByUserId.getInUseDeviceId().equals("")) {
//					if( studentByInUseDeviceId != null) {
//						studentResponse.setIsDeviceAlreadyInUse(true);
//
//					}else {
//						studentResponse.setIsDeviceAlreadyInUse(false);	
//					}
//					studentResponse.setToken(null);
//					studentResponse.setIsDeviceIdDifferent(true);
//					studentResponse.setIsFeesDue(false);
//					response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
//					response.put("student", studentResponse);
//					return new ResponseEntity<>(response,HttpStatus.OK);
//				}
//				if (studentByUserId.getIsActive()) {
//					if (studentByInUseDeviceId != null) {
//
//						if (userId.equals(studentByInUseDeviceId.getUserId())) {
//							System.out.println("1 case");
//							String token = util.generateTokenForStudent(studentByUserId.getStudentId().toString(),
//									studentByUserId.getUserId(), deviceId, Roles.STUDENT.toString());
//
//							StudentLoginResponse studentResponse2 = new StudentLoginResponse(token, false, false, false);
//							studentResponse2.setStudentData(studentByUserId);
//							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
//							response.put("student", studentResponse2);
//							return new ResponseEntity<>(response,HttpStatus.OK);
//						}
//
//						else {
//							System.out.println("2 case");
//							studentResponse.setToken(null);
//							studentResponse.setIsDeviceIdDifferent(true);
//							studentResponse.setIsFeesDue(false);
//							studentResponse.setIsDeviceAlreadyInUse(true);
//
//							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
//							response.put("student", studentResponse);
//							return new ResponseEntity<>(response,HttpStatus.OK);
//						}
//
//					}
//
//					else {
//						if ((studentByUserId.getInUseDeviceId() == null)
//								|| (studentByUserId.getDeviceId().trim().equals(""))) {
////							System.out.println("3 case");
////							studentByUserId.setInUseDeviceId(deviceId);
////							studentByUserId.setFcmId(fcmId);
////							studentByUserId.setDeviceType(deviceType);
////							studRepo.save(studentByUserId);
//
//							String token = util.generateTokenForStudent(studentByUserId.getStudentId().toString(),
//									studentByUserId.getUserId(), deviceId, Roles.STUDENT.toString());
//
//							studentResponse.setToken(token);
//							studentResponse.setIsDeviceIdDifferent(false);
//							studentResponse.setIsDeviceAlreadyInUse(false);
//							studentResponse.setIsFeesDue(false);
//							studentResponse.setStudentData(studentByUserId);
//							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
//							response.put("student", studentResponse);
//							return new ResponseEntity<>(response,HttpStatus.OK);
//						}
//
//						else {
//							System.out.println("4 case");
//							studentResponse.setToken("");
//							studentResponse.setIsDeviceIdDifferent(true);
//							studentResponse.setIsDeviceAlreadyInUse(false);
//							studentResponse.setIsFeesDue(false);
//
//							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
//							response.put("student", studentResponse);
//							return new ResponseEntity<>(response,HttpStatus.OK);
//
//						}
//					}
//				} else {
//					response.put(AppConstants.MESSAGE, AppConstants.STUDENT_DEACTIVE);
//					return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//				}
//					
//			}
//
//			else {
//				response.put(AppConstants.MESSAGE, AppConstants.INVALID_CREDENTIALS);
//				return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//			}
//			
//		}
//
//		else {
//			response.put(AppConstants.MESSAGE, AppConstants.INVALID_DEVICE_TYPE);
//			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//		}
//		
//	}

	@Override
	public Student registerStudent(Student student) {
		Student student1 = studRepo.save(student);
		student1.setPassword(passwordEncoder.encode("123456"));
		student1.setContactFather(student.getContactFather());
		student1.setRole(Roles.STUDENT.toString());
		student1.setUserId(student1.getFullName().split(" ")[0] + "@" + student1.getStudentId());
		student1.setProfilePic("default.png");
		student1.setDeviceId("");
		student1.setInUseDeviceId("");
		student1.setCreatedDate(LocalDateTime.now());
		return studRepo.save(student1);
	}

	@Override
	public ResponseEntity<?> login(String userId, String password, String fcmId, String deviceId, String deviceType) {
		Map<String, Object> response = new HashMap<>();
		if (deviceType.equals(ANDROID) || deviceType.equals(IOS)) {
			Student studentByUserId = getStudentByUserId(userId);
			Student studentByInUseDeviceId = getStudentByInUseDeviceId(deviceId);
			StudentLoginResponse studentResponse = new StudentLoginResponse();

			if (studentByUserId != null && encoder.matches(password, studentByUserId.getPassword())) {
				if (studentByUserId.getIsActive()) {
					if (studentByInUseDeviceId == null) {// device Id is not present in db
						// login
						if (studentByUserId.getInUseDeviceId().equals("")) {
							System.out.println("1 CASE");
							studentByUserId.setInUseDeviceId(deviceId);
							studentByUserId.setFcmId(fcmId);
							studentByUserId.setIsDeviceApproved("Approved");
							Student student = studRepo.save(studentByUserId);
							studentResponse.setStudentData(student);

							String token = util.generateTokenForStudent(studentByUserId.getStudentId().toString(),
									studentByUserId.getUserId(), deviceId, Roles.STUDENT.toString());

							studentResponse.setToken(token);
							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
							response.put("student", studentResponse);
							return new ResponseEntity<>(response, HttpStatus.OK);
						} else {
							System.out.println("2 CASE");
							studentResponse.setIsDeviceIdDifferent(true);
							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
							response.put("student", studentResponse);

							return new ResponseEntity<>(response, HttpStatus.OK);
						}

					} else {
						if (studentByUserId.getUserId().equals(studentByInUseDeviceId.getUserId())) {
							System.out.println("3 CASE");
							String token = util.generateTokenForStudent(studentByUserId.getStudentId().toString(),
									studentByUserId.getUserId(), deviceId, Roles.STUDENT.toString());
							studentResponse.setToken(token);
							studentResponse.setStudentData(studentByInUseDeviceId);
							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
							response.put("student", studentResponse);

							return new ResponseEntity<>(response, HttpStatus.OK);
						} else {
							System.out.println("4 CASE");
							studentResponse.setIsDeviceAlreadyInUse(true);
							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
							response.put("student", studentResponse);
							return new ResponseEntity<>(response, HttpStatus.OK);

						}

					}

				} else {
					response.put(AppConstants.MESSAGE, AppConstants.STUDENT_DEACTIVE);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}

			} else {
				response.put(AppConstants.MESSAGE, AppConstants.INVALID_CREDENTIALS);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

		}

		response.put(AppConstants.MESSAGE, AppConstants.INVALID_DEVICE_TYPE);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<ApiResponse> approveDevice(String userId, String deviceId) {
		if (Objects.nonNull(userId) && Objects.nonNull(deviceId)) {
			Student findByUserId = studRepo.findByUserId(userId);

			if (findByUserId != null) {
				findByUserId.setDeviceId(deviceId);
				findByUserId.setIsDeviceApproved("Not Approved");
				Student updateStudent = studRepo.save(findByUserId);

				if (updateStudent != null) {

					return new ResponseEntity<>(
							new ApiResponse(Boolean.TRUE, AppConstants.APPROVAL_REQUEST, HttpStatus.OK), HttpStatus.OK);
				}

				else {
					return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, AppConstants.FAILED, HttpStatus.OK),
							HttpStatus.OK);
				}
			}

			else {
				return new ResponseEntity<>(
						new ApiResponse(Boolean.FALSE, AppConstants.INVALID_CREDENTIALS, HttpStatus.BAD_REQUEST),
						HttpStatus.BAD_REQUEST);
			}

		}

		else {
			return new ResponseEntity<>(
					new ApiResponse(Boolean.FALSE, AppConstants.ALL_REQUIRED, HttpStatus.BAD_REQUEST),
					HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> checkInCheckOut(String latitude, String longitude, String time, String type, String date,
			MultipartFile studentImage, MultipartFile attachment, String workReport, HttpHeaders header) {
		Map<String, Object> response = new HashMap<>();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());
		if (validateToken) {
			if (latitude != null && longitude != null && time != null && date != null && type != null && !type.isEmpty()
					&& studentImage != null && studentImage.getOriginalFilename() != null) {
				Integer studentId = Integer.parseInt(util
						.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
				Attendance attendanceData = attendenceRepository.findByStudentIdAndCheckInDate(studentId,
						LocalDate.parse(date));
				if (type.equals(AppConstants.CHECK_IN)) {
					if (Objects.isNull(attendanceData)) {
						Attendance checkInAttenedanceData = new Attendance();
						checkInAttenedanceData.setStudentId(studentId);
						checkInAttenedanceData.setCheckInDate(LocalDate.now());
						checkInAttenedanceData.setCheckInTime(LocalTime.now());
						checkInAttenedanceData.setCheckInLat(latitude);
						checkInAttenedanceData.setCheckInLong(longitude);
//						String savePath = helperService.saveImage(studentImage, IMG_UPLOAD_DIR);
						String imageName = fileService.uploadFileInFolder(studentImage, IMG_UPLOAD_DIR);
						checkInAttenedanceData.setCheckInImage(imageName);
						checkInAttenedanceData.setCreatedDate(LocalDateTime.now());
						checkInAttenedanceData.setUpdatedDate(LocalDateTime.now());
						checkInAttenedanceData.setCheckOutStatus("Pending");
						Attendance saveAttendenceCheckInData = attendenceRepository.save(checkInAttenedanceData);
						if (saveAttendenceCheckInData != null) {
							response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
							return new ResponseEntity<>(response, HttpStatus.OK);
						} else {
							response.put(AppConstants.MESSAGE, AppConstants.FAILED);
							return new ResponseEntity<>(response, HttpStatus.OK);
						}
					} else {
						response.put(AppConstants.MESSAGE, AppConstants.ALREADY_CHECKIN);
						return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
					}
				} else if (type.equals(AppConstants.CHECK_OUT)) {
					if (workReport != null) {
						if (attendanceData != null) {
							OrganizationInfo organizationInfo = organizationInfoRepository.findById(1).get();
							LocalDateTime checkInDateTime = attendanceData.getCreatedDate();
							LocalDateTime checkOutDateTime = LocalDateTime.now();
							Duration duration = Duration.between(checkInDateTime, checkOutDateTime);
							long workingHours = duration.getSeconds();
							if (workingHours >= (Long.parseLong(organizationInfo.getWorkingHours()) * 3600)) {
								attendanceData.setCheckOutDate(LocalDate.now());
								attendanceData.setCheckOutTime(LocalTime.now());
								attendanceData.setCheckOutLat(latitude);
								attendanceData.setCheckOutLong(longitude);
								String imageName = fileService.uploadFileInFolder(studentImage, IMG_UPLOAD_DIR);
								attendanceData.setCheckOutImage(imageName);
								attendanceData.setWorkingHour(workingHours);
								attendanceData.setCheckOutStatus("Approved");
								attendanceData.setUpdatedDate(LocalDateTime.now());

								Attendance saveAttendenceCheckOutData = attendenceRepository.save(attendanceData);

								StudentWorkReport studentWorkReport = new StudentWorkReport();
								studentWorkReport.setAttendanceId(saveAttendenceCheckOutData.getAttendanceId());
								if (!attachment.getOriginalFilename().equals("")) {
									String workImageName = fileService.uploadFileInFolder(attachment, WORK_UPLOAD_DIR);
									studentWorkReport.setAttachment(workImageName);
								}
								studentWorkReport.setWorkReport(workReport);
								studentWorkReport.setCreatedDate(LocalDateTime.now());

								StudentWorkReport workReportData = workReportRepository.save(studentWorkReport);
								if (Objects.nonNull(saveAttendenceCheckOutData)) {
									response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
									return new ResponseEntity<>(response, HttpStatus.OK);
								} else {
									response.put(AppConstants.MESSAGE, AppConstants.FAILED);
									return new ResponseEntity<>(response, HttpStatus.OK);
								}
							} else {
								response.put(AppConstants.MESSAGE, AppConstants.EARLY_CHECKOUT);
								return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
							}
						} else {
							response.put(AppConstants.MESSAGE, AppConstants.NOT_CHECKED_IN);
							return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
						}
					} else {
						response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
						return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
					}
				}
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			}
		}

		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public ResponseEntity<?> dashboard(HttpHeaders header) {
		Map<String, Object> response = new HashMap<String, Object>();

		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();

		StudentResponse studentResponseDto = new StudentResponse();
		DashboardResponse dashboardResponseDto = new DashboardResponse();
		MispunchResponse mispunchResponseDto = new MispunchResponse();

		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());

		if (validateToken) {
			Attendance attendance = new Attendance();

			attendance = attendenceRepository.findByStudentIdAndCheckInDate(studentId, LocalDate.now());
			if (attendance != null) {
				if (attendance.getCheckOutDate() == null) {
					dashboardResponseDto.setAttendanceStatus(AppConstants.CHECK_IN);
					dashboardResponseDto.setCheckInDate(attendance.getCheckInDate());
					dashboardResponseDto.setCheckInTime(attendance.getCheckInTime());
					dashboardResponseDto.setCheckInImage(attendance.getCheckInImage());

				} else {
					dashboardResponseDto.setAttendanceStatus(AppConstants.CHECK_OUT);
					dashboardResponseDto.setCheckInDate(attendance.getCheckInDate());
					dashboardResponseDto.setCheckInTime(attendance.getCheckInTime());
					dashboardResponseDto.setCheckInImage(attendance.getCheckInImage());
					dashboardResponseDto.setCheckOutImage(attendance.getCheckOutImage());
					dashboardResponseDto.setCheckOutTime(attendance.getCheckOutTime());
					dashboardResponseDto.setCheckOutDate(attendance.getCheckOutDate());
				}
			}

			else
				dashboardResponseDto.setAttendanceStatus(AppConstants.CHECK_OUT);

//			dashboardResponseDto.setIsFeesDue(false);
//			dashboardResponseDto.setFeesDueDate(null);

			if (student != null) {
				studentResponseDto.setUserId(student.getUserId());
				studentResponseDto.setStudentId(student.getStudentId());
				studentResponseDto.setFullName(student.getFullName());
				studentResponseDto.setMobile(student.getMobile());
				studentResponseDto.setEmail(student.getEmail());
				studentResponseDto.setDob(student.getDob());

				studentResponseDto.setProfilePic(student.getProfilePic());
			}

			dashboardResponseDto.setStudentResponseDto(studentResponseDto);
			dashboardResponseDto.setOrganizationInfo(organizationInfoRepository.findById(1).get());

			Attendance attendance2 = checkStudentMispunch(studentId);

			if (attendance2 != null) {
				dashboardResponseDto.setIsMispunch(true);
				mispunchResponseDto.setMispunchStatus(attendance2.getCheckOutStatus());
				mispunchResponseDto.setMispunchDate(attendance2.getCheckInDate());
				mispunchResponseDto.setCheckInTime(attendance2.getCheckInTime());
				dashboardResponseDto.setMispunchResponseDto(mispunchResponseDto);
			}

			else
				dashboardResponseDto.setIsMispunch(false);

			if (Objects.nonNull(dashboardResponseDto)) {
				response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
				QrManage findByUserId = qrManageRepository.findByUserId(username);
				if (Objects.nonNull(findByUserId))
					dashboardResponseDto.setIsWebLoggedIn(true);
				response.put("dashboardResponseDto", dashboardResponseDto);
				return new ResponseEntity<>(response, HttpStatus.OK);

			} else {
				response.put(AppConstants.MESSAGE, AppConstants.FAILED);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		}

		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	private Attendance checkStudentMispunch(Integer studentId) {

		return attendenceRepository.findByStudentIdAndCheckInDateLessThanCurrentDate(studentId, LocalDate.now());
	}

	@Override
	public ResponseEntity<?> studentMispunchRequest(HttpHeaders header, String time, String date, String workReport,
			MultipartFile attachment) {
		Map<String, Object> response = new HashMap<>();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());
		if (validateToken) {

			Integer studentId = Integer.parseInt(
					util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
			if (time != null && date != null && workReport != null) {
				Attendance attendanceData = attendenceRepository.findByStudentIdAndCheckInDate(studentId,
						LocalDate.parse(date));

				if (attendanceData != null && Objects.nonNull(attendanceData.getCheckOutStatus().equals("Pending"))) {
					LocalDateTime checkInDateTime = attendanceData.getCreatedDate();
					LocalDateTime checkOutDateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
					Duration duration = Duration.between(checkInDateTime, checkOutDateTime);
					long workingHour = duration.getSeconds();
					attendanceData.setCheckOutDate(LocalDate.parse(date));
					attendanceData.setCheckOutTime(LocalTime.parse(time));
					attendanceData.setWorkingHour(workingHour);
					attendanceData.setCheckOutStatus("Approved");
					attendanceData.setUpdatedDate(checkOutDateTime);
					Attendance saveAttdance = attendenceRepository.save(attendanceData);

					StudentWorkReport studentWorkReport = new StudentWorkReport();
					studentWorkReport.setAttendanceId(saveAttdance.getAttendanceId());
					if (!attachment.getOriginalFilename().equals("")) {
						String workImageName = fileService.uploadFileInFolder(attachment, WORK_UPLOAD_DIR);
						studentWorkReport.setAttachment(workImageName);
					}
					studentWorkReport.setWorkReport(workReport);
					studentWorkReport.setCreatedDate(LocalDateTime.now());

					StudentWorkReport workReportData = workReportRepository.save(studentWorkReport);
					if (Objects.nonNull(saveAttdance)) {
						response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
						return new ResponseEntity<>(response, HttpStatus.OK);

					} else {
						response.put(AppConstants.MESSAGE, AppConstants.FAILED);
						return new ResponseEntity<>(response, HttpStatus.OK);
					}

				} else {
					response.put(AppConstants.MESSAGE, AppConstants.NOT_CHECKED_IN);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			}
		}

		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}

	@Override
	public ResponseEntity<?> getStudentProfileApi(HttpHeaders header) {
		Map<String, Object> response = new HashMap<>();

		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));

		Student student = studRepo.findByUserIdAndIsActive(username, true).get();

		StudentResponse studentResponseDto = new StudentResponse();
		DashboardResponse dashboardResponseDto = new DashboardResponse();

		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());

		if (validateToken) {

			Integer studentId = Integer.parseInt(
					util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
			LocalDate currentDate = LocalDate.now();
			Student findByStudentId = studRepo.findByStudentId(studentId);
			if (findByStudentId != null) {
				studentResponseDto.setFullName(student.getFullName());
				studentResponseDto.setMobile(student.getMobile());
				studentResponseDto.setEmail(student.getEmail());
				studentResponseDto.setDob(student.getDob());
				studentResponseDto.setApplyForCourse(student.getApplyForCourse());
				studentResponseDto.setJoinDate(student.getJoinDate());

				if (studentResponseDto.getCompletionDuration() == false) {
					long monthsDifference = HelperService.getMonthsDifference(student.getJoinDate(), currentDate);

					if (monthsDifference >= 9) {
						studentResponseDto.setCompletionDuration(true);

					}

				}

				studentResponseDto.setProfilePic(student.getProfilePic());
//				File f = new File(IMG_UPLOAD_DIR + student.getProfilePic());
//                
//				if((student.getProfilePic()!=null) && (f.exists())) {
//			
//					studentResponseDto.setProfilePic(student.getProfilePic());
//				}
			}
			dashboardResponseDto.setStudentResponseDto(studentResponseDto);
			dashboardResponseDto.setOrganizationInfo(organizationInfoRepository.findById(1).get());

			if (Objects.nonNull(dashboardResponseDto)) {
				response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
				QrManage findByUserId = qrManageRepository.findByUserId(username);
				if (Objects.nonNull(findByUserId))
					dashboardResponseDto.setIsWebLoggedIn(true);
				response.put("dashboardResponseDto", dashboardResponseDto);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.FAILED);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		}

		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public ResponseEntity<?> studentEarlyCheckoutRequest(HttpHeaders header, String latitude, String longitude,
			String time, String date, String type, String workReport, MultipartFile studentImage,
			MultipartFile attachment) {
		Map<String, Object> response = new HashMap<>();

		CheckoutResponse checkoutResponseDto = new CheckoutResponse();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());

		if (validateToken) {

			if (latitude != null && longitude != null && time != null && date != null && type != null
					&& studentImage != null && studentImage.getOriginalFilename() != null && workReport != null) {
				Attendance attendance = attendenceRepository.findByStudentIdAndCheckInDateAndCheckOutDate(studentId,
						LocalDate.parse(date), null);

				if (attendance != null) {

					LocalDateTime checkInDateTime = attendance.getCreatedDate();
					LocalDateTime checkOutDateTime = LocalDateTime.now();
					Duration duration = Duration.between(checkInDateTime, checkOutDateTime);
					Long workingHours = duration.getSeconds();

					checkoutResponseDto.setCheckoutDate(LocalDate.now());
					checkoutResponseDto.setCheckoutTime(LocalTime.now());
					checkoutResponseDto.setCheckoutLat(latitude);
					checkoutResponseDto.setCheckoutLong(longitude);

//					String savePath = helperService.saveImage(studentImage, IMG_UPLOAD_DIR);
					String imageName = fileService.uploadFileInFolder(studentImage, IMG_UPLOAD_DIR);
					checkoutResponseDto.setCheckoutImage(imageName);

					checkoutResponseDto.setWorkingHour(workingHours);
					checkoutResponseDto.setCheckoutImage(imageName);
					checkoutResponseDto.setCheckoutStatus("Approved");
					checkoutResponseDto.setUpdatedDate(LocalDateTime.now());

					attendance.setCheckOutDate(checkoutResponseDto.getCheckoutDate());
					attendance.setCheckOutTime(checkoutResponseDto.getCheckoutTime());
					attendance.setCheckOutLat(checkoutResponseDto.getCheckoutLat());
					attendance.setCheckOutLong(checkoutResponseDto.getCheckoutLong());
					attendance.setCheckOutImage(checkoutResponseDto.getCheckoutImage());
					attendance.setWorkingHour(checkoutResponseDto.getWorkingHour());
					attendance.setCheckOutStatus(checkoutResponseDto.getCheckoutStatus());
					attendance.setUpdatedDate(checkoutResponseDto.getUpdatedDate());

					Attendance updateAttendance = attendenceRepository.save(attendance);
					StudentWorkReport studentWorkReport = new StudentWorkReport(0, attendance.getAttendanceId(),
							workReport, LocalDateTime.now());
					if (!attachment.getOriginalFilename().equals("")) {
						String workImageName = fileService.uploadFileInFolder(attachment, WORK_UPLOAD_DIR);
						studentWorkReport.setAttachment(workImageName);
					}
					workReportRepository.save(studentWorkReport);

					if (updateAttendance != null) {
						response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else {
						response.put(AppConstants.MESSAGE, AppConstants.FAILED);
						return new ResponseEntity<>(response, HttpStatus.OK);
					}
				} else {
					response.put(AppConstants.MESSAGE, AppConstants.NOT_CHECKED_IN);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		}
		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	// get history data after checkout
	@Override
	public ResponseEntity<?> getStudentCheckInCheckOutHistory(HttpHeaders header, String startDate, String endDate,
			Integer limit, Integer offset) {
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		System.out.println(username);
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());

		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), username);
		Map<String, Object> response = new HashMap<>();

		if (validateToken) {
			Page<Attendance> attendanceHistory = attendenceRepository.findAttendanceHistory(studentId,
					LocalDate.parse(startDate), LocalDate.parse(endDate),
					PageRequest.of(limit, offset, Sort.by(Direction.DESC, "attendanceId")));
			List<CheckinCheckoutHistoryResponse> historyDto = new ArrayList<>();
			if (!attendanceHistory.isEmpty()) {
				List<Attendance> content = attendanceHistory.getContent();
				for (Attendance attendance : content) {
					StudentWorkReport stdWorkReport = workReportRepository
							.findByAttendanceId(attendance.getAttendanceId());
					CheckinCheckoutHistoryResponse cicoHistoryObjDto = getCicoHistoryObjDto(attendance);
					if (stdWorkReport != null) {
						cicoHistoryObjDto.setWorkReport(stdWorkReport.getWorkReport());
						cicoHistoryObjDto.setAttachment(stdWorkReport.getAttachment());
					}
					historyDto.add(cicoHistoryObjDto);
				}

				Map<String, Object> map = new HashMap<>();
				map.put("attendance", historyDto);
				map.put("totalPages", attendanceHistory.getTotalPages());
				map.put("totalAttendance", attendanceHistory.getTotalElements());
				map.put("currentPage", attendanceHistory.getNumber());
				map.put("pageSize", attendanceHistory.getNumberOfElements());
				response.put("response", map);
				response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}
		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	private CheckinCheckoutHistoryResponse getCicoHistoryObjDto(Attendance attendance) {
		CheckinCheckoutHistoryResponse historyDto = new CheckinCheckoutHistoryResponse();
		historyDto.setAttendanceId(attendance.getAttendanceId());
		historyDto.setCheckInDate(attendance.getCheckInDate());
		historyDto.setCheckInImage(attendance.getCheckInImage());
		historyDto.setCheckInTime(attendance.getCheckInTime());
		historyDto.setCheckOutDate(attendance.getCheckOutDate());
		historyDto.setCheckOutImage(attendance.getCheckOutImage());
		historyDto.setCheckOutTime(attendance.getCheckOutTime());
		historyDto.setWorkingHour(attendance.getWorkingHour());

		return historyDto;
	}

	@Override
	public ResponseEntity<?> studentChangePassword(HttpHeaders header, String oldPassword, String newPassword) {
		Map<String, Object> response = new HashMap<>();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());
		if (validateToken) {
			if (Objects.nonNull(oldPassword) && Objects.nonNull(newPassword)) {
				if (encoder.matches(oldPassword, student.getPassword())) {
					Boolean checkPasswordValidation = true;
					if (checkPasswordValidation) {
						student.setPassword(encoder.encode(newPassword));
						Student updatedStudent = studRepo.save(student);
						if (updatedStudent != null) {
							response.put(AppConstants.MESSAGE, AppConstants.PASSWORD_CHANGED);
							return new ResponseEntity<>(response, HttpStatus.OK);
						} else {
							response.put(AppConstants.MESSAGE, AppConstants.PASSWORD_NOT_CHANGED);
							return new ResponseEntity<>(response, HttpStatus.OK);
						}
					} else {
						response.put(AppConstants.MESSAGE, AppConstants.INVALID_PASSWORD_FORMAT);
						return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
					}
				} else {
					response.put(AppConstants.MESSAGE, AppConstants.WRONG_PASSWORD);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.ALL_REQUIRED);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		}

		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public ResponseEntity<?> updateStudentProfile(HttpHeaders header, String fullName, String mobile, String dob,
			String email, MultipartFile profilePic) {
		Map<String, Object> response = new HashMap<>();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());
		if (validateToken) {
			if (!fullName.equals("")) {
				student.setFullName(fullName);
			}
			if (!dob.equals("")) {
				student.setDob(LocalDate.parse(dob));
			}
			if (!mobile.equals("")) {
				student.setMobile(mobile);
			}
			if (!email.equals("")) {
				student.setEmail(email);
			}
			if (!(profilePic.isEmpty()) && !(profilePic.getOriginalFilename().equals(""))) {
				String imageName = fileService.uploadFileInFolder(profilePic, IMG_UPLOAD_DIR);
				student.setProfilePic(imageName);
			}

			if (Objects.nonNull(student)) {
				Student updatedStudent = studRepo.save(student);
				if (updatedStudent != null) {
					response.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					response.put(AppConstants.MESSAGE, AppConstants.FAILED);
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			} else {
				response.put(AppConstants.MESSAGE, AppConstants.FAILED);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}

		response.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public Map<String, Object> getTodayAttendance(Integer studentId) {
		Map<String, Object> map = new HashMap<>();
		Attendance attendance = attendenceRepository.findByStudentIdAndCheckInDate(studentId, LocalDate.now());
		// String format =
		// attendance.getCheckInTime().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
		if (Objects.nonNull(attendance)) {
			map.put(AppConstants.MESSAGE, AppConstants.SUCCESS);
			map.put("Attendance", attendance);
		} else {
			map.put(AppConstants.MESSAGE, AppConstants.NOT_CHECKED_IN);

		}
		return map;
	}

	@Override
	public Map<String, Object> studentAttendanceMonthFilter(HttpHeaders header, Integer monthNo) {
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());

		Map<String, Object> map = new HashMap<>();

		if (validateToken) {
			List<Attendance> findByStudentIdAndMonthNo = attendenceRepository.findByStudentIdAndMonthNo(studentId,
					monthNo);
			Collections.reverse(findByStudentIdAndMonthNo);
			if (!findByStudentIdAndMonthNo.isEmpty()) {
				map.put(AppConstants.MESSAGE, AppConstants.SUCCESS);

				map.put("AttendanceData", findByStudentIdAndMonthNo);
			} else {
				map.put(AppConstants.MESSAGE, AppConstants.NO_DATA_FOUND);

				map.put("AttendanceData", findByStudentIdAndMonthNo);
			}
		} else {
			map.put(AppConstants.MESSAGE, AppConstants.UNAUTHORIZED);

		}
		return map;
	}

	public Map<String, Object> getCalenderData(Integer id, Integer month, Integer year) { //working code
		Map<String, Object> response = new HashMap<>();
		LocalDate joinDate = studRepo.findById(id).get().getJoinDate();
		if (year >= joinDate.getYear() && year <= LocalDate.now().getYear()) {

			List<Integer> present = new ArrayList<>();
			List<Integer> leaves = new ArrayList<>();
			List<Integer> absent = new ArrayList<>();

			// Get the first day of the month
			LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

			YearMonth yearMonth = YearMonth.of(year, month);
			int lastDay = yearMonth.lengthOfMonth();
			LocalDate lastDayOfMonth = LocalDate.of(year, month, lastDay);
			LocalDate currentDay = firstDayOfMonth;
			StudentCalenderResponse data = new StudentCalenderResponse();
			LocalDate currentDate = LocalDate.now();

			if (LocalDate.now().getYear() != year || month <= LocalDate.now().getMonthValue()) {
				// counting total leaves
				List<Leaves> leavesData = leaveRepository.findAllByStudentId(id);
				for (Leaves list : leavesData) {
					LocalDate startLeaveDate = list.getLeaveDate();
					LocalDate endLeaveDate = list.getLeaveEndDate();

					while (!startLeaveDate.isAfter(endLeaveDate) && startLeaveDate.getMonthValue() == month) {
						leaves.add(startLeaveDate.getDayOfMonth());
						startLeaveDate = startLeaveDate.plusDays(1);
					}
				}

				currentDay = firstDayOfMonth;

				// getting total present
				// Get total present
				List<Attendance> studentAttendanceList = attendenceRepository.findAllByStudentId(id);
				for (Attendance attendance : studentAttendanceList) {
					LocalDate attendanceDate = attendance.getCheckInDate();
					if (attendanceDate.getMonthValue() == month && attendanceDate.getYear() == year) {
						present.add(attendanceDate.getDayOfMonth());
					}
				}

				// getting total absent for current month and till today date
				if (currentDate.getMonthValue() == month && LocalDate.now().getYear() == year) {
					while (currentDay.getDayOfMonth() <= currentDate.getDayOfMonth() - 1
							&& !currentDay.isAfter(lastDayOfMonth)) {
						if (!present.contains(currentDay.getDayOfMonth())
								&& currentDay.getDayOfWeek() != DayOfWeek.SUNDAY) {
							absent.add(currentDay.getDayOfMonth());
						}
						currentDay = currentDay.plusDays(1);
					}
				} else {// getting absent for previous month from current month
					if (month == joinDate.getMonth().getValue() && (year == joinDate.getYear())) {
						currentDay = joinDate;
					}
					while (!currentDay.isAfter(lastDayOfMonth)) {
						if (!present.contains(currentDay.getDayOfMonth())
								&& currentDay.getDayOfWeek() != DayOfWeek.SUNDAY) {
							absent.add(currentDay.getDayOfMonth());
						}
						currentDay = currentDay.plusDays(1);
					}
				}
			}

			data.setPresent(present);
			data.setAbsent(absent);
			data.setLeaves(leaves);

			response.put("StudentCalenderData", data);
			response.put("status", true);
		} else {

			response.put("status", false);
		}
		return response;
	}


	@Override
	public Map<String, Object> getStudentData(Integer studentId) {
		Student student = studRepo.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found with this id :"));
		Map<String, Object> response = new HashMap<>();
		response.put("studentName", student.getFullName());
		response.put("profilePic", student.getProfilePic());
		response.put("course", student.getApplyForCourse());
		response.put("id", student.getStudentId());
		return response;
	}

	@Override
	public Map<String, Object> getTotalTodayAbsentStudent() { // getting present and absent students

		Map<String, Object> response = new HashMap<>();
		LocalDate today = LocalDate.now();
		List<Object[]> result = studRepo.getTotalTodayAbsentStudent(today);
		// List<Attendance> totalPresentToday = studRepo.getTotalPresentToday(today);
		List<Student> absentStudents = new ArrayList<>();
		for (Object[] row : result) {
			Student student = new Student();
			student.setFullName((String) row[0]);
			student.setMobile((String) row[1]);
			student.setProfilePic((String) row[2]);

			student.setApplyForCourse((String) row[3]);
			student.setStudentId((Integer) row[4]);
			absentStudents.add(student);
		}

		Long totalPresentToday = studRepo.getTotalPresentToday(today);// present
		response.put("totalPresent", totalPresentToday);
		response.put("totalAbsent", absentStudents);
		return response;
	}

	// here student details depends on this method [ getStudentData(id) ]so ,
	// changes
	// reflect here if any changes are done in that method
	@Override
	public List<OnLeavesResponse> getTotalStudentInLeaves() {
		List<OnLeavesResponse> response = new ArrayList<>();
		List<Object[]> totalStudentInLeaves = studRepo.getTotalStudentInLeaves();
		for (Object[] row : totalStudentInLeaves) {
			OnLeavesResponse leavesResponse = new OnLeavesResponse();
			Integer id = (Integer) row[0];
			Map<String, Object> studentData = this.getStudentData(id); // ?????? <-
			leavesResponse.setProfilePic(studentData.get("profilePic").toString());
			leavesResponse.setApplyForCourse(studentData.get("course").toString());
			leavesResponse.setName(studentData.get("studentName").toString());
			leavesResponse.setLeaveDate((LocalDate) row[1]);
			leavesResponse.setLeaveEndDate((LocalDate) row[2]);
			response.add(leavesResponse);
		}
		return response;
	}

	@Override
	public List<TodayLeavesRequestResponse> getTotalTodaysLeavesRequest() {

		List<Object[]> totalTodaysLeavesRequest = studRepo.getTotalTodaysLeavesRequest();
		List<TodayLeavesRequestResponse> response = new ArrayList<>();

		for (Object[] row : totalTodaysLeavesRequest) {
			TodayLeavesRequestResponse leavesRequestResponse = new TodayLeavesRequestResponse();
			leavesRequestResponse.setLeaveDate((LocalDate) row[0]);
			leavesRequestResponse.setLeaveEndDate((LocalDate) row[1]);
			leavesRequestResponse.setStudentId((Integer) row[2]);
			leavesRequestResponse.setFullName((String) row[3]);
			leavesRequestResponse.setProfilePic((String) row[4]);
			leavesRequestResponse.setApplyForCourse((String) row[5]);
			leavesRequestResponse.setLeaveTypeId((Integer) row[6]);
			leavesRequestResponse.setLeaveDuration((Integer) row[7]);
			leavesRequestResponse.setLeaveReason((String) row[8]);
			leavesRequestResponse.setLeaveId((Integer) row[9]);
			leavesRequestResponse.setLeaveTypeName((String) row[10]);
			response.add(leavesRequestResponse);
		}

		return response;
	}

	@Override
	public Boolean approveStudentLeaveReqeust(Integer studentId, Integer leaveId, String leaveStatus) {

		int updateStudentLeaves = 0;
		if (leaveStatus.equals("approve")) {
			updateStudentLeaves = leaveRepository.updateStudentLeaves(studentId, 1, leaveId);
		} else if (leaveStatus.equals("deny")) {
			updateStudentLeaves = leaveRepository.updateStudentLeaves(studentId, 2, leaveId);
		}
		return (updateStudentLeaves != 0) ? true : false;
	}

	@Override
	public PageResponse<StudentResponse> getAllStudentData(Integer page, Integer size) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "studentId");
		Page<Student> student = studRepo.findAllByIsCompletedAndIsActive(false, true, pageable);

		if (student.getNumberOfElements() == 0) {
			return new PageResponse<>(Collections.emptyList(), student.getNumber(), student.getSize(),
					student.getTotalElements(), student.getTotalPages(), student.isLast());
		}
		List<StudentResponse> asList = Arrays.asList(mapper.map(student.getContent(), StudentResponse[].class));
		return new PageResponse<>(asList, student.getNumber(), student.getSize(), student.getTotalElements(),
				student.getTotalPages(), student.isLast());
	}

	@Override
	public List<StudentResponse> searchStudentByName(String fullName) {
		// TODO Auto-generated method stub
		List<Student> findByFullNameContaining = studRepo.findAllByFullNameContaining(fullName);
		if (Objects.isNull(findByFullNameContaining)) {
			throw new ResourceNotFoundException("Student was not found");
		}
		List<StudentResponse> asList = Arrays.asList(mapper.map(findByFullNameContaining, StudentResponse[].class));
		return asList;
	}

	@Override
	public StudentResponse getStudentById(Integer studentId) {
		// TODO Auto-generated method stub
		Student student = studRepo.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found from given id"));
		return mapper.map(student, StudentResponse.class);

	}

	@Override
	public ResponseEntity<?> getStudentProfileForWeb(Integer studentId) {
		Student findByStudentId = studRepo.findByStudentId(studentId);
		if (Objects.isNull(findByStudentId))
			throw new ResourceNotFoundException("Student Not Found");
		return new ResponseEntity<>(findByStudentId, HttpStatus.OK);
	}

	@Override
	public Student updateStudent(Student student) {
		Student save = studRepo.save(student);
		return save;
	}

	@Override
	public ResponseEntity<?> getStudentOverAllAttendanceData(Integer studentId) {
		List<AttendanceLogResponse> attendanceList = new ArrayList<>();
		List<Attendance> findAllByStudentId = attendenceRepository.findAllByStudentId(studentId);
		for (Attendance attendance : findAllByStudentId) {
			AttendanceLogResponse logResponse = new AttendanceLogResponse();
			logResponse.setDate(attendance.getCheckInDate());
			logResponse.setCheckIn(attendance.getCheckInTime());
			logResponse.setCheckOut(attendance.getCheckOutTime());
			logResponse.setTimeIn(attendance.getWorkingHour());
			if (attendance.getWorkingHour() >= 32400) {
				logResponse.setStatus("FullDay");
			} else {
				logResponse.setStatus("HalfDay");
				;
			}
			attendanceList.add(logResponse);
		}

		List<Leaves> leavesList = leaveRepository.getStudentAllLeavesAndApproved(studentId, 1);
		for (Leaves leaves : leavesList) {
			AttendanceLogResponse logResponse = null;
			LocalDate leavesDate = leaves.getLeaveDate();
			for (int i = 1; i <= leaves.getLeaveDuration(); i++) {
				logResponse = new AttendanceLogResponse();
				logResponse.setDate(leavesDate);
				logResponse.setStatus("OnLeave");
				leavesDate = leavesDate.plusDays(1);
				attendanceList.add(logResponse);
			}
		}
		attendanceList.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
		return new ResponseEntity<>(attendanceList, HttpStatus.OK);
	}
	
	
	  public static int countSundays(int month) {
	        int totalDays = LocalDate.of(LocalDate.now().getYear(), month, 1).lengthOfMonth();
	        int sundays = 0;

	        for (int day = 1; day <= totalDays; day++) {
	            LocalDate date = LocalDate.of(LocalDate.now().getYear(), month, day);
	            if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
	                sundays++;
	            }
	        }

	        return sundays;
	    }
	
		@Override
		public ResponseEntity<?> getMonthwiseAttendence(Integer month) {
			Map<String, Long> map = new HashMap();

			Long presentStudents = attendenceRepository.countPresentStudentsByMonth(month);
			Long onLeaveStudents = attendenceRepository.countLeaveStudentsByMonth(month);
			Long totalStudents = studRepo.countTotalStudents();

			Long absentStudents = totalStudents - (presentStudents + onLeaveStudents);

			map.put("Present", presentStudents);
			map.put("Absent", absentStudents);
			map.put("OnLeave", onLeaveStudents);

			return new ResponseEntity(map, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getTodaysPresentsAndEarlyCheckouts(String key) {
		System.out.println(key);
		List<Student> studentsPresentAndEarlyCheckout = new ArrayList<>();
		if(key.equals("Present")) {
			System.out.println("inside present");
		 List<Object[]> todaysPresents = attendenceRepository.getTodaysPresents(LocalDate.now());
		 System.out.println(todaysPresents);
		 for (Object[] row : todaysPresents) {
			Student student = new Student();
			student.setFullName((String) row[0]);
			student.setMobile((String) row[1]);
			student.setProfilePic((String) row[2]);
			student.setApplyForCourse((String) row[3]);
			student.setStudentId((Integer) row[4]);
			studentsPresentAndEarlyCheckout .add(student);
		 }	
		}else {
			List<Object[]> todaysEarlyCheckouts = attendenceRepository.getTodaysEarlyCheckouts(LocalDate.now());
			for (Object[] row : todaysEarlyCheckouts) {
				Student student = new Student();
				student.setFullName((String) row[0]);
				student.setMobile((String) row[1]);
				student.setProfilePic((String) row[2]);
				student.setApplyForCourse((String) row[3]);
				student.setStudentId((Integer) row[4]);
				studentsPresentAndEarlyCheckout .add(student);
			}
		}	
		return new ResponseEntity<>(studentsPresentAndEarlyCheckout,HttpStatus.OK);
	}

}
