package com.cico.payload;

import java.time.LocalDate;

import com.cico.model.Fees;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeesPayResponse {

	private Integer payId;
	private Double feesPayAmount;
	private LocalDate payDate;
	private String recieptNo;
	private String description;
	private LocalDate createDate;
	private LocalDate updatedDate;
}
