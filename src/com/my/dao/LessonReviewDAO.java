package com.my.dao;

import java.util.List;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.vo.LPS;
import com.my.vo.LessonReview;

public interface LessonReviewDAO {
	/**
	 * 한 프로젝트의 수강 후기를 전부 불러온다.
	 * @return List<LessonReview> 수강후기 전체 리스트를 불러온다.
	 * @throws FindException 수강후기가 없을 경우에 예외 발생
	 */
	public List<LessonReview> selectAll() throws FindException;
	
	public LessonReview selectById(int lpsId) throws FindException;
	/**
	 * 한 프로젝트에 수강후기를 추가한다. 
	 * @param review 수강 후기 
	 * @throws AddException 이미 수강후기를 남겼을 경우, 예외 발생 (중복으로 남길 수 없다.)
	 */
	public void insert(LessonReview review) throws AddException;
	
	//데이터 전체 개수 세기
	int selectAllCount() throws FindException;
	
	public int selectAllCount(int lessonId) throws FindException;

	//특정 페이지에 해당하는 공지사항 게시물 조회
	public List<LessonReview> selectByPage(int currPage, int dataPerPage) throws FindException;
	
	public List<LessonReview> selectByPage(int currPage, int dataPerPage, int lessonId) throws FindException;
}