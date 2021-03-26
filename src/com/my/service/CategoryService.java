package com.my.service;

import java.util.List;

import com.my.dao.CategoryDAO;
import com.my.dao.CategoryDAOOracle;
import com.my.exception.FindException;
import com.my.vo.Category;
import com.my.vo.Lesson;

public class CategoryService implements ICategoryService {
	CategoryDAO dao = new CategoryDAOOracle();
	@Override
	public List<Category> findAll() throws FindException {
		return dao.selectAll();
	}

	@Override
	public List<Lesson> findById(int categoryId) throws FindException {
		return dao.selectById(categoryId);
	}
}
