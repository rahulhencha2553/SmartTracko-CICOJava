package com.cico.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cico.model.Fees;
import com.cico.model.Student;

public interface FeesRepository extends JpaRepository<Fees, Integer> {

	Fees findByStudent(Student findByStudentId);

	Page<Fees> findAllByIsCompleted(boolean b, Pageable pageable);

	@Query("SELECT f FROM Fees f WHERE f.student IN (SELECT s FROM Student s WHERE s.fullName LIKE %:fullName%)")
	List<Fees> findByStudentFullNameContaining(@Param("fullName") String fullName);

	@Query("SELECT f FROM Fees f WHERE f.date BETWEEN :startDate AND :endDate")
	List<Fees> findFeesByGivenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	Fees findByFeesId(Integer feesId);

	@Transactional
	@Modifying
	@Query("UPDATE Fees f SET f.remainingFees =:newRemainingFees where f.feesId =:feesId  ")
	Fees updateRemainingFees(@Param("newRemainingFees") Double newRemainingFees, @Param("feesId") Integer feesId);

	@Query("SELECT f FROM Fees f WHERE f.remainingFees != 0")
	Page<Fees> findByRemainingFees(Pageable pageable);

	@Transactional
	@Modifying
	@Query("UPDATE Fees f SET f.isCompleted =1 where f.feesId =:feesId  ")
	public int updateIsCompleted(@Param("feesId") Integer feesId);

	@Query("SELECT MONTH(f.date) AS month, SUM(f.feesPaid) AS totalPaid " +
		       "FROM Fees f " +
		       "WHERE YEAR(f.date) = :year " +
		       "GROUP BY MONTH(f.date) ")
		List<Object[]> getTotalFeesPaidByMonth(@Param("year") int year);

  
		@Query("SELECT SUM(f.finalFees) AS totalfees, SUM(f.remainingFees) AS pending, SUM(f.feesPaid) AS collected FROM Fees f")
		public List<Object[]> getTotalFeeCollection();

}
