package com.my.dao;

import java.util.List;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.vo.LPS;
import com.my.vo.Lesson;

public interface LPSDAO {
	
	/**
	 * 마이페이지 내가 수강한 강좌 리스트 조회
	 * @param studentId 로그인한 학생 계정
	 * @return List<Lesson> 수강한 강좌 리스트
	 * @throws FindException 수강한 강좌가 없으면 예외 발생
	 */
	public List<Lesson> selectByStudentId(int studentId) throws FindException;
	/**
	 * LPS 조회
	 * @param studentId, lessonId 로그인한 학생 계정
	 * @return LPS 현재 로그인한 수강생이 현재 선택된 강좌를 수강했는지 여부
	 * @throws FindException 수강생이 해당 강좌를 수강하지 않았을 경우 발생한다 
	 */
	public LPS selectByStudentLessonId(int studentId, int lessonId) throws FindException;
	/**
	 * LPSId를 이용한 LPS 조회
	 * @param lpsId 로그인한 학생과 선택한 강좌의 해당하는 id
	 * @return LPS
	 * @throws FindException
	 */
	public LPS selectByLPSId(int lpsId) throws FindException;
	/**
	 * LPSId를 이용한 LPS 조회
	 * @param lpsId 로그인한 학생과 선택한 강좌의 해당하는 id
	 * @return LPS
	 * @throws FindException
	 */
	public List<LPS> selectByLessonId(int lessonId) throws FindException;
	/**
	 * 마이페이지 - 내가 수강한 강좌 추가
	 * @param lessonId, studentId 선택된 강좌ID 와 현재 로그인된 ID
	 * @throws AddException 학생 1명이 동일 강좌 2개를 수강할 경우 예외 발생
	 */
	public void insert(int lessonId, int studentId) throws AddException;
	/**
	 * 마이페이지 - 내가 수강한 강좌 추가
	 * @param lps 객체 추가
	 * @throws AddException 학생 1명이 동일 강좌 2개를 수강할 경우 예외 발생
	 */
	public abstract void insert(LPS lps) throws AddException;
	
	//내가 수강한 강좌중에서 강좌 상태별 강좌 조회
	int selectAllCount(int studentId, int lessonStatus) throws FindException;
	
	//마이페이지 수강현황 강좌 모두 조회
	int selectAllCount(int studentId) throws FindException;
	
	//마이페이지 수강현황 강좌 상태별 페이징 기능 구현
	List<Lesson> selectByPage(int currPage, int dataPerPage, int studentId) throws FindException;
	
	//마이페이지 수강현황 강좌 상태별 페이징 기능 구현
	public List<Lesson> selectByPage(int currPage, int dataPerPage, int studentId, int lessonStatus) throws FindException;
}