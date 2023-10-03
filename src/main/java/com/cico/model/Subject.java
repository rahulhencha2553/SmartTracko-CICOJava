package com.cico.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer subjectId;

	@Column(unique = true)
	private String subjectName;
    @JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL)
	private List<Chapter> chapters = new ArrayList<>();

	@OneToOne
	private TechnologyStack technologyStack; // profile picture of subject
	@OneToOne
	private Exam exam;
	private Boolean isDeleted = false;
	private Boolean isActive = true;

}
