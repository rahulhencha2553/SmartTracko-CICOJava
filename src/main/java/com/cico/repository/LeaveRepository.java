package com.cico.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cico.model.Leaves;

@Repository
public interface LeaveRepository extends JpaRepository<Leaves, Integer> {
	@Query("SELECT l FROM Leaves l WHERE l.studentId=:studentId")
	public Page<Leaves> findStudentLeaves(Integer studentId, PageRequest of);

	@Query("SELECT COUNT(l) FROM Leaves l WHERE l.studentId=:studentId")
	public int countByStudentId(Integer studentId);

	public Leaves findByStudentIdAndLeaveId(Integer studentId, Integer leaveId);

	@Transactional
	@Modifying
	@Query("delete from Leaves l  where l.studentId=:studentId And l.leaveId=:leaveId")
	public int deleteByStudnetIdLeaveId(@Param("studentId") Integer studentId, @Param("leaveId") Integer leaveId);

	@Transactional
	@Modifying
	@Query("UPDATE  Leaves l SET l.retractLeave=1 where l.studentId=:studentId And l.leaveId=:leaveId")
	public int deleteByStudnetIdLeaveIdStudentId(@Param("studentId") Integer studentId,
			@Param("leaveId") Integer leaveId);

	@Query("SELECT l FROM Leaves l WHERE l.studentId=:studentId AND MONTH(l.leaveDate)=:monthNo")
	public List<Leaves> findByStudentIdAndMonthNo(@Param("studentId") Integer studentId,
			@Param("monthNo") Integer monthNo);
	
	@Query("SELECT u FROM Leaves u where u.studentId =:id And u.leaveStatus = 1 ")
	public List<Leaves> findAllByStudentId(@Param("id") Integer id);
}
