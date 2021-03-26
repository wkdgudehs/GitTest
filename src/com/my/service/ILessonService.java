package com.my.service;

import java.util.List;

import com.my.exception.FindException;
import com.my.vo.Lesson;

public interface ILessonService {
	List<Lesson> findAll() throws FindException;
	
	Lesson findById(int id) throws FindException;
	
	List<Lesson> findUnion(String union) throws FindException;
	
	int findAllCount() throws FindException;

	List<Lesson> findByPage(int currPage, int dataPerPage) throws FindException;
}
