package com.my.service;

import java.util.List;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.vo.LessonReview;

public interface ILessonReviewService {
	List<LessonReview> findAll() throws FindException;
	
	LessonReview findByLPSId(int lpsId) throws FindException;
	
	void add(LessonReview review) throws AddException;
	
	int findAllCount() throws FindException;

	int findAllCount(int lessonId) throws FindException;
	
	List<LessonReview> findByPage(int currPage, int dataPerPage, int lessonId) throws FindException;
}