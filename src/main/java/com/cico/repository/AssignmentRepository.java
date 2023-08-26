package com.cico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cico.model.Assignment;
import com.cico.model.Course;

@Repository
public interface AssignmentRepository  extends JpaRepository<Assignment, Long>{

	Optional<Assignment> findByIdAndIsActive(Long id, boolean b);
 
}

