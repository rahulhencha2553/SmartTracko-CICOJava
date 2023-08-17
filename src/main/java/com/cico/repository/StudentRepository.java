package com.cico.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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

	@Query("SELECT s.fullName, s.mobile ,s.profilePic ,s.applyForCourse , s.studentId FROM Student s WHERE  s.isCompleted = 0 AND  s.studentId  NOT IN ("
			+ "SELECT a.studentId FROM Attendance a WHERE DATE(a.checkInDate) = DATE(:todaysdate))  ")
	List<Object[]> getTotalTodayAbsentStudent(@Param("todaysdate") LocalDate todaysdate);

	@Query("SELECT COUNT(A.studentId) FROM Attendance A WHERE A.checkInDate = :todaysdate")
	Long getTotalPresentToday(@Param("todaysdate") LocalDate todaysdate);

	@Query("SELECT l.studentId ,l.leaveDate,l.leaveEndDate  FROM Leaves l Where l.leaveStatus=1 AND CURRENT_DATE() BETWEEN DATE(l.leaveDate) AND DATE(l.leaveEndDate)  ")
	List<Object[]> getTotalStudentInLeaves();

	@Query("SELECT l.leaveDate, l.leaveEndDate, s.studentId, s.fullName, s.profilePic, s.applyForCourse, " +
		       "l.leaveTypeId, l.leaveDuration, l.leaveReason, l.leaveId, lt.leaveTypeName " +
		       "FROM Leaves l " +
		       "INNER JOIN Student s ON l.studentId = s.studentId " +
		       "INNER JOIN LeaveType lt ON l.leaveTypeId = lt.leaveTypeId " +
		       "WHERE l.leaveStatus = 0 AND CURRENT_DATE() < l.leaveDate")
		List<Object[]> getTotalTodaysLeavesRequest();


	Page<Student> findAllByIsCompletedAndIsActive(Boolean isCompleted, Boolean isActive, Pageable pageable);

	List<Student> findAllByFullNameContaining(String fullName);

	@Query("select count(s) from Student s where s.isCompleted=0")
	Long countTotalStudents();

	@Query("SELECT s.userId, s.fullName, s.profilePic, a.checkInDate, a.checkOutDate, a.checkInTime, a.checkOutTime, a.checkInImage, a.checkOutImage " +
		       "FROM Student s " +
		       "INNER JOIN Attendance a ON a.studentId = s.studentId " +
		       "WHERE s.isCompleted = 0 AND a.checkInDate = CURRENT_DATE "+
		       "ORDER BY a.workingHour DESC")
	List<Object[]> getStudentAttendanceDataForTv();
	
	@Query("SELECT MONTH(s.joinDate) AS month, COUNT(s.studentId) AS count FROM Student s "
			+ "WHERE YEAR(s.joinDate) = :year GROUP BY MONTH(s.joinDate)")
	List<Object[]> getMonthwiseAdmissionCountForYear(Integer year);

	
	@Query("SELECT MONTH(s.joinDate) AS month, COUNT(s.studentId) AS count FROM Student s "
			+ "WHERE YEAR(s.joinDate) = :year   GROUP BY MONTH(s.joinDate)")
	List<Object[]> getAbsent(Integer year);
   

//	   @Query("SELECT s FROM Student s ORDER BY s.someAttribute DESC")
//	    List<Student> findAllOrderedDesc();
	 List<Student> findAllByOrderByStudentIdDesc();
}
