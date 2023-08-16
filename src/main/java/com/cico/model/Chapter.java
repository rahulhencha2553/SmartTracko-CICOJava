package com.cico.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Chapter {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer chapterId;
	
	@Column(unique = true)
	@NonNull
	private String chapterName;
	private String chapterScore;
	//private String chapterImage;
	
	
	
	@Column(columnDefinition = "longtext")
	@NonNull
	private String content;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Exam> exams;
	
	private Boolean isDeleted=false;
	private Boolean isActive=true;
	
	private Boolean isCompleted;
	
	

}
