package com.cico.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Batch {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int batchId;
	private String batchName;
	private LocalDate batchStartDate;
	private LocalDate batchEndDate;
	
	@ManyToOne
	private TechnologyStack technologyStack;
	
	private boolean isDeleted;
	private boolean isActive=true;
	public Batch(String batchName, LocalDate batchStartDate, LocalDate batchEndDate, TechnologyStack technologyStack) {
		super();
		this.batchName = batchName;
		this.batchStartDate = batchStartDate;
		this.batchEndDate = batchEndDate;
		this.technologyStack = technologyStack;
	}

}
