
package com.cico.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cico.model.Subject;
import com.cico.payload.SubjectResponse;


public interface ISubjectService {

	ResponseEntity<?> addSubject(String subjectName,Integer imageId);

	void addChapterToSubject(Integer subjectId, String chapterName);

	Subject updateSubject(Subject subject);

	Map<String,Object> getSubjectById(Integer subjectId);

	void deleteSubject(Integer subjectId);

	void updateSubjectStatus(Integer subjectId);

	List<SubjectResponse> getAllSubjects(Integer studentId);

	List<SubjectResponse> getAllSubjectsByCourseId(Integer courseId);

}
