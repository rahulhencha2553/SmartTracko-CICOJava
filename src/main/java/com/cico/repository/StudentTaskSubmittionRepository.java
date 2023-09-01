package com.cico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cico.model.StudentTaskSubmittion;

@Repository
public interface StudentTaskSubmittionRepository  extends JpaRepository<StudentTaskSubmittion,Integer>{

}
