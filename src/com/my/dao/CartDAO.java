package com.my.dao;

import java.util.List;

import com.my.exception.AddException;
import com.my.exception.DeleteException;
import com.my.exception.FindException;
import com.my.vo.Cart;
import com.my.vo.Lesson;
import com.my.vo.Student;

public interface CartDAO {
	/**
	 * 마이페이지 - 내가 좋아한 강좌의 전체 강좌 리스트를 불러온다.
	 * @param studentId 마이페이지에 진입한 유저
	 * @return List<Lesson> 좋아한 강좌 리스트
	 * @throws FindException 좋아한 강좌가 없을 경우에 예외 발생
	 */
	public List<Lesson> selectById(int studentId) throws FindException;
	/**
	 * 로그인한 학생이 선택한 강좌의 좋아요를 눌렀는지 확인한다
	 * @param lessonId, studentId
	 * @return Cart 좋아요 객체
	 * @throws FindException 좋아한 강좌가 없을 경우에 예외가 발생한다
	 */
	public Cart selectById(int lessonId, int studentId) throws FindException;
	/**
	 * 유저가 프로젝트에 좋아요를 누르면, 마이페이지 - 내가 좋아한 강좌에 프로젝트가 추가된다.
	 * @param studentId, lessonId 좋아요를 누를 때 선택된 강좌ID 와 로그인 된 수강생ID
	 * @throws AddException 이미 좋아요를 누른 강좌를 다시 누를 경우 예외 발생 
	 */
	public void insert(int lessonId, int studentId) throws AddException;
	/**
	 * 좋아요를 누른 프로젝트를 취소한다. 
	 * @param lessonId, studentId 좋아요 취소를 누른 강좌와 로그인한 수강생ID 
	 * @return Cart 좋아요를 누른 객체 - 학생, 프로젝트 정보 반환
	 * @throws DeleteException 좋아요를 누르지 않은 강좌를 좋아요 취소할 경우 예외 발생
	 */
	public Cart delete(int lessonId, int studentId) throws DeleteException;
	
	List<Lesson> selectByPage(int currPage, int dataPerPage, int studentId) throws FindException;

	public int selectAllCount() throws FindException;
	
	public int selectAllCount(int studentId) throws FindException;
}
