package com.cico.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")

public class Student {
	@Id
	private Integer studentId;
	private String userId;
	private String fullName;
	private String mobile;
	private LocalDate dob;
	private String email;
	private String password;
	private String college;
	private LocalDate joinDate;
	private String profilePic;
	private LocalDateTime createdDate;
	private String applyForCourse;
	private String currentSem;
	private String fathersName;
	private String mothersName;
	private String fathersOccupation;
	private String contactFather;
	private String cotactMother;
	private String localAddress;
	private String parmanentAddress;
	private String languageKnown;
	private String currentCourse;
	private String fcmId;
	private String deviceId;
	private String inUseDeviceId;
	private String isDeviceApproved;
	private String deviceType;
	private Boolean isFromEnquiry;
	private Boolean isConverted;
	private Boolean isCompleted;
	private Boolean isActive;
	private String role;
}
