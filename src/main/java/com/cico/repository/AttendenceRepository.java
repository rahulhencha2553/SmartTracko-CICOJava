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

	@Query("SELECT s.fullName, s.mobile ,s.profilePic ,s.applyForCourse , s.studentId FROM Student s WHERE  s.isCompleted = 0 AND  s.studentId  IN ("
			+ "SELECT a.studentId FROM Attendance a WHERE a.checkInDate = :currentDate)")
	public List<Object[]> getTodaysPresents(@Param("currentDate")LocalDate currentDate);

	
	@Query("SELECT s.fullName, s.mobile ,s.profilePic ,s.applyForCourse , s.studentId FROM Student s WHERE  s.isCompleted = 0 AND  s.studentId  IN ("
			+ "SELECT a.studentId FROM Attendance a WHERE DATE(a.checkInDate) = DATE(:currentDate) AND a.workingHour <=32400)")
	public List<Object[]> getTodaysEarlyCheckouts(@Param("currentDate")LocalDate currentDate);
}
