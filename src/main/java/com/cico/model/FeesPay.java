package com.cico.model;

import java.time.LocalDate;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "feespay")
public class FeesPay {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer payId;
	private Integer feesId;
	private Double feesPay;
	private LocalDate payDate;
	private String recieptNo;
	private String description;
	private LocalDate createDate;
	private LocalDate updatedDate;
	
}
