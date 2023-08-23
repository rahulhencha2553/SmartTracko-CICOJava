package com.cico.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cico.model.Fees;
import com.cico.model.FeesPay;

public interface FeesPayRepository extends JpaRepository<FeesPay, Integer> {

	List<FeesPay> findByFees(Fees fees);
	
	@Query("SELECT f FROM FeesPay f WHERE f.feesPayAmount != 0")
	Page<FeesPay> findByFeesPayAmount(Pageable pageable);

}
