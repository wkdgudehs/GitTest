package com.my.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.my.exception.AddException;
import com.my.exception.DeleteException;
import com.my.exception.FindException;
import com.my.service.ILessonService;
import com.my.service.IStudentService;
import com.my.service.LessonService;
import com.my.service.StudentService;
import com.my.sql.MyConnection;
import com.my.vo.Cart;
import com.my.vo.Lesson;
import com.my.vo.Student;

public class CartDAOOracle implements CartDAO {

	   @Override
	   public List<Lesson> selectById(int studentId) throws FindException {
			Connection con = null; 
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				con = MyConnection.getConnection();
				
			} catch (Exception e) {
				throw new FindException(e.getMessage());
			}
			String selectByIdSQL = "SELECT *\r\n" + 
					"FROM lesson\r\n" + 
					"WHERE lesson_id =\r\n" + 
					"                			   (SELECT cart_lesson_id \r\n" + 
					"                               FROM cart \r\n" + 
					"                               WHERE cart_student_id = ?)\r\n" + 
					"ORDER BY lesson_end_date, lesson_total_amount;";
			try {
				pstmt = con.prepareStatement(selectByIdSQL);
				pstmt.setInt(1, studentId);
				rs = pstmt.executeQuery();	
				List<Lesson> cartLessonList = new ArrayList<>();
				while(rs.next()) {
					Lesson lesson = new Lesson();
					Student teacher = new Student();

					int lesson_id = rs.getInt("LESSON_ID");
					int lesson_teacher_id = rs.getInt("LESSON_TEACHER_ID");
					String lesson_name =rs.getString("LESSION_NAME");
					int lesson_total_amount = rs.getInt("LESSON_TOTAL_AMOUNT");
					int lesson_target_amount = rs.getInt("LESSON_TARGET_AMOUNT");
					int lesson_participant =rs.getInt("LESSON_PARTICIPANT");
					int lesson_status = rs.getInt("LESSON_STATUS");
					Date lesson_create_date = rs.getDate("LESSON_CREATE_DATE");
					Date lesson_end_date = rs.getDate("LESSON_END_DATE");
					Date lesson_start_date = rs.getDate("LESSON_START_DATE");
					int lesson_fee = rs.getInt("LESSON_FEE");
					String lesson_description =rs.getString("LESSON_DESCRIPTION");
					int lesson_category_id = rs.getInt("LESSON_CATEGORY_ID");
					int lesson_recommend_cnt = rs.getInt("LESSON_RECOMMEND_CNT");
					
					lesson.setLessonId(lesson_id);
					lesson.setTeacher(teacher);
					teacher.setStudentId(lesson_teacher_id);
					lesson.setLessonName(lesson_name);
					lesson.setLessonTotalFee(lesson_total_amount);
					lesson.setLessonTargetFee(lesson_target_amount);
					lesson.setLessonParticipant(lesson_participant);
					lesson.setLessonStatus(lesson_status);
					lesson.setLessonCreate(lesson_create_date);
					lesson.setLessonEnd(lesson_end_date);
					lesson.setLessonStart(lesson_start_date);
					lesson.setLessonFee(lesson_fee);
					lesson.setLessonDescription(lesson_description);
					lesson.setLessonCategory(lesson_category_id);
					lesson.setLessonRecommend(lesson_recommend_cnt);				
					
					cartLessonList.add(lesson);
				}
				return cartLessonList;
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new FindException(e.getMessage());
			}finally {
				MyConnection.close(con,pstmt, rs);
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
		String insertCartSQL = "INSERT INTO cart(cart_id, cart_lesson_id, cart_student_id) VALUES (seq_cart_id.NEXTVAL, ?, ?)";
		try {
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(insertCartSQL);
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
	public Cart delete(int lessonId, int studentId) throws DeleteException {
		Connection con = null;
		PreparedStatement pstmt = null;
		Cart c = new Cart();
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DeleteException(e.getMessage());
		}
		String deleteSQL = "DELETE FROM cart WHERE CART_LESSON_ID = ? AND CART_STUDENT_ID = ?";
		try {
			pstmt = con.prepareStatement(deleteSQL);
			pstmt.setInt(1, lessonId);
			pstmt.setInt(2, studentId);
			int rowcnt = pstmt.executeUpdate();
			if(rowcnt != 1) { //삭제건수가 0건
				throw new DeleteException("삭제실패: 아이디에 해당 고객이 없습니다");
			}
			return c;
		}catch(SQLException e) {
			throw new DeleteException(e.getMessage());
		}finally {
			MyConnection.close(con, pstmt);
		}
	}


	@Override
	public Cart selectById(int lessonId, int studentId) throws FindException {
		  Connection con = null; 
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      ILessonService lservice = new LessonService();
	      IStudentService sservice = new StudentService();
	      Cart cart = new Cart();
	         try {
	            con = MyConnection.getConnection();
	         } catch (Exception e) {
	            throw new FindException(e.getMessage());
	         }
	         String selectByIdSQL = "Select * From cart where cart_lesson_id = ? AND cart_student_id = ?";
	         try {
	            pstmt = con.prepareStatement(selectByIdSQL);
	            pstmt.setInt(1, lessonId);
	            pstmt.setInt(2, studentId);	            
	            rs = pstmt.executeQuery();
	            Lesson l = lservice.findById(lessonId);
	            Student s = sservice.findById(studentId);
	            if(rs.next()) {
	            	int cartId = rs.getInt("cart_id");
	            	cart.setCartId(cartId);
	            	cart.setLesson(l);
	            	cart.setStudent(s);
	            }
	            return cart;
	         } catch (SQLException e) {
	            e.printStackTrace();
	            throw new FindException(e.getMessage());
	         }finally {
	            MyConnection.close(con,pstmt, rs);
	         }
	   }
	



	@Override
	public List<Lesson> selectByPage(int currPage, int dataPerPage,int studentId) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}
		String selectByPageSQL = "SELECT * FROM (\r\n" + 
				"SELECT lesson_id,lesson_name, lesson_end_date, lesson_description,lesson_total_amount,lesson_target_amount,row_number() \r\n" + 
				"        OVER (ORDER BY lesson_end_date DESC) AS rnum\r\n" + 
				"        FROM lesson\r\n" + 
				"        WHERE lesson_id IN (SELECT cart_lesson_id\r\n" + 
				"        FROM cart\r\n" + 
				"        WHERE cart_student_id = ?)\r\n" + 
				"    )\r\n" + 
				"        WHERE rnum BETWEEN fun_start_row(?,?) AND fun_end_row(?,?)";
		List<Lesson> currPageList = new ArrayList<>();
		
		try {
			pstmt = con.prepareStatement(selectByPageSQL);
			Calendar enddate = Calendar.getInstance();   //
			Calendar sysdate = Calendar.getInstance();  //
			sysdate.setTime(new Date());                //
			pstmt.setInt(1, studentId);
			pstmt.setInt(2, currPage);
			pstmt.setInt(3, dataPerPage);
			pstmt.setInt(4, currPage);
			pstmt.setInt(5, dataPerPage);
			rs = pstmt.executeQuery();
	
			while(rs.next()) {
				Lesson lesson = new Lesson();
				int lessonId= rs.getInt("lesson_id");
				String lessonName = rs.getString("lesson_name");
				Date lessonEnd = rs.getDate("lesson_end_date");
				String lessonDescription = rs.getString("lesson_description");
				int lessonTotalFee = rs.getInt("lesson_total_amount");	
				int lessonTargetFee = rs.getInt("lesson_target_amount");
				enddate.setTime(lessonEnd); 
				int targetPercent = (lessonTotalFee*100 / lessonTargetFee);//퍼센티지
				long diffDays = (enddate.getTimeInMillis() - sysdate.getTimeInMillis()) / 1000 / (24*60*60);  
				if(diffDays < 0) {
					diffDays = 0;
				}
				System.out.println(diffDays);
				
				lesson.setLessonId(lessonId);
				lesson.setLessonName(lessonName);
				lesson.setLessonDescription(lessonDescription);
				lesson.setLessonEnd(lessonEnd);
				lesson.setLessonTotalFee(lessonTotalFee);
				lesson.setLessonTargetFee(lessonTargetFee);
				lesson.setTargetPercent(targetPercent);
				lesson.setDiffDays((int)diffDays);
				currPageList.add(lesson);
			}
			if(currPageList.size() == 0) {
				throw new FindException("좋아요가 없습니다.");
			}
			System.out.println(currPageList);
			return currPageList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con,pstmt,rs);
		}
	}

	@Override
	public int selectAllCount(int studentId) throws FindException {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			throw new FindException(e.getMessage());
		}
		
		String selectAllCountSQL = "SELECT COUNT (*) FROM cart where cart_student_id = ? ";
		
		try {
			pstmt = con.prepareStatement(selectAllCountSQL);
		    pstmt.setInt(1, studentId);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int selectAllCount = rs.getInt("COUNT(*)");
				return selectAllCount;
			}else {
				throw new FindException("좋아요 한 강좌가 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
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
			throw new FindException(e.getMessage());
		}
		
		String selectAllCountSQL = "SELECT COUNT (*) FROM cart";
		
		try {
			pstmt = con.prepareStatement(selectAllCountSQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				int selectAllCount = rs.getInt("COUNT(*)");
				return selectAllCount;
			}else {
				throw new FindException("좋아요 한 강좌가 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
//	public static void main(String[] args) {
//	CartDAO dao = new CartDAOOracle();
//	try {
//		Cart cart = dao.selectById(3, 8);
//		System.out.println(cart.getCartId());
//	} catch (FindException e) {
//		e.printStackTrace();
//	}
//	try {
//		dao.delete(5, 10);
//		System.out.println("성공");
//	} catch (DeleteException e) {
//		e.printStackTrace();
//	}
//}
}
