package com.cico.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cico.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	Optional<Student> findByUserIdAndIsActive(String username, boolean b);

	Student findByInUseDeviceId(String deviceId);

	Student findByUserId(String userId);

	Student findByStudentId(Integer studentId);

	Optional<Student> findByEmail(String email);

	Student findByinUseDeviceId(String deviceId);

	@Query("SELECT s.fullName, s.mobile ,s.profilePic ,s.currentCourse FROM Student s WHERE   s.studentId NOT IN ("
			+ "SELECT a.studentId FROM Attendance a WHERE DATE(a.checkInDate) = DATE(:todaysdate))")
	List<Object[]> getTotalTodayAbsentStudent(@Param("todaysdate") LocalDate todaysdate);

	@Query("SELECT l.studentId ,l.leaveDate,l.leaveEndDate  FROM Leaves l Where l.leaveStatus=1 AND CURRENT_DATE() BETWEEN l.createdDate AND l.leaveDate")
	List<Object[]> getTotalStudentInLeaves();

//	@Query("SELECT l.leaveDate , l.leaveEndDate , l.studentId , l.leaveTypeId , l.leaveReason FROM Leaves l WHERE  l.leaveStatus = 0  AND CURRENT_DATE() BETWEEN l.createdDate AND l.leaveDate")
//	List<Object[]> getTotalTodaysLeavesRequest();

//	@Query("SELECT l.leaveDate, l.leaveEndDate, s.studentId, l.leaveTypeId, l.leaveReason " +
//		       "FROM Leaves l " +
//		       "INNER JOIN Student s ON l.studentId = s.studentId " +
//		       "WHERE l.leaveStatus = 0 AND CURRENT_DATE() BETWEEN l.createdDate AND l.leaveDate")
//		List<Object[]> getTotalTodaysLeavesRequest();

	@Query("SELECT l.leaveDate, l.leaveEndDate, s.studentId, s.fullName, s.profilePic ,s.applyForCourse, l.leaveTypeId,l.leaveDuration, l.leaveReason\r\n"
			+ "FROM Leaves l\r\n" + "INNER JOIN Student s ON l.studentId = s.studentId\r\n"
			+ "WHERE l.leaveStatus = 0 AND CURRENT_DATE() BETWEEN l.createdDate AND l.leaveDate \r\n" + "")
	List<Object[]> getTotalTodaysLeavesRequest();
}
