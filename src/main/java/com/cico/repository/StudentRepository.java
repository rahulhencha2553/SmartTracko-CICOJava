package com.cico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
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
	
}
