package com.cico.service.impl;

import java.io.File;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cico.exception.InvalidCredentialsException;
import com.cico.exception.ResourceNotFoundException;
import com.cico.model.Attendance;
import com.cico.model.Leaves;
import com.cico.model.OrganizationInfo;
import com.cico.model.Student;
import com.cico.model.StudentWorkReport;
import com.cico.payload.CheckinCheckoutHistoryResponse;
import com.cico.payload.CheckoutResponse;
import com.cico.payload.DashboardResponse;
import com.cico.payload.MispunchResponse;
import com.cico.payload.StudentCalenderResponse;
import com.cico.payload.StudentLoginResponse;
import com.cico.payload.StudentResponse;
import com.cico.repository.AttendenceRepository;
import com.cico.repository.LeaveRepository;
import com.cico.repository.OrganizationInfoRepository;
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

	public static final String NOT_CHECKED_IN = "NOT_CHECKED_IN";
	public static final String ALREADY_CHECKIN = "ALREADY_CHECKIN";
	public static final String CHECK_IN = "checkIn";
	public static final String CHECK_OUT = "checkOut";

	@Autowired
	private StudentRepository studRepo;
	
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

	@Value("${fileUploadPath}")
	private String IMG_UPLOAD_DIR;

	@Value("${workReportUploadPath}")
	private String WORK_UPLOAD_DIR;

	public Student getStudentByUserId(String userId) {
		return studRepo.findByUserId(userId);
	}

	public Student getStudentByDeviceId(String deviceId) {
		return studRepo.findByDeviceId(deviceId);
	}

	@Override
	public StudentLoginResponse login(String userId, String password, String fcmId, String deviceId,
			String deviceType) {

		if (deviceType.equals("Android") || deviceType.equals("iOS")) {
			// manager.authenticate(new UsernamePasswordAuthenticationToken(userId,
			// password));
			// String token = util.generateToken(dto.getUserId());
			Student studentByUserId = getStudentByUserId(userId);
			Student studentByDeviceId = getStudentByDeviceId(deviceId);
			StudentLoginResponse studentResponse = new StudentLoginResponse();

			if (studentByUserId != null) {

				if (studentByUserId.getIsActive().equals(true)) {
					if (studentByDeviceId != null) {

						if (userId.equalsIgnoreCase(studentByDeviceId.getUserId())) {

							String token = util.generateTokenForStudent(studentByUserId.getStudentId().toString(),
									studentByUserId.getUserId(), deviceId, Roles.STUDENT.toString());

							StudentLoginResponse studentResponse2 = new StudentLoginResponse(token, false, true, false);

							return studentResponse2;
						}

						else {
							studentByUserId.setUserId(userId);
							studentByUserId.setInUseDeviceId(deviceId);
							studRepo.save(studentByUserId);

							studentResponse.setToken(null);
							studentResponse.setIsDeviceIdDifferent(true);
							studentResponse.setIsFeesDue(false);
							studentResponse.setIsDeviceAlreadyInUse(true);

							return studentResponse;
						}

					}

					else {
						if ((studentByUserId.getDeviceId() == null)
								|| (studentByUserId.getDeviceId().trim().equals(""))) {

							studentByUserId.setDeviceId(deviceId);
							studentByUserId.setFcmId(fcmId);
							studentByUserId.setDeviceType(deviceType);
							studRepo.save(studentByUserId);

							String token = util.generateTokenForStudent(studentByUserId.getStudentId().toString(),
									studentByUserId.getUserId(), deviceId, Roles.STUDENT.toString());

							studentResponse.setToken(token);
							studentResponse.setIsDeviceIdDifferent(false);
							studentResponse.setIsDeviceAlreadyInUse(false);
							studentResponse.setIsFeesDue(false);
						}

						else {
							studentResponse.setToken("");
							studentResponse.setIsDeviceIdDifferent(true);
							studentResponse.setIsDeviceAlreadyInUse(false);
							studentResponse.setIsFeesDue(false);

							return studentResponse;
						}
					}
				} else
					throw new ResourceNotFoundException("Student_Deactive");

				return studentResponse;
			}

			else
				throw new InvalidCredentialsException(AppConstants.INVALID_CREDENTIALS + "ram");
		}

		else
			throw new ResourceNotFoundException("Invalid_Device_Type");
	}

	@Override
	public Map<String, Object> approveDevice(String userId, String deviceId) {
		Map<String, Object> map = new HashMap();
		if ((!userId.equals("")) && (!deviceId.equals(""))) {
			Student findByUserId = studRepo.findByUserId(userId);

			if (findByUserId != null) {
				findByUserId.setDeviceId(deviceId);
				findByUserId.setIsDeviceApproved("Not Approved");
				Student updateStudent = studRepo.save(findByUserId);

				if (updateStudent != null) {
					map.put("Message", "APPROVAL_REQUEST");
					map.put("StatusCode", 200);

				}

				else {
					map.put("Message", AppConstants.FAILED);
					map.put("StatusCode", 200);
				}
			}

			else {
				map.put("Message", AppConstants.INVALID_CREDENTIALS);
				map.put("StatusCode", 404);
			}

		}

		else {
			map.put("Message", AppConstants.ALL_REQUIRED);
			map.put("StatusCode", 404);
		}
		return map;
	}

	@Override
	public Map<String, Object> checkInCheckOut(String latitude, String longitude, String time, String type, String date,
			MultipartFile studentImage, MultipartFile attachment, String workReport, HttpHeaders header) {
		Map<String, Object> map = new HashMap<>();
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
				if (type.equals(CHECK_IN)) {
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
							map.put("Message", AppConstants.SUCCESS);
							map.put("StatusCode", 200);
						} else {
							map.put("Message", AppConstants.FAILED);
							map.put("StatusCode", 200);
						}
					} else {
						map.put("Message", ALREADY_CHECKIN);
						map.put("StatusCode", 400);
					}
				} else if (type.equals(CHECK_OUT)) {
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
								attendanceData.setCheckOutImage(imageName + studentImage.getOriginalFilename());
								attendanceData.setWorkingHour(workingHours);
								attendanceData.setCheckOutStatus("Approved");
								attendanceData.setUpdatedDate(LocalDateTime.now());

								Attendance saveAttendenceCheckOutData = attendenceRepository.save(attendanceData);

								StudentWorkReport studentWorkReport = new StudentWorkReport();
								studentWorkReport.setAttendanceId(saveAttendenceCheckOutData.getAttendanceId());
								String workImageName = fileService.uploadFileInFolder(attachment, WORK_UPLOAD_DIR);
								studentWorkReport.setAttachment(workImageName);
								studentWorkReport.setWorkReport(workReport);
								studentWorkReport.setCreatedDate(LocalDateTime.now());

								StudentWorkReport workReportData = workReportRepository.save(studentWorkReport);
								if (Objects.nonNull(saveAttendenceCheckOutData)) {
									map.put("Message", AppConstants.SUCCESS);
									map.put("StatusCode", 200);
								} else {
									map.put("Message", AppConstants.FAILED);
									map.put("StatusCode", 200);
								}
							} else {
								map.put("Message", "EARLY_CHECKOUT");
								map.put("StatusCode", 202);
							}
						} else {
							map.put("Message", NOT_CHECKED_IN);
							map.put("StatusCode", 400);
						}
					} else {
						map.put("Message", AppConstants.ALL_REQUIRED);
						map.put("StatusCode", 400);
					}
				}
			} else {
				map.put("Message", AppConstants.ALL_REQUIRED);
				map.put("StatusCode", 400);
			}
		} else {
			map.put("Message", AppConstants.UNAUTHORIZED);
			map.put("StatusCode", 401);
		}
		return map;
	}

	@Override
	public Map<String, Object> dashboard(HttpHeaders header) {
		Map<String, Object> map = new HashMap<String, Object>();

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
					dashboardResponseDto.setAttendanceStatus(CHECK_IN);
					dashboardResponseDto.setCheckInDate(attendance.getCheckInDate());
					dashboardResponseDto.setCheckInTime(attendance.getCheckInTime());
					dashboardResponseDto.setCheckInImage(attendance.getCheckInImage());

				} else {
					dashboardResponseDto.setAttendanceStatus(CHECK_OUT);
					dashboardResponseDto.setCheckInDate(attendance.getCheckInDate());
					dashboardResponseDto.setCheckInTime(attendance.getCheckInTime());
					dashboardResponseDto.setCheckInImage(attendance.getCheckInImage());
					dashboardResponseDto.setCheckOutImage(attendance.getCheckOutImage());
					dashboardResponseDto.setCheckOutTime(attendance.getCheckOutTime());
					dashboardResponseDto.setCheckOutDate(attendance.getCheckOutDate());
				}
			}

			else
				dashboardResponseDto.setAttendanceStatus(CHECK_OUT);

			dashboardResponseDto.setIsFeesDue(false);
			dashboardResponseDto.setFeesDueDate(null);

			if (student != null) {

				studentResponseDto.setStudentId(student.getStudentId());
				studentResponseDto.setFullName(student.getFullName());
				studentResponseDto.setMobile(student.getMobile());
				studentResponseDto.setEmail(student.getEmail());
				studentResponseDto.setDob(student.getDob());

				File f = new File(IMG_UPLOAD_DIR + student.getProfilePic());

				if (student.getProfilePic() != null && f.exists()) {
					studentResponseDto.setProfilePic(IMG_UPLOAD_DIR + student.getProfilePic());
				}
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
				map.put("Message", AppConstants.SUCCESS);
				map.put("StatusCode", 200);
				map.put("DashboardResponseDto", dashboardResponseDto);
			} else {
				map.put("Message", AppConstants.FAILED);
				map.put("StatusCode", 404);
				map.put("DashboardResponseDto", null);
			}

			return map;

		}

		else
			return null;

	}

	private Attendance checkStudentMispunch(Integer studentId) {

		return attendenceRepository.findByStudentIdAndCheckInDateLessThanCurrentDate(studentId, LocalDate.now());
	}

	@Override
	public Map<String, Object> studentMispunchRequest(HttpHeaders header, String time, String date, String workReport) {
		Map<String, Object> map = new HashMap<>();
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
					LocalDateTime checkOutDateTime = LocalDateTime.now();
					Duration duration = Duration.between(checkInDateTime, checkOutDateTime);
					long workingHour = duration.getSeconds();
					attendanceData.setCheckOutDate(LocalDate.now());
					attendanceData.setCheckOutTime(LocalTime.now());
					attendanceData.setWorkingHour(workingHour);
					attendanceData.setCheckOutStatus("Approved");
					attendanceData.setUpdatedDate(checkOutDateTime);
					Attendance saveAttdance = attendenceRepository.save(attendanceData);

					StudentWorkReport studentWorkReport = new StudentWorkReport();
					studentWorkReport.setAttendanceId(saveAttdance.getAttendanceId());
					studentWorkReport.setAttachment(null);
					studentWorkReport.setWorkReport(workReport);
					studentWorkReport.setCreatedDate(LocalDateTime.now());

					StudentWorkReport workReportData = workReportRepository.save(studentWorkReport);
					if (Objects.nonNull(saveAttdance)) {
						map.put("Message", AppConstants.SUCCESS);
						map.put("StatusCode", 200);
					} else {
						map.put("Message", AppConstants.FAILED);
						map.put("StatusCode", 200);

					}

				} else {
					map.put("Message", NOT_CHECKED_IN);
					map.put("StatusCode", 400);
				}
			} else {
				map.put("Message", AppConstants.ALL_REQUIRED);
				map.put("StatusCode", 400);
			}
		}
		return map;

	}

	@Override
	public Map<String, Object> getStudentProfileApi(HttpHeaders header) {
		Map<String, Object> map = new HashMap<>();

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
				map.put("Message", AppConstants.SUCCESS);
				map.put("StatusCode", 200);
				map.put("DashboardResponseDto", dashboardResponseDto);
			} else {
				map.put("Message", AppConstants.FAILED);
				map.put("StatusCode", 404);
				map.put("DashboardResponseDto", null);
			}

		}

		return map;
	}

	@Override
	public Map<String, Object> studentEarlyCheckoutRequest(HttpHeaders header, String latitude, String longitude,
			String time, String date, String type, String workReport, MultipartFile studentImage) {
		Map<String, Object> map = new HashMap<>();

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
							workReport, WORK_UPLOAD_DIR + studentImage.getOriginalFilename(), LocalDateTime.now());
					workReportRepository.save(studentWorkReport);

					if (updateAttendance != null) {
						map.put("Message", AppConstants.SUCCESS);
						map.put("StatusCode", 200);
					}

					else {
						map.put("Message", AppConstants.FAILED);
						map.put("StatusCode", 404);
					}
				}

				else {
					map.put("Message", NOT_CHECKED_IN);
					map.put("StatusCode", 404);
				}
			}

			else {
				map.put("Message", AppConstants.ALL_REQUIRED);
				map.put("StatusCode", 404);
			}
			return map;
		}

		else {
			map.put("Message", AppConstants.UNAUTHORIZED);
			map.put("StatusCode", 401);
		}
		return map;
	}

	@Override
	public Map<String, Object> getStudentCheckInCheckOutHistory(HttpHeaders header, String startDate, String endDate,
			Integer limit, Integer offset) {
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(
				util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());

		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), username);
		Map<String, Object> map = new HashMap<>();

		if (validateToken) {
			Page<Attendance> attendanceHistory = attendenceRepository.findAttendanceHistory(studentId,
					LocalDate.parse(startDate), LocalDate.parse(endDate),
					PageRequest.of(limit, offset, Sort.by(Direction.DESC, "attendanceId")));
			List<CheckinCheckoutHistoryResponse> historyDto = new ArrayList();

			List<Attendance> content = attendanceHistory.getContent();

			for (Attendance attendance : content) {
				historyDto.add(getCicoHistoryObjDto(attendance));
			}

			Map<String, Object> response = new HashMap<>();
			response.put("Attendance", historyDto);
			response.put("TotalPages", attendanceHistory.getTotalPages());
			response.put("TotalAttendance", attendanceHistory.getTotalElements());
			response.put("currentPage", attendanceHistory.getNumber());
			response.put("PageSize", attendanceHistory.getNumberOfElements());
			map.put("response", response);
			map.put("Message", AppConstants.SUCCESS);
		}

		else {
			map.put("Message", AppConstants.UNAUTHORIZED);
			map.put("StatusCode", 401);
		}

		return map;
	}

	private CheckinCheckoutHistoryResponse getCicoHistoryObjDto(Attendance attendance) {
		CheckinCheckoutHistoryResponse historyDto = new CheckinCheckoutHistoryResponse();
		historyDto.setAttendanceId(attendance.getAttendanceId());
		historyDto.setCheckInDate(attendance.getCheckInDate());
		historyDto.setCheckInImage(attendance.getCheckInImage());
		historyDto.setCheckInTime(attendance.getCheckInTime());
		historyDto.setCheckOutDate(attendance.getCheckInDate());
		historyDto.setCheckOutImage(attendance.getCheckOutImage());
		historyDto.setCheckOutTime(attendance.getCheckOutTime());
		historyDto.setWorkingHour(attendance.getWorkingHour());

		return historyDto;
	}

	@Override
	public Map<String, Object> studentChangePassword(HttpHeaders header, String oldPassword, String newPassword) {
		Map<String, Object> map = new HashMap<>();
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());
		if (validateToken) {
			if (!(oldPassword.equals("")) && !(newPassword.equals(""))) {
				if (student.getPassword().equals(oldPassword)) {
					Boolean checkPasswordValidation = true;
					if (checkPasswordValidation) {
						student.setPassword(newPassword);
						Student updatedStudent = studRepo.save(student);
						if (updatedStudent != null) {
							map.put("Message", "PASS_CHANGED");
							map.put("StatusCode", 200);
						} else {
							map.put("Message", "PASS_NOT_CHANGED");
							map.put("StatusCode", 200);
						}
					} else {
						map.put("Message", "INVALID_PASSWORD_FORMAT");
						map.put("StatusCode", 400);
					}
				} else {
					map.put("Message", "WRONG_PASSWORD");
					map.put("StatusCode", 400);
				}
			} else {
				map.put("Message", AppConstants.ALL_REQUIRED);
				map.put("StatusCode", 400);
			}
		} else {
			map.put("Message", AppConstants.UNAUTHORIZED);
			map.put("StatusCode", 401);
		}
		return map;
	}

	@Override
	public Map<String, Object> updateStudentProfile(HttpHeaders header, String fullName, String mobile, String dob,
			String email, MultipartFile profilePic) {
		Map<String, Object> map = new HashMap<>();
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
					map.put("Message", AppConstants.SUCCESS);
					map.put("StatusCode", 200);
				} else {
					map.put("Message", AppConstants.FAILED);
					map.put("StatusCode", 200);
				}
			} else {
				map.put("Message", AppConstants.FAILED);
				map.put("StatusCode", 200);
			}
		} else {
			map.put("Message", AppConstants.UNAUTHORIZED);
			map.put("StatusCode", 401);
		}
		return map;
	}

	@Override
	public Map<String, Object> getTodayAttendance(Integer studentId) {
		Map<String, Object> map = new HashMap<>();
		Attendance attendance = attendenceRepository.findByStudentIdAndCheckInDate(studentId, LocalDate.now());
		// String format =
		// attendance.getCheckInTime().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
		if (Objects.nonNull(attendance)) {
			map.put("Message", AppConstants.SUCCESS);
			map.put("Attendance", attendance);
			map.put("StatusCode", 200);
		}

		else {
			map.put("Message", NOT_CHECKED_IN);
			map.put("StatusCode", 400);
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
				map.put("Message", AppConstants.SUCCESS);
				map.put("StatusCode", 200);
				map.put("AttendanceData", findByStudentIdAndMonthNo);
			} else {
				map.put("Message", AppConstants.NO_DATA_FOUND);
				map.put("StatusCode", 404);
				map.put("AttendanceData", findByStudentIdAndMonthNo);
			}
		} else {
			map.put("Message", AppConstants.UNAUTHORIZED);
			map.put("StatusCode", 401);
		}
		return map;
	}

public Map<String, Object> getCalenderData(Integer id, Integer month, Integer year) {

		List<Integer> present = new ArrayList<>();
		List<Integer> leaves = new ArrayList<>();
		List<Integer> absent = new ArrayList<>();

		// Get the first day of the month
		LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

		// Get the last day of the month
		LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

		LocalDate currentDay = firstDayOfMonth;
		
		LocalDate lastDateOfLeave = LocalDate.of(year, month, 1);

		StudentCalenderResponse data = new StudentCalenderResponse();

		LocalDate currentDate = LocalDate.now();

		int currentMonthValue = currentDate.getMonthValue();

			
		// checking given month is not greather then current month
		if (month <= currentMonthValue && currentDate.getYear() == year) {


			//counting total leaves
		   List<Leaves> leavesData = this.leaveRepository.findAllByStudentId(id);
		   for (Leaves list : leavesData) {
			           
			       int dayOfMonth = list.getLeaveDate().getDayOfMonth();
			           currentDay = LocalDate.of(year, month, dayOfMonth);
			       int dayOfMonth2 = list.getLeaveEndDate().getDayOfMonth();
			       	   lastDateOfLeave  = LocalDate.of(year, month, dayOfMonth2);
			    
			       while (!currentDay.isAfter(lastDateOfLeave) && list.getLeaveDate().getMonth().getValue() ==month) {
						if (!present.contains(currentDay.getDayOfMonth())) {
							leaves.add(currentDay.getDayOfMonth());
						}
						currentDay = currentDay.plusDays(1);
					}
			}
		   currentDay = firstDayOfMonth;
		   
		   
		    // getting total present 
			List<Attendance> studentAttendenceList = attendenceRepository.findAllByStudentId(id);
			for (Attendance list : studentAttendenceList) {
				//tempCount++;
				LocalDate temp = list.getCheckInDate();
				if (temp.getMonth().getValue() == month && temp.getYear() == year) {
					present.add(temp.getDayOfMonth());
				}
			}
			 
		
			//getting total absent for current month and  till today date
			if(currentDate.getMonthValue() == month) {
				while (currentDay.getDayOfMonth() <= currentDate.getDayOfMonth()-1 && !currentDay.isAfter(lastDayOfMonth)) {
					if (!present.contains(currentDay.getDayOfMonth()-1) && currentDay.getDayOfWeek() != DayOfWeek.SUNDAY ) {
						absent.add(currentDay.getDayOfMonth());
					}
					currentDay = currentDay.plusDays(1);
				}
				
			}else {// getting absent for previous month from current month
				while (!currentDay.isAfter(lastDayOfMonth)) {
					if (!present.contains(currentDay.getDayOfMonth()-1) && currentDay.getDayOfWeek() != DayOfWeek.SUNDAY) {
						absent.add(currentDay.getDayOfMonth());
					}
					currentDay = currentDay.plusDays(1);
				}
			}
		}else {
			// return ????????
		}
		
		data.setPresent(present);
		data.setAbsent(absent);
		data.setLeaves(leaves);

		Map<String, Object> response = new HashMap<>();
		response.put("StudentCalenderData", data);

		return response;
	}

}
