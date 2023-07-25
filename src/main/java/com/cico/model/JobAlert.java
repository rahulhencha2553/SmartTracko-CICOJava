package com.cico.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_alert")
public class JobAlert {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer jobId;
	private String jobTitle;
	private String jobDescription;
	private String companyName;
	private String experienceRequired;
	private String technicalSkills;
	private Boolean isActive;
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private  TechnologyStack technologyStack;
	private String type;
	private String jobPackage;
	private Boolean isDeleted;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	public JobAlert(String jobTitle, String jobDescription, String companyName, String experienceRequired,
			String technicalSkills, Boolean isActive, String type, String jobPackage) {
		super();
		this.jobTitle = jobTitle;
		this.jobDescription = jobDescription;
		this.companyName = companyName;
		this.experienceRequired = experienceRequired;
		this.technicalSkills = technicalSkills;
		this.isActive = isActive;
		this.type = type;
		this.jobPackage = jobPackage;
	}
	
	
}
