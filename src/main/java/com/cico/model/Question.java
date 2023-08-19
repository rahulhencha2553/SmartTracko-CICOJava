package com.cico.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer questionId;

	@Column(unique = true)
	@NonNull
	private String questionContent;

	private String option1;
	private String option2;
	private String option3;
	private String option4;
	
	private String correctOption;
	private String selectedOption;

	@ManyToOne
	private Exam chapterExam;
	@ManyToOne
	private Chapter chapter;
	private String questionImage;
	private Boolean isDeleted = false;
	private Boolean isActive = true;
}
