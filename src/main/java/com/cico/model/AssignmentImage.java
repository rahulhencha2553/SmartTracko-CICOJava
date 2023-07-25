package com.cico.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assignment_images")
public class AssignmentImage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String imageName;

}
