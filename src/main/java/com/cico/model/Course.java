package com.cico.model;

import java.time.LocalDate;

import javax.persistence.Entity;
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
@Table(name = "courses")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer courseId;
	private String courseName;
	private String courseFees;
	private String duration;
	private String sortDescription;
	private LocalDate createdDate;
	private LocalDate updatedDate;
    private Boolean isDeleted=false;
	
    @OneToOne
    private TechnologyStack technologyStack;
    
	public Course(String courseName, String courseFees, String duration, String sortDescription,
			TechnologyStack technologyStack) {
		super();
		this.courseName = courseName;
		this.courseFees = courseFees;
		this.duration = duration;
		this.sortDescription = sortDescription;
		
		this.technologyStack = technologyStack;
	}
	
}
