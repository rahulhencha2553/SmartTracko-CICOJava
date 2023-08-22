package com.cico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cico.model.Fees;
import com.cico.model.FeesPay;

public interface FeesPayRepository extends JpaRepository<FeesPay, Integer> {

	List<FeesPay> findByFees(Fees fees);

}
