package com.cico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cico.model.Assignment;

@Repository
public interface AssignmentRepository  extends JpaRepository<Assignment, Integer>{
 
}

