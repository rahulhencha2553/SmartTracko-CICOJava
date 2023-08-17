package com.cico.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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

	@ElementCollection
	@CollectionTable
	@NonNull
	private List<String> options;

	@JsonIgnore
	private String correctOption;
	private String selectedOption;

	@ManyToOne
	private Exam chapterExam;
	@ManyToOne
	private Chapter chapter;
	private String questionImage;
	private Boolean isDeleted= false;
	private Boolean isActive = true;
}
