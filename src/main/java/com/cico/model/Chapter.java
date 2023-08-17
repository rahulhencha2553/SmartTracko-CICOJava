package com.cico.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
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
	@ManyToOne
	private Subject subject;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ChapterContent> chapterContent;
	@OneToOne(cascade = CascadeType.ALL)
	private Exam exam;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Question>questions = new ArrayList<>();
	private Boolean isDeleted = false;
	private Boolean isActive = true;

	private Boolean isCompleted;

}
