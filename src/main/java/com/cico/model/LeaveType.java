package com.cico.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "leave_type")
@AllArgsConstructor
@NoArgsConstructor
public class LeaveType {
	 @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private Integer leaveTypeId;
	  private String leaveTypeName;
	  private Boolean isActive;
	  private Boolean isDelete; 
	  @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	  private LocalDateTime createdDate;
}

