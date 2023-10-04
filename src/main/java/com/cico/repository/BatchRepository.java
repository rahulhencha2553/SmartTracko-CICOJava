package com.cico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cico.model.Batch;
import com.cico.model.Course;



@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {

	@Query("SELECT b FROM Batch b WHERE b.batchStartDate > :currentDate")
	List<Batch> findAllByBatchStartDate();

	List<Batch> findAllByIsDeleted(Boolean b);

	List<Batch> findByBatchIdAndIsDeleted(Integer batchId, boolean b);

}

