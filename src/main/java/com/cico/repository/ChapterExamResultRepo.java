package com.cico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cico.model.Chapter;
import com.cico.model.ChapterExamResult;
import com.cico.model.Student;

public interface ChapterExamResultRepo extends JpaRepository<ChapterExamResult, Integer>{

	Optional<ChapterExamResult> findByChapterAndStudent(Chapter chapter, Student student);

}
