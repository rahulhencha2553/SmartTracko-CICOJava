package com.cico.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TaskQuestion {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int questionId;
	
	private String question;
	
	@ElementCollection
	@CollectionTable
	private List<String> questionImages;
	
	@ManyToOne
	private Task task;
	
	private String videoUrl;
}
