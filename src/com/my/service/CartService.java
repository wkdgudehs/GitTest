package com.my.service;

import java.util.List;

import com.my.dao.CartDAO;
import com.my.dao.CartDAOOracle;
import com.my.exception.AddException;
import com.my.exception.DeleteException;
import com.my.exception.FindException;
import com.my.vo.Cart;
import com.my.vo.Lesson;

public class CartService implements ICartService {
	CartDAO dao = new CartDAOOracle();
	
	@Override
	public List<Lesson> findById(int studentId) throws FindException {
		return null;
	}

	@Override
	public Cart findById(int lessonId, int studentId) throws FindException {
		return dao.selectById(lessonId, studentId);
	}
	
	@Override
	public void add(int lessonId, int studentId) throws AddException {
		dao.insert(lessonId, studentId);
	}

	@Override
	public Cart remove(int lessonId, int studentId) throws DeleteException {
		return dao.delete(lessonId, studentId);
	}

	@Override
	public int findAllCount() throws FindException {
		// TODO Auto-generated method stub
		return dao.selectAllCount();
	}

	@Override
	public int findAllCount(int studentId) throws FindException {
		return dao.selectAllCount(studentId);
		
	}

	@Override
	public List<Lesson> findByPage(int currPage, int dataPerPage, int studentId) throws FindException {
		// TODO Auto-generated method stub
		return dao.selectByPage(currPage, dataPerPage, studentId);
	}
}
