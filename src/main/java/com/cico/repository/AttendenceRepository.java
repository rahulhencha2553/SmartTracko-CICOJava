package com.cico.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cico.model.Attendance;

@Repository
public interface AttendenceRepository extends JpaRepository<Attendance, Integer> {

public Attendance findByStudentIdAndCheckInDate(Integer studentId,LocalDate date);
	
	@Query("SELECT a FROM Attendance a WHERE a.studentId=:studentId AND a.checkInDate<:currentDate AND a.checkOutDate is null ORDER BY checkInDate DESC")
	Attendance findByStudentIdAndCheckInDateLessThanCurrentDate(Integer studentId, LocalDate currentDate);

	public Attendance findByStudentIdAndCheckInDateAndCheckOutDate(Integer studentId, LocalDate date, LocalDate checkOutDate );
	
	@Query("SELECT a FROM Attendance a WHERE a.studentId=:studentId AND a.checkInDate between :startDate AND :endDate AND a.checkInTime IS NOT NULL AND a.checkOutTime IS NOT NULL")
	public Page<Attendance> findAttendanceHistory(Integer studentId, LocalDate startDate, LocalDate endDate, PageRequest of);
	
	@Query("SELECT a FROM Attendance a WHERE a.studentId=:studentId AND MONTH(a.checkInDate)=:monthNo")
	public List<Attendance> findByStudentIdAndMonthNo(@Param("studentId") Integer studentId,@Param("monthNo") Integer monthNo);
	
	public List<Attendance>findAllByStudentId(Integer id);

	@Query("SELECT COUNT(a) FROM Attendance a WHERE MONTH(a.checkInDate) = :month")
	public Long countPresentStudentsByMonth(@Param("month") Integer month);

	@Query("SELECT COUNT(l) FROM Leaves l WHERE MONTH(l.leaveDate) = :month and DAY(l.leaveDate)!=7 and l.leaveDayType='Full Day'")
	public Long countLeaveStudentsByMonth(@Param("month") Integer month);
	
	
}
