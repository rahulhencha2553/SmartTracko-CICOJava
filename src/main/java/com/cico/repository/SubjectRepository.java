package com.cico.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.cico.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

	Subject findBySubjectNameAndIsDeleted(String subjectName, boolean b);

	Optional<Subject> findBySubjectIdAndIsDeleted(Integer subjectId, boolean b);

	List<Subject> findByIsDeleted(boolean b);



}
