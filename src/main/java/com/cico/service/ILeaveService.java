package com.cico.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import com.cico.model.LeaveType;
import com.cico.model.Leaves;
import com.cico.payload.PageResponse;

public interface ILeaveService {

	public List<LeaveType> getAllLeavesType();
	
	public Map<String, Object> addStudentLeave(HttpHeaders header, Integer leaveTypeId, String leaveStartDate, String leaveEndDate,
			String leaveReason, String leaveDayType, String halfDayType);

	public PageResponse<Leaves> getStudentLeaves(HttpHeaders header,Integer page, Integer size);

	public Map<String, Object> deleteStudentLeave(HttpHeaders header, Integer leaveId);

	public Map<String, Object> retractStudentLeave(HttpHeaders header, Integer leaveId);

	public Map<String, Object> studentLeaveMonthFilter(HttpHeaders header, Integer monthNo);

}
