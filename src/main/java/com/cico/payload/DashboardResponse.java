package com.cico.payload;

import java.time.LocalDate;
import java.time.LocalTime;

import com.cico.model.OrganizationInfo;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
	String attendanceStatus;
	LocalDate checkInDate;
	LocalTime checkInTime;
	String checkInImage;
	String checkOutImage;
	LocalTime checkOutTime;
	LocalDate checkOutDate;
    Boolean isFeesDue;
    LocalDate feesDueDate;
    Boolean isMispunch;
    Boolean isWebLoggedIn = false;
     MispunchResponse mispunchResponseDto;
    StudentResponse studentResponseDto;
    OrganizationInfo organizationInfo;
    Integer todayAllottedSeatNo;
}
