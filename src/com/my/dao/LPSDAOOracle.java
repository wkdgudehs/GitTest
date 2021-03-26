package com.my.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.service.ILessonService;
import com.my.service.IStudentService;
import com.my.service.LessonService;
import com.my.service.StudentService;
import com.my.sql.MyConnection;
import com.my.vo.LPS;
import com.my.vo.Lesson;
import com.my.vo.Student;

public class LPSDAOOracle implements LPSDAO {

	@Override
	public List<Lesson> selectByStudentId(int studentId) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		String selectLPSSQL = "SELECT * FROM lesson WHERE lesson_id IN \r\n" + 
				"(SELECT lps_lesson_id\r\n" + 
				"FROM lesson_per_student\r\n" + 
				"WHERE lps_student_id = ?)\r\n" + 
				"ORDER BY lesson_start_date, lesson_total_amount";
		List<Lesson> list = new ArrayList<>();
		IStudentService service = new StudentService();
		try {
			pstmt = con.prepareStatement(selectLPSSQL);
			pstmt.setInt(1, studentId);
			rs = pstmt.executeQuery();
			Student student = service.findById(studentId);
			while(rs.next()) {
				int lessonId = rs.getInt("lesson_id");
				Student teacher = student; 
				String lessonName = rs.getString("lesson_name");
				int lessonStatus = rs.getInt("lesson_status");
				int lessonFee = rs.getInt("lesson_fee");
				int lessonCategory = rs.getInt("lesson_category_id");
				Date lessonEndDate = rs.getDate("lesson_end_date");
				Date lessonStartDate = rs.getDate("lesson_start_date");
				Lesson lesson = new Lesson(lessonId, teacher, lessonName, lessonStatus, lessonFee, lessonCategory, lessonEndDate, lessonStartDate);
				list.add(lesson);
			}
			if(list.size() == 0) {
				throw new FindException("강좌가 하나도 없습니다");
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
	@Override
	public LPS selectByStudentLessonId(int lessonId, int studentId) throws FindException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		String selectByLPSSQL = "Select * From LESSON_PER_STUDENT Where LPS_LESSON_ID = ? AND LPS_STUDENT_ID = ?";
		IStudentService service = new StudentService();
		ILessonService lservice = new LessonService();
		LPS lps = new LPS();
		try {
			pstmt = con.prepareStatement(selectByLPSSQL);
			pstmt.setInt(1, lessonId);
			pstmt.setInt(2, studentId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int LPSId = rs.getInt("lps_id");
				Lesson lesson = lservice.findById(lessonId);
				Student student = service.findById(studentId);
				lps.setLPSId(LPSId);
				lps.setLesson(lesson);
				lps.setStudent(student);
			}
			return lps;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
	
	@Override
	public void insert(int lessonId, int studentId) throws AddException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}
		String insertLPSSQL = "INSERT INTO lesson_per_student(LPS_ID, LPS_LESSON_ID, LPS_STUDENT_ID) VALUES (SEQ_LESSON_PER_STUDENT_ID.NEXTVAL, ?, ?)";
		try {
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(insertLPSSQL);
			pstmt.setInt(1, lessonId);
			pstmt.setInt(2, studentId);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new AddException(e.getMessage());
			}
		}finally {
			MyConnection.close(con, pstmt);
		}
	}
	@Override
	public LPS selectByLPSId(int lpsId) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		String selectByLPSSQL = "SELECT * FROM lesson_per_student where lps_id = ?";
		ILessonService lservice = new LessonService();
		IStudentService sservice = new StudentService();
		LPS lps = new LPS();
		try {
			pstmt = con.prepareStatement(selectByLPSSQL);
			pstmt.setInt(1, lpsId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int LPSId = rs.getInt("lps_id");
				int lessonId = rs.getInt("lps_lesson_id");
				int studentId = rs.getInt("lps_student_id");
				Lesson lesson = lservice.findById(lessonId);
				Student student = sservice.findById(studentId);
				lps.setLPSId(LPSId);
				lps.setLesson(lesson);
				lps.setStudent(student);
			}
			return lps;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
	@Override
	public List<LPS> selectByLessonId(int lessonId) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		String selectByLPSSQL = "SELECT * FROM lesson_per_student where lps_lesson_id = ?";
		ILessonService lservice = new LessonService();
		IStudentService sservice = new StudentService();
		List<LPS> list = new ArrayList<>();
		try {
			pstmt = con.prepareStatement(selectByLPSSQL);
			pstmt.setInt(1, lessonId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int LPSId = rs.getInt("lps_id");
				int lessonId1 = rs.getInt("lps_lesson_id");
				int studentId = rs.getInt("lps_student_id");
				Lesson lesson = lservice.findById(lessonId1);
				Student student = sservice.findById(studentId);
				LPS lps = new LPS(LPSId, lesson, student);
				list.add(lps);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
	@Override
	public void insert(LPS lps) throws AddException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int selectAllCount(int studentId, int lessonStatus) throws FindException { //0대기 1 성공 2실패
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
		String selectAllCountSQL = "SELECT COUNT(*) "
				+ "FROM lesson_per_student "
				+ "WHERE lps_student_id = ? AND lps_lesson_id IN (SELECT lesson_id FROM lesson WHERE lesson_status = ?) "
				+ "ORDER BY lps_id DESC";
		try {
			pstmt = con.prepareStatement(selectAllCountSQL);
			pstmt.setInt(1, studentId);
			pstmt.setInt(2, lessonStatus);
			rs = pstmt.executeQuery();
			if(lessonStatus == 0) {//0대기 
				if(rs.next()) {
					int selectAllCount = rs.getInt("COUNT(*)");
					return selectAllCount;
				}
			}else if(lessonStatus ==1) {//1 성공 
				if(rs.next()) {
					int selectAllCount = rs.getInt("COUNT(*)");
					return selectAllCount;
				}
			}else if(lessonStatus ==2) {//2실패
				if(rs.next()) {
					int selectAllCount = rs.getInt("COUNT(*)");
					return selectAllCount;
				}
			}
			throw new FindException("수강한 강좌가 없습니다.");
		} catch (SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
	@Override
	public int selectAllCount(int studentId) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con= MyConnection.getConnection();
		} catch (Exception e1) {
			throw new FindException(e1.getMessage());
		}
		
		String selectAllCountSQL = "SELECT COUNT(*) FROM lesson_per_student WHERE lps_student_id = (SELECT student_id\r\n" + 
				"                                                                FROM student\r\n" + 
				"                                                                WHERE student_id = ?)";
		try {
			pstmt = con.prepareStatement(selectAllCountSQL);
			pstmt.setInt(1, studentId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int selectAllCount = rs.getInt("COUNT(*)");
				return selectAllCount;
			}else {
				throw new FindException("수강한 강좌가 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con,pstmt,rs);
		}
	}
	@Override
	public List<Lesson> selectByPage(int currPage, int dataPerPage, int studentId) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Lesson> pageList = new ArrayList<>();
		StudentDAOOracle sdao = new StudentDAOOracle();
		
		try {
			con = MyConnection.getConnection();
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new FindException(e1.getMessage());
		}
		
		String selectByPageSQL = "SELECT * \r\n" + 
				"FROM (SELECT lesson_id, lesson_teacher_id, lesson_name, lesson_status, lesson_fee, lesson_category_id, lesson_end_date, lesson_start_date, lesson_total_amount, row_number() OVER (ORDER BY lesson_status ASC, lesson_end_date ASC ,lesson_total_amount DESC) AS rnum \r\n" + 
				"      FROM lesson\r\n" + 
				"      WHERE lesson_id IN(SELECT lps_lesson_id\r\n" + 
				"                        FROM lesson_per_student\r\n" + 
				"                        WHERE lps_student_id = ?)\r\n" + 
				")\r\n" + 
				"WHERE rnum BETWEEN fun_start_row(?,?) AND fun_end_row(?,?)\r\n" + 
				"";
		try {
			pstmt = con.prepareStatement(selectByPageSQL);
			pstmt.setInt(1, studentId);
			pstmt.setInt(2, currPage);
			pstmt.setInt(3, dataPerPage);
			pstmt.setInt(4, currPage);
			pstmt.setInt(5, dataPerPage);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int lessonId = rs.getInt("lesson_id");
				String lessonName = rs.getString("lesson_name");
				int lessonStatus = rs.getInt("lesson_status");
				int lessonFee = rs.getInt("lesson_fee");
				Date lessonEnd = rs.getDate("lesson_end_date");
				Date lessonStart = rs.getDate("lesson_start_date");
				Lesson lesson = new Lesson(lessonId, lessonName, lessonStatus ,lessonFee, lessonEnd, lessonStart);
				pageList.add(lesson);
			}
			if(pageList.size()==0) {
				throw new FindException("수강한 강좌가 존재하지 않습니다.");
			}
			return pageList;
		} catch (SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con,pstmt,rs);
		}
	}
	
	public List<Lesson> selectByPage(int currPage, int dataPerPage, int studentId, int lessonStatus) throws FindException { //0대기 1 성공 2실패
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
		String selectByPageSQL = "SELECT * \r\n" + 
				"FROM (SELECT lesson_id, lesson_teacher_id, lesson_name, lesson_status, lesson_fee, lesson_category_id, lesson_end_date, lesson_start_date, lesson_total_amount, row_number() OVER (ORDER BY lesson_end_date DESC, lesson_total_amount DESC) AS rnum \r\n" + 
				"      FROM lesson\r\n" + 
				"      WHERE lesson_id IN(SELECT lps_lesson_id\r\n" + 
				"                        FROM lesson_per_student\r\n" + 
				"                        WHERE lps_student_id = ?)\r\n" + 
				"            AND lesson_status = ?)\r\n" + 
				"WHERE rnum BETWEEN fun_start_row(?,?) AND fun_end_row(?,?)";
		
		StudentService ss = new StudentService();
		List<Lesson> lessonListHold = new ArrayList<>();
		List<Lesson> lessonListSuccess = new ArrayList<>();
		List<Lesson> lessonListFail = new ArrayList<>();
		
		try {
			pstmt = con.prepareStatement(selectByPageSQL);
			pstmt.setInt(1, studentId);
			pstmt.setInt(2, lessonStatus);
			pstmt.setInt(3, currPage);
			pstmt.setInt(4, dataPerPage);
			pstmt.setInt(5, currPage);
			pstmt.setInt(6, dataPerPage);
			rs = pstmt.executeQuery();
			
			if(lessonStatus == 0) {//0대기 
				while(rs.next()) {
					int lessonId = rs.getInt("lesson_id");
					int teacherId = rs.getInt("lesson_teacher_id");
					Student student = ss.findById(teacherId);
					String lessonName = rs.getString("lesson_name");
					int lessonFee = rs.getInt("lesson_fee");
					int lessonCategoryId = rs.getInt("lesson_category_id");
					Date lessonEndDate = rs.getDate("lesson_end_date");
					Date lessonStartDate = rs.getDate("lesson_start_date");
					Lesson lessonStatusHold = new Lesson(lessonId, student, lessonName, lessonStatus ,lessonFee, lessonCategoryId, lessonEndDate, lessonStartDate);
					lessonListHold.add(lessonStatusHold);
				}
				if(lessonListHold.size()==0) {
					throw new FindException("수강한 강좌 중 대기중인 강좌가 없습니다.");
				}
				return lessonListHold;
				
			}else if(lessonStatus ==1) {//1 성공
				while(rs.next()) {
					int lessonId = rs.getInt("lesson_id");
					int teacherId = rs.getInt("lesson_teacher_id");
					Student student = ss.findById(teacherId);
					String lessonName = rs.getString("lesson_name");
					int lessonFee = rs.getInt("lesson_fee");
					int lessonCategoryId = rs.getInt("lesson_category_id");
					Date lessonEndDate = rs.getDate("lesson_end_date");
					Date lessonStartDate = rs.getDate("lesson_start_date");
					Lesson lessonStatusSuccess = new Lesson(lessonId, student, lessonName, lessonStatus ,lessonFee, lessonCategoryId, lessonEndDate, lessonStartDate);
					lessonListSuccess.add(lessonStatusSuccess);
				}
				if(lessonListSuccess.size()==0) {
					throw new FindException("수강한 강좌중 성공한 강좌가 없습니다.");
				}
				return lessonListSuccess;
			}else if(lessonStatus ==2) {// 2실패
				while(rs.next()) {
					int lessonId = rs.getInt("lesson_id");
					int teacherId = rs.getInt("lesson_teacher_id");
					Student student = ss.findById(teacherId);
					String lessonName = rs.getString("lesson_name");
					int lessonFee = rs.getInt("lesson_fee");
					int lessonCategoryId = rs.getInt("lesson_category_id");
					Date lessonEndDate = rs.getDate("lesson_end_date");
					Date lessonStartDate = rs.getDate("lesson_start_date");
					Lesson lessonFailSuccess = new Lesson(lessonId, student, lessonName, lessonStatus ,lessonFee, lessonCategoryId, lessonEndDate, lessonStartDate);
					lessonListFail.add(lessonFailSuccess);
				}
				if(lessonListFail.size()==0) {
					throw new FindException("수강한 강좌 중 실패한 강좌가 없습니다.");
				}
				return lessonListFail;
			}
			throw new FindException("수강한 강좌가 없습니다.");
		} catch (SQLException e) {
			throw new FindException(e.getMessage());
		}
	}


//	public static void main(String[] args) {
//		LPSDAO dao = new LPSDAOOracle();
//		try {
//			List<LPS> lpslist = dao.selectByLessonId(7);
//			for(LPS lps: lpslist) {
//				System.out.println(lps.getLPSId());
//			}
//		} catch (FindException e) {
//			e.printStackTrace();
//		}
		//		try {
//			List<Lesson>lessons = dao.selectByStudentId(12);
//			for(Lesson l: lessons) {
//				System.out.println(l.getLessonId());
//			}
//		} catch (FindException e) {
//			e.printStackTrace();
//		}
		//		try {
//			dao.insert(2, 7);
//			System.out.println("성공");
//		} catch (AddException e) {
//			e.printStackTrace();
//		}
//	}
}