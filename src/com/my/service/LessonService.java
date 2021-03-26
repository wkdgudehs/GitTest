package com.my.service;

import java.util.List;

import com.my.dao.LessonDAO;
import com.my.dao.LessonDAOOracle;
import com.my.exception.FindException;
import com.my.vo.Lesson;

public class LessonService implements ILessonService {
	LessonDAO dao = new LessonDAOOracle();

	@Override
	public List<Lesson> findAll() throws FindException {
		List<Lesson> lAll = dao.selectAll();
		return lAll;
	}

	@Override
	public Lesson findById(int lessonId) throws FindException {
		return dao.selectById(lessonId);
	}

	@Override
	public List<Lesson> findUnion(String union) throws FindException {
		return dao.selectByUnion(union);
	}

	@Override
	public int findAllCount() throws FindException {
		return dao.selectAllCount();
	}

	@Override
	public List<Lesson> findByPage(int currPage, int dataPerPage) throws FindException {
		return dao.selectByPage(currPage, dataPerPage);
	}
}
