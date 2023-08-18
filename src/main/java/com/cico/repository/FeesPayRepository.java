package com.cico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cico.model.Fees;
import com.cico.model.FeesPay;

public interface FeesPayRepository extends JpaRepository<FeesPay, Integer> {

	FeesPay findByFees(Fees findByFeesId);

//	FeesPay findByFees(Fees findByFeesId);

}
