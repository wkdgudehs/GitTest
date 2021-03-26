package com.my.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.my.exception.FindException;
import com.my.sql.MyConnection;
import com.my.vo.LPS;
import com.my.vo.Lesson;
import com.my.vo.Notice;
import com.my.vo.Student;
//수정
public class LessonDAOOracle implements LessonDAO {

	@Override
	public List<Lesson> selectAll() throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		//String selectAllSQL = "select * from lesson order by lesson_end_date";
		String selectAllSQL = "select * from lesson where lesson_status = 0 order by lesson_end_date";
		List<Lesson> all = new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		try {
			pstmt = con.prepareStatement(selectAllSQL);
			rs = pstmt.executeQuery();
			Calendar enddate = Calendar.getInstance();
			Calendar sysdate = Calendar.getInstance();
			sysdate.setTime(new Date());
			while(rs.next()) {
				int lessonId = rs.getInt("lesson_id");
				String lessonName = rs.getString("lesson_name");
				int lessonTargetFee = rs.getInt("lesson_target_amount");
				int lessonTotalFee = rs.getInt("lesson_total_amount");;
				String lessonDescription = rs.getString("lesson_description");
				Date lessonEnd = rs.getDate("lesson_end_date");
				int categoryId = rs.getInt("lesson_category_id");
				enddate.setTime(lessonEnd);
				long diffDays = (enddate.getTimeInMillis() - sysdate.getTimeInMillis()) / 1000 / (24*60*60);
				if(diffDays < 0) {
					diffDays = 0;
				}
				Lesson l = new Lesson(lessonId, lessonName, lessonDescription, lessonTargetFee, lessonTotalFee, lessonTotalFee * 100/lessonTargetFee, (int)diffDays, categoryId);
				all.add(l);
			}
			if(all.size() == 0) {
				throw new FindException("강좌가 하나도 없습니다");
			}
			return all;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(con, pstmt, rs);
		}
	}

	@Override
	public Lesson selectById(int lessonId) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		
		String selectByIdSQL = "SELECT * FROM lesson WHERE lesson_id = ?";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		
		try {
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setInt(1, lessonId);
			rs = pstmt.executeQuery();
			Calendar enddate = Calendar.getInstance();
			Calendar sysdate = Calendar.getInstance();
			if(rs.next()) {
				int lessonId1 = rs.getInt("lesson_id");//강좌아이디
				String lessonName = rs.getString("lesson_name");//강좌이름
				int lessonTotalFee = rs.getInt("lesson_total_amount");//모인금액
				int lessonTargetFee = rs.getInt("lesson_target_amount");//목표금액
				int targetPercent = (lessonTotalFee*100 / lessonTargetFee);//퍼센티지
				Date lessonEnd = rs.getDate("lesson_end_date");//모집마감일
				enddate.setTime(lessonEnd);
				long diffDays = (enddate.getTimeInMillis() - sysdate.getTimeInMillis()) / 1000 / (24*60*60);//남은기간
				int lessonParticipant = rs.getInt("lesson_participant");;//신청한 사람
				int lessonStatus = rs.getInt("lesson_status");;//펀딩진행중
				int lessonFee = rs.getInt("lesson_fee");//강좌 금액
				int lessonRecommend = rs.getInt("lesson_recommend_cnt");;//추천개수
		
				return new Lesson(lessonId1, lessonName, lessonTotalFee, 
							lessonTargetFee, targetPercent,
									lessonEnd, (int)diffDays, 
									lessonParticipant, lessonStatus, 
									lessonFee, lessonRecommend);
				}	
				else {
					throw new FindException("번호에 해당하는 상품이 없습니다");
				} 
					
			} catch (SQLException e) {
				e.printStackTrace();
				throw new FindException(e.getMessage());
			} finally {
				MyConnection.close(con, pstmt, rs);
			}
		}

	@Override
	public List<Lesson> selectByUnion(String union) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
//		String selectAllSQL = "SELECT *\r\n" + 
//				"FROM lesson\r\n" + 
//				"WHERE lesson_name LIKE '%'||?||'%'\r\n" + 
//				"      OR lesson_category_id IN (SELECT category_id FROM category WHERE category_parent_id IN (SELECT category_id FROM category WHERE category_name LIKE '%'||?||'%'))\r\n" + 
//				"      OR lesson_category_id IN (SELECT category_id FROM category WHERE category_name LIKE '%'||?||'%')\r\n" + 
//				"ORDER BY lesson_end_date";

		String selectAllSQL = "SELECT *\r\n" + 
				"FROM lesson\r\n" + 
				"WHERE lesson_status = 0 AND (lesson_name LIKE '%'||?||'%'\r\n" + 
				"      OR lesson_category_id IN (SELECT category_id FROM category WHERE category_parent_id IN (SELECT category_id FROM category WHERE category_name LIKE '%'||?||'%'))\r\n" + 
				"      OR lesson_category_id IN (SELECT category_id FROM category WHERE category_name LIKE '%'||?||'%'))\r\n" + 
				"ORDER BY lesson_end_date";
		
		List<Lesson> all = new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		try {
			pstmt = con.prepareStatement(selectAllSQL);
			pstmt.setString(1, union);
			pstmt.setString(2, union);
			pstmt.setString(3, union);
			rs = pstmt.executeQuery();
			Calendar enddate = Calendar.getInstance();
			Calendar sysdate = Calendar.getInstance();
			sysdate.setTime(new Date());
			
			while(rs.next()) {
				int lessonId = rs.getInt("lesson_id");
				String lessonName = rs.getString("lesson_name");
				int lessonTargetFee = rs.getInt("lesson_target_amount");
				int lessonTotalFee = rs.getInt("lesson_total_amount");;
				String lessonDescription = rs.getString("lesson_description");
				Date lessonEnd = rs.getDate("lesson_end_date");
				int categoryId = rs.getInt("lesson_category_id");
				enddate.setTime(lessonEnd);
				long diffDays = (enddate.getTimeInMillis() - sysdate.getTimeInMillis()) / 1000 / (24*60*60);
				if(diffDays < 0) {
					diffDays = 0;
				}
				Lesson l = new Lesson(lessonId, lessonName, lessonDescription, lessonTargetFee, lessonTotalFee, lessonTotalFee * 100/lessonTargetFee, (int)diffDays, categoryId);
				all.add(l);
			}
			return all;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(con, pstmt, rs);
		}
	}

	@Override
	public int selectAllCount() throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		String selectAllCountSQL = "SELECT COUNT (*) FROM lesson";
//		String selectAllCountSQL = "SELECT COUNT(*) FROM lesson WHERE lesson_status = 0";
		try {
			pstmt = con.prepareStatement(selectAllCountSQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int selectAllCount = rs.getInt("COUNT(*)");
				return selectAllCount;
			}else {
				throw new FindException("강좌가 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con,pstmt,rs);
		}
	}

	@Override
	public List<Lesson> selectByPage(int currPage, int dataPerPage) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
//		String selectByPageSQL = "SELECT * FROM (SELECT LESSON_ID,LESSON_TEACHER_ID,LESSON_NAME,LESSON_TOTAL_AMOUNT,LESSON_TARGET_AMOUNT,LESSON_PARTICIPANT,LESSON_STATUS,LESSON_CREATE_DATE,LESSON_END_DATE,LESSON_START_DATE,LESSON_FEE,LESSON_DESCRIPTION,LESSON_CATEGORY_ID,LESSON_RECOMMEND_CNT, row_number() OVER (ORDER BY lesson_end_date) AS rnum FROM lesson) WHERE rnum BETWEEN fun_start_row(?, ?) AND fun_end_row(?, ?)";
//		String selectByPageSQL = "SELECT * FROM (SELECT LESSON_ID,LESSON_TEACHER_ID,LESSON_NAME,LESSON_TOTAL_AMOUNT,LESSON_TARGET_AMOUNT,LESSON_PARTICIPANT,LESSON_STATUS,LESSON_CREATE_DATE,LESSON_END_DATE,LESSON_START_DATE,LESSON_FEE,LESSON_DESCRIPTION,LESSON_CATEGORY_ID,LESSON_RECOMMEND_CNT, row_number() OVER (ORDER BY lesson_end_date) AS rnum FROM lesson) WHERE lesson_status = 0 AND rnum BETWEEN fun_start_row(?, ?) AND fun_end_row(?, ?)";
		String selectByPageSQL = "SELECT * FROM (SELECT LESSON_ID,LESSON_TEACHER_ID,LESSON_NAME,LESSON_TOTAL_AMOUNT,LESSON_TARGET_AMOUNT,LESSON_PARTICIPANT,LESSON_STATUS,LESSON_CREATE_DATE,LESSON_END_DATE,LESSON_START_DATE,LESSON_FEE,LESSON_DESCRIPTION,LESSON_CATEGORY_ID,LESSON_RECOMMEND_CNT, row_number() OVER (ORDER BY lesson_end_date) AS rnum FROM lesson WHERE lesson_status = 0) WHERE rnum BETWEEN fun_start_row(?, ?) AND fun_end_row(?, ?)";
		List<Lesson> currPageList = new ArrayList<>();
		try {
			pstmt = con.prepareStatement(selectByPageSQL);
			pstmt.setInt(1, currPage);
			pstmt.setInt(2, dataPerPage);
			pstmt.setInt(3, currPage);
			pstmt.setInt(4, dataPerPage);
			rs = pstmt.executeQuery();
			Calendar enddate = Calendar.getInstance();
			Calendar sysdate = Calendar.getInstance();
			sysdate.setTime(new Date());
			while(rs.next()) {
				int lessonId = rs.getInt("lesson_id");
				String lessonName = rs.getString("lesson_name");
				int lessonTargetFee = rs.getInt("lesson_target_amount");
				int lessonTotalFee = rs.getInt("lesson_total_amount");;
				String lessonDescription = rs.getString("lesson_description");
				Date lessonEnd = rs.getDate("lesson_end_date");
				int categoryId = rs.getInt("lesson_category_id");
				enddate.setTime(lessonEnd);
				long diffDays = (enddate.getTimeInMillis() - sysdate.getTimeInMillis()) / 1000 / (24*60*60);
				if(diffDays < 0) {
					diffDays = 0;
				}
				Lesson l = new Lesson(lessonId, lessonName, lessonDescription, lessonTargetFee, lessonTotalFee, lessonTotalFee * 100/lessonTargetFee, (int)diffDays, categoryId);
				currPageList.add(l);
			}
			if(currPageList.size() == 0) {
				throw new FindException("강좌가 하나도 없습니다");
			}
			return currPageList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con,pstmt,rs);
		}
	}

//	public static void main(String[]args) {
//		LessonDAOOracle dao = new LessonDAOOracle();
//		try {
//			List<Lesson> list = dao.selectAll();
//			for(Lesson l: list) {
//				System.out.println(l.getTargetPercent());
//			}
//		} catch (FindException e) {
//			e.printStackTrace();
//		}	
//	}
}
