package com.my.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.sql.MyConnection;
import com.my.vo.Student;

public class StudentDAOOracle implements StudentDAO {

	@Override
	public Student selectById(int studentId) throws FindException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			throw new FindException();
		}
		
		String selectByIdSQL = "SELECT * FROM student WHERE student_id = ?";
		//구문 송신
		try {
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setInt(1, studentId);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String studentName = rs.getString("student_name");
				String studentEmail = rs.getString("student_email");
				String studentPhone = rs.getNString("student_phone");
				int studentStatus = rs.getInt("student_status");
				//현재 마이페이지 - 내 정보 진입한 학생 객체 생성
				Student curStudent = new Student(studentId, studentEmail, studentName, studentPhone, studentStatus);
				//현재 마이페이지 - 내 정보 진입한 학생 반환
				return curStudent;
			}else {
				throw new FindException("학생정보가 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con,pstmt,rs);
		}
	}
	
	@Override
	public Student selectByEmail(String studentEmail) throws FindException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			throw new FindException();
		}
		
		String selectByIdSQL = "SELECT * FROM student WHERE student_email = ?";
		//구문 송신
		try {
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setString(1, studentEmail);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int studentId = rs.getInt("student_id");
				String studentName = rs.getString("student_name");
				String studentPwd = rs.getNString("student_pwd");
				String studentPhone = rs.getString("student_phone");
				int studentStatus = rs.getInt("student_status");
				//현재 마이페이지 - 내 정보 진입한 학생 객체 생성
				Student curStudent = new Student(studentId, studentEmail, studentPwd,studentName, studentPhone, studentStatus);
				//현재 마이페이지 - 내 정보 진입한 학생 반환
				return curStudent;
			}else {
				throw new FindException("학생정보가 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con,pstmt,rs);
		}
	};
	
	@Override
	public Student selectByName(String name) throws FindException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			throw new FindException();
		}
		String selectByIdSQL = "SELECT * FROM student WHERE student_name = ?";
		//구문 송신
		try {
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int studentId = rs.getInt("student_id");
				String studentEmail = rs.getString("student_email");
				String studentName = rs.getString("student_name");
				String studentPwd = rs.getString("student_pwd");
				String studentPhone = rs.getString("student_phone");
				int studentStatus = rs.getInt("student_status");
				//현재 마이페이지 - 내 정보 진입한 학생 객체 생성
				Student curStudent = new Student(studentId, studentEmail, studentPwd, studentName, studentPhone, studentStatus);
				//현재 마이페이지 - 내 정보 진입한 학생 반환
				return curStudent;
			}else {
				throw new FindException("학생정보가 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con,pstmt,rs);
		}
	};
	
	@Override
	public void insert(Student s) throws AddException {
		Connection con = null;  //서버연결
		
		try {
			con=MyConnection.getConnection();
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new AddException("추가 실패: 이유:" + e.getMessage());
		}
		PreparedStatement pstmt = null; // 상속받는 인터페이스로 SQL구문을 실행시키는 기능을 갖는 객체
		String insertSQL = "INSERT INTO student (student_id, student_email, student_pwd, student_name, student_phone, student_status)\r\n" + 
							"VALUES (SEQ_STUDENT_ID.NEXTVAL, ?, ?, ?, ?, 1)";
		try {
			pstmt = con.prepareStatement(insertSQL);
			pstmt.setString(1, s.getStudentEmail());
			pstmt.setString(2, s.getStudentPwd());
			pstmt.setString(3, s.getStudentName());
			pstmt.setString(4, s.getStudentPhone());
			
			pstmt.executeUpdate();  // pstmt값  반환
		} catch (SQLException e) {
			if(e.getErrorCode() == 1) {
				throw new AddException("이미 존재하는 아이디입니다.");
			}else if(e.getErrorCode() == 3) {
				throw new AddException("이미 존재하는 닉네임입니다.");
			}else {
			e.printStackTrace();
			throw new AddException(e.getMessage());
			}
		}finally {
			MyConnection.close(con,pstmt);
		}
	}

	@Override
	public Student update(Student s, String inputPwd) throws ModifyException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			throw new ModifyException(e.getMessage());
		}
		String updateSQLUPDATE = "UPDATE student SET ";
		String updateSQLSET = "";
		String updateSQLWHERE = " WHERE student_email ='"+s.getStudentEmail()+"'";
		
		try {
			stmt = con.createStatement();
			boolean flag = false; //수정 여부
			try {
				Student originalstu = selectByEmail(s.getStudentEmail());
				if(!(originalstu.getStudentPwd().equals(inputPwd))) {
					throw new ModifyException("비밀번호가 일치하지 않습니다.");
				}else{
					if(s.getStudentPwd() != null && !(s.getStudentPwd().equals(""))) {
						updateSQLSET = "student_pwd='"+s.getStudentPwd()+"'";
						updateSQLWHERE = " WHERE student_email ='"+s.getStudentEmail()+"'"+"AND student_pwd='"+inputPwd+"'";
						flag = true;
					}
					if(s.getStudentPhone() !=null && !(s.getStudentPhone().equals(""))) {
						if(flag) {
							updateSQLSET += ",";
						}
						updateSQLSET +="student_phone='"+s.getStudentPhone()+"'";
						flag = true;
					}
				}
				if(flag) {
					System.out.println(updateSQLUPDATE+updateSQLSET+updateSQLWHERE);
					stmt.executeUpdate(updateSQLUPDATE+updateSQLSET+updateSQLWHERE);
					try {
						return selectByEmail(s.getStudentEmail());
					}catch(FindException e) {
						throw new ModifyException(e.getMessage());
					}
				}else {
					throw new ModifyException("수정할 내용이 없습니다");
				}
			} catch (FindException e1) {
				throw new ModifyException(e1.getMessage());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ModifyException(e.getMessage());
		}finally {
			MyConnection.close(con,stmt);
		}
		
	}
	
//	public static void main(String[] args) {
		//selectById 테스트 코드
//		StudentDAOOracle sdao = new StudentDAOOracle();
//		int studentId = 2;
//		Student s = new Student();
//		try {
//			s = sdao.selectById(studentId);
//			System.out.println(s);
//		} catch (FindException e) {
//			e.printStackTrace();
//		}
		
		//update 테스트 코드
//		Student s2 = new Student("orobez0@ihg.com", "zzz113", "010-1111-1111");
//		Student s2 = new Student("orobez0@ihg.com", "jaeK");
//		Student s2 = new Student("orobez0@ihg.com", "010-1234-1234");
//		Student s2 = new Student("orobez0@ihg.com", "");
		//비번변경
//		Student s2 = new Student(1,"orobez0@ihg.com","changpwd");
		//연락처수정
//		Student s2 = new Student("orobez0@ihg.com","010-9999-9999");
//		Student s2 = new Student("orobez0@ihg.com","finalpwd","999-9999-9999");
		//연락처 비번 수정
		
		//기존비밀번호 입력하여 수정하기
//		Student s2 = new Student(1,"orobez0@ihg.com","change");
		//비밀번호 연락처 동시에 변경
//		Student s2 = new Student("orobez0@ihg.com","111111","999-9999-9999");
		//연락처만 변경
//		Student s2 = new Student("orobez0@ihg.com","010-9999-9999");
//		try {
//			Student s3 = sdao.update(s2,"111111");
//			System.out.println(s2);
//		} catch (ModifyException e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public Student update(Student s) throws ModifyException {
		return null;
	}

	@Override
	public Student selectByPwd(String inputPwd) throws FindException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
		} catch (Exception e) {
			throw new FindException();
		}
		
		String selectByIdSQL = "SELECT * FROM student WHERE student_pwd = ?";
		//구문 송신
		try {
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setString(1, inputPwd);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int studentId = rs.getInt("student_id");
				String studentEmail = rs.getString("student_email");
				String studentName = rs.getString("student_name");
				String studentPwd = rs.getString("student_pwd");
				String studentPhone = rs.getString("student_phone");
				int studentStatus = rs.getInt("student_status");
				//현재 마이페이지 - 내 정보 진입한 학생 객체 생성
				Student curStudent = new Student(studentId, studentEmail, studentName, studentPwd, studentPhone, studentStatus);
				//현재 마이페이지 - 내 정보 진입한 학생 반환
				return curStudent;
			}else {
				throw new FindException("학생정보가 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			throw new FindException(e.getMessage());
		}finally {
			MyConnection.close(con,pstmt,rs);
		}
	}
	
}