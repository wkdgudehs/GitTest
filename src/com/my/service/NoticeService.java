package com.my.service;

import java.util.List;

import com.my.dao.NoticeDAO;
import com.my.dao.NoticeDAOOracle;
import com.my.exception.FindException;
import com.my.vo.Notice;

public class NoticeService implements INoticeService{
	
	NoticeDAO dao = new NoticeDAOOracle();
	@Override
	public List<Notice> findAll() throws FindException {
		return dao.selectAll();
		
	}

	@Override
	public Notice findById(int NoticeId) throws FindException {
		return dao.selectById(NoticeId);
		
	}

	@Override
	public Notice findtByName(String NoticeName) throws FindException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int findAllCount() throws FindException{
		return dao.selectAllCount();
	}
	
	@Override
	public List<Notice> findByPage(int currPage, int dataPerPage) throws FindException{
		return dao.selectByPage(currPage, dataPerPage);
	}
}