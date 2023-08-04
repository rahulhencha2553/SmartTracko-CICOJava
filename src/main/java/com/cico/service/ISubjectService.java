
package com.cico.service;

import java.util.List;
import java.util.Map;

import com.cico.model.Subject;


public interface ISubjectService {

	void addSubject(String subjectName);

	void addChapterToSubject(Integer subjectId, String chapterName,String content);

	void updateSubject(Subject subject);

	Map<String,Object> getSubjectById(Integer subjectId);

	void deleteSubject(Integer subjectId);

	void updateSubjectStatus(Integer subjectId);

	List<Subject> getAllSubjects();

}
