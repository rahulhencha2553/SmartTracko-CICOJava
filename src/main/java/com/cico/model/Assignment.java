package com.cico.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assignments")
public class Assignment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(columnDefinition = "longtext")
	private String question;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<AssignmentImage> assignmentImages = new ArrayList<>();
	private LocalDateTime createdTime;
	private LocalDateTime upatedDate;
	private Boolean isDeleted;
	private  Boolean isCompleted;
	@ElementCollection
    @CollectionTable
    private List<String> hints = new ArrayList<>();

}

