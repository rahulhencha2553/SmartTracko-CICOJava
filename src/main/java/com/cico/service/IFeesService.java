package com.cico.service;

import com.cico.model.Fees;

public interface IFeesService {

	

	public Fees createStudentFees(Integer studentId, Integer courseId, Double finalFees, String date);

}
