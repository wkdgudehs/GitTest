package com.my.service;

import java.util.List;

import com.my.dao.LessonReviewDAO;
import com.my.dao.LessonReviewDAOOracle;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.vo.LessonReview;

public class LessonReviewService implements ILessonReviewService {
	LessonReviewDAO dao = new LessonReviewDAOOracle();
	@Override
	public List<LessonReview> findAll() throws FindException {
		return dao.selectAll();
	}
	
	@Override
	public LessonReview findByLPSId(int lpsId) throws FindException {
		return dao.selectById(lpsId);
	}

	@Override
	public void add(LessonReview review) throws AddException {
		System.out.println("service의 lps" + review.getLps());
		dao.insert(review);
	}

	@Override
	public int findAllCount() throws FindException {
		return dao.selectAllCount();
	}

	@Override
	public int findAllCount(int lessonId) throws FindException {
		return dao.selectAllCount(lessonId);
	}

	@Override
	public List<LessonReview> findByPage(int currPage, int dataPerPage, int lessonId) throws FindException {
		return dao.selectByPage(currPage, dataPerPage, lessonId);
	}
}