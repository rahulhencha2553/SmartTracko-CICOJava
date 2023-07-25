package com.cico.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.cico.exception.ResourceNotFoundException;
import com.cico.model.LeaveType;
import com.cico.model.Leaves;
import com.cico.model.Student;
import com.cico.payload.PageResponse;
import com.cico.repository.LeaveRepository;
import com.cico.repository.LeaveTypeRepository;
import com.cico.repository.StudentRepository;
import com.cico.security.JwtUtil;
import com.cico.service.ILeaveService;
import com.cico.util.AppConstants;


@Service
public class LeaveServiceImpl implements ILeaveService{
	
	public static final String INVALID_DATE = "INVALID_DATE";
	public static final String INVALID_ID = "INVALID_ID";

	@Autowired
	private LeaveTypeRepository leaveTypeRepository;

	@Autowired
	private StudentRepository studRepo;

	@Autowired
	private JwtUtil util;

	@Autowired
	private LeaveRepository leavesRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Map<String, Object> addStudentLeave(HttpHeaders header, Integer leaveTypeId, String leaveStartDate,
			String leaveEndDate, String leaveReason, String leaveDayType, String halfDayType) {
		Map<String, Object> map = new HashMap<>();
		long dateDiff = 0;
		int i = 0;
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());
		LocalDate toDate = null;
		LocalDate fromDate = null;

		if (leaveStartDate != null && !leaveStartDate.equals("")) {
			toDate = LocalDate.parse(leaveStartDate);
		} else {
			toDate = LocalDate.now();
		}

		if (leaveEndDate != null && !leaveEndDate.equals("")) {
			fromDate = LocalDate.parse(leaveEndDate);
		} else {
			fromDate = toDate;
		}

		if (validateToken) {
			System.out.println(validateToken);
			if (leaveTypeId != null && leaveStartDate != null && leaveReason != null
					&& (leaveDayType.equals("Full Day") || leaveDayType.equals("Half Day"))) {
				Leaves leavesData = new Leaves(studentId, leaveTypeId, leaveReason, leaveDayType);
				if (leaveDayType.equals("Half Day")) {
					if (halfDayType.equals("First Half") || halfDayType.equals("Second Half")) {
						leavesData.setHalfDayType(halfDayType);
						leavesData.setCreatedDate(LocalDateTime.now());
						leavesData.setLeaveDate(toDate);
						leavesData.setLeaveEndDate(toDate);
						leavesData.setLeaveDuration(0);

						map.put("Message", AppConstants.SUCCESS);
					} else {
						map.put("Message", AppConstants.ALL_REQUIRED);
					}
				}
				if (leaveDayType.equals("Full Day")) {
					if (Objects.nonNull(leaveEndDate)) {
						System.out.println(leaveEndDate);
						System.out.println(leaveStartDate);
						leavesData.setLeaveDate(toDate);
						leavesData.setLeaveEndDate(fromDate);
						dateDiff = (int) ChronoUnit.DAYS.between(toDate, fromDate);
						leavesData.setCreatedDate(LocalDateTime.now());
						leavesData.setLeaveDuration((int) dateDiff);
						System.out.println(dateDiff);
					} else {
						map.put("Message", AppConstants.ALL_REQUIRED);
					}
				}
				if (!leaveDayType.isEmpty()) {
					if (dateDiff >= 0) {
						for (i = 0; i <= dateDiff; i++) {
							if (i == 0) {
								leavesData.setLeaveDate(toDate);

								leavesData.setLeaveEndDate(fromDate);
							} else {

								leavesData.setLeaveDate(toDate);
								leavesData.setCreatedDate(LocalDateTime.now());
							}
							leavesRepository.save(leavesData);
						}
						if (Objects.nonNull(leavesData)) {
							map.put("Message", AppConstants.SUCCESS);
							map.put("LeavesData", leavesData);
						}
						else {
							map.put("Message", AppConstants.FAILED);
						}
					} else {
						map.put("Message", INVALID_DATE);
					}
				} else {
					map.put("Message", "error");
				}
			} else {
				map.put("Message", AppConstants.ALL_REQUIRED);
			}
		}
		return map;
	}

	@Override
	public PageResponse<Leaves> getStudentLeaves(HttpHeaders header, Integer page, Integer size) {
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());

		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), username);
		Map<String, Object> map = new HashMap<>();

		if (validateToken) {
			Page<Leaves> StudentLeaves = leavesRepository.findStudentLeaves(studentId,
					PageRequest.of(page, size,Sort.by(Direction.DESC, "leaveId")));

			int totalLeaves = leavesRepository.countByStudentId(studentId);
			if (Objects.nonNull(StudentLeaves)) {
				return new PageResponse<>(StudentLeaves.getContent(), StudentLeaves.getNumber(), StudentLeaves.getSize(), StudentLeaves.getTotalElements(), StudentLeaves.getTotalPages(),StudentLeaves.isLast());
			} else {
				
				throw new ResourceNotFoundException(AppConstants.NO_DATA_FOUND);
			}
		}
		return null;
	}

	@Override
	public Map<String, Object> deleteStudentLeave(HttpHeaders header, Integer leaveId) {
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());

		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), username);
		Map<String, Object> map = new HashMap<>();

		if (validateToken) {
			Leaves findByStudentIdAndLeaveId = leavesRepository.findByStudentIdAndLeaveId(studentId, leaveId);
			if (findByStudentIdAndLeaveId != null) {
				if (findByStudentIdAndLeaveId.getLeaveStatus() == 0) {
					int deleteByStudnetIdLeaveId = leavesRepository.deleteByStudnetIdLeaveId(studentId, leaveId);
					if (Objects.nonNull(deleteByStudnetIdLeaveId)) {
						map.put("Message", AppConstants.SUCCESS);

					} else {
						map.put("Message", AppConstants.FAILED);
					}
				} else {
					map.put("Message", "LEAVE_APPROVE_REJECT");
				}
			} else {
				map.put("Message", INVALID_ID);
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> retractStudentLeave(HttpHeaders header, Integer leaveId) {
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(util.getHeader(header.getFirst(AppConstants.AUTHORIZATION),AppConstants.STUDENT_ID).toString());

		boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), username);
		Map<String, Object> map = new HashMap<>();

		if (validateToken) {
			Leaves findByStudentIdAndLeaveId = leavesRepository.findByStudentIdAndLeaveId(studentId, leaveId);
			if (findByStudentIdAndLeaveId != null) {
				if (findByStudentIdAndLeaveId.getLeaveStatus() == 1) {
					if (findByStudentIdAndLeaveId.getRetractLeave() == 0) {
						int deleteByStudnetIdLeaveId = leavesRepository.deleteByStudnetIdLeaveIdStudentId(studentId,
								leaveId);
						if (Objects.nonNull(deleteByStudnetIdLeaveId)) {
							map.put("Message", AppConstants.SUCCESS);
						} else {
							map.put("Message", AppConstants.FAILED);
						}
					} else {
						map.put("Message", "ALREADY_RETRACT_LEAVE");
					}
				} else {
					map.put("Message", "LEAVE_PENDING");
				}
			} else {
				map.put("Message", INVALID_ID);
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> studentLeaveMonthFilter(HttpHeaders header, Integer monthNo) {
		String username = util.getUsername(header.getFirst(AppConstants.AUTHORIZATION));
		Integer studentId = Integer.parseInt(util.getHeader(header.getFirst(AppConstants.AUTHORIZATION), AppConstants.STUDENT_ID).toString());
		Student student = studRepo.findByUserIdAndIsActive(username, true).get();
		Boolean validateToken = util.validateToken(header.getFirst(AppConstants.AUTHORIZATION), student.getUserId());

		Map<String, Object> map = new HashMap<>();

		if (validateToken) {
			List<Leaves> findByStudentIdAndMonthNo = leavesRepository.findByStudentIdAndMonthNo(studentId, monthNo);
			if (Objects.nonNull(findByStudentIdAndMonthNo)) {
				map.put("Message", AppConstants.SUCCESS);
				map.put("LeaveData", findByStudentIdAndMonthNo);
			} else {
				map.put("Message", AppConstants.NO_DATA_FOUND);
				map.put("LeaveData", findByStudentIdAndMonthNo);

			}
		}else {
			map.put("Message",AppConstants.UNAUTHORIZED);
		}
		return map;
	}

	@Override
	public List<LeaveType> getAllLeavesType() {
		List<LeaveType> leaveTypeList = leaveTypeRepository.findByIsActiveAndIsDelete(true, false);
		return leaveTypeList;
	}

}
