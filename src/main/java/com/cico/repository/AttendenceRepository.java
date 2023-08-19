package com.cico.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import com.cico.model.Attendance;
import com.cico.payload.MonthWiseAttendanceDTO;

@Repository
public interface AttendenceRepository extends JpaRepository<Attendance, Integer> {

public Attendance findByStudentIdAndCheckInDate(Integer studentId,LocalDate date);
	
	@Query("SELECT a FROM Attendance a WHERE a.studentId=:studentId AND a.checkInDate<:currentDate AND a.checkOutDate is null ORDER BY checkInDate DESC")
	Attendance findByStudentIdAndCheckInDateLessThanCurrentDate(Integer studentId, LocalDate currentDate);

	public Attendance findByStudentIdAndCheckInDateAndCheckOutDate(Integer studentId, LocalDate date, LocalDate checkOutDate );
	
	@Query("SELECT a FROM Attendance a WHERE a.studentId=:studentId AND a.checkInDate between :startDate AND :endDate AND a.checkInTime IS NOT NULL AND a.checkOutTime IS NOT NULL")
	public Page<Attendance> findAttendanceHistory(Integer studentId, LocalDate startDate, LocalDate endDate, PageRequest of);
	
	@Query("SELECT a FROM Attendance a WHERE a.studentId=:studentId AND MONTH(a.checkInDate)=:monthNo AND a.checkOutTime IS NOT NULL")
	public List<Attendance> findByStudentIdAndMonthNo(@Param("studentId") Integer studentId,@Param("monthNo") Integer monthNo);
	
	@Query("SELECT a FROM Attendance a WHERE a.studentId=:id And a.checkInTime IS NOT NULL And a.checkOutTime IS NOT NULL")
	public List<Attendance>findAllByStudentId(@Param("id") Integer id);


//	@Query("SELECT s.fullName, s.mobile ,s.profilePic ,s.applyForCourse , s.studentId FROM Student s WHERE  s.isCompleted = 0 AND  s.studentId  IN ("
//			+ "SELECT a.studentId FROM Attendance a WHERE a.checkInDate = :currentDate)")
	
	@Query("SELECT s.fullName, s.mobile, s.profilePic, s.applyForCourse, s.studentId, a.checkInTime "
	        + "FROM Student s "
	        + "INNER JOIN Attendance a ON a.studentId = s.studentId "
	        + "WHERE s.isCompleted = 0 "
	        + "AND a.checkInDate = :currentDate")
	public List<Object[]> getTodaysPresents(@Param("currentDate")LocalDate currentDate);

	
	@Query("SELECT s.fullName, s.mobile, s.profilePic, s.applyForCourse, s.studentId, a.checkOutTime "
	        + "FROM Student s "
	        + "INNER JOIN Attendance a ON a.studentId = s.studentId "
	        + "WHERE s.isCompleted = 0 "
	        + "AND a.checkInDate = :currentDate "
	        + "AND a.workingHour < 32400")
	public List<Object[]> getTodaysEarlyCheckouts(@Param("currentDate")LocalDate currentDate);

	@Query("SELECT COUNT(a) FROM Attendance a WHERE MONTH(a.checkInDate) = :month")
	public Long countPresentStudentsByMonth(@Param("month") Integer month);

	@Query("SELECT MONTH(a.checkInDate) AS month, COUNT(a.attendanceId) AS count FROM Attendance a "
			+ "WHERE YEAR(a.checkInDate) = :year AND a.studentId=:studentId GROUP BY MONTH(a.checkInDate)")
	List<Object[]> getMonthWisePresentForYear(@Param("year") Integer year,@Param("studentId") Integer studentId);

	
	@Transactional
	@Modifying
	@Query("DELETE FROM Attendance a WHERE a.studentId=:id AND a.checkInDate=:now")
	public void deleteAttendanceToday(@Param("id") Integer id, @Param("now") LocalDate now);
	
	//@Query("SELECT MONTH(a.checkInDate) AS month, COUNT(a.attendanceId) AS count FROM Attendance a "
//			+ "WHERE MONTH(a.checkInDate) = :year AND a.studentId=:studentId GROUP BY MONTH(a.checkInDate)")
//	 @Query("SELECT s.fullName, s.mobile ,s.profilePic ,s.applyForCourse , s.studentId FROM Student s WHERE  s.isCompleted = 0 AND  s.studentId  NOT IN ("
//			+ "SELECT a.studentId FROM Attendance a WHERE DATE(a.checkInDate) = DATE(:todaysdate))  ")
	
//	
//	@Query("SELECT NEW com.cico.payload.MonthWiseAttendanceDTO(MONTH(a.checkInDate), COUNT(a.attendanceId)) "
//	        + "FROM Attendance a WHERE YEAR(a.checkInDate) = :year AND a.studentId = :studentId GROUP BY MONTH(a.checkInDate)")
//	List<MonthWiseAttendanceDTO> getMonthWisePresentForYear(@Param("year") Integer year, @Param("studentId") Integer studentId);

}















