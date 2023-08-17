package com.cico.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cico.model.Fees;
import com.cico.model.Student;

public interface FeesRepository extends JpaRepository<Fees, Integer>{

	Fees findByStudent(Student findByStudentId);

	Page<Fees> findAllByIsCompleted(boolean b, Pageable pageable);

	@Query("SELECT f FROM Fees f WHERE f.student IN (SELECT s FROM Student s WHERE s.fullName LIKE %:fullName%)")
    List<Fees> findByStudentFullNameContaining(@Param("fullName") String fullName);

	@Query("SELECT f FROM Fees f WHERE f.date BETWEEN :startDate AND :endDate")
	List<Fees> findFeesByGivenDates(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

}
