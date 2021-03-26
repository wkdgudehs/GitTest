package com.my.service;

import com.my.dao.StudentDAOOracle;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.vo.Student;

public class StudentService implements IStudentService {
	private StudentDAOOracle sdao= new StudentDAOOracle();
	
	@Override
	public Student findById(int studentId) throws FindException {
		return sdao.selectById(studentId);
	}
	
	@Override
	public Student findByEmail(String studentEmail) throws FindException {
		return sdao.selectByEmail(studentEmail);
	}
	
	@Override
	public Student findByName(String studentName) throws FindException {
		return sdao.selectByName(studentName);
	}

	@Override
	public void add(Student s) throws AddException {
		sdao.insert(s);
	}

	@Override
	public Student modify(Student s) throws ModifyException {
		Student s1 = sdao.update(s);
		return s;
	}
	
	@Override
	public Student modify(Student s, String inputPwd) throws ModifyException {
		Student s1 = sdao.update(s, inputPwd);
		return s;
	}

	@Override
	public Student findByPwd(String inputPwd) throws FindException {
		return sdao.selectByPwd(inputPwd);
	}
	@Override
	public Student login(String studentEmail, String studentPwd) throws FindException {
		try {
			Student s = sdao.selectByEmail(studentEmail);
			System.out.println(s);
			if(s.getStudentPwd().equals(studentPwd)) {
				System.out.println("비밀번호일치");
				System.out.println("로그인 성공");
				return s;
			}else {
				throw new FindException("로그인 실패");
			}
		}catch(FindException e) {
			throw new FindException("로그인 실패");
		}
	}
	
//		String studentEmail = "orobez0@ihg.com";
//		String studentPwd = "Xz9FQA3";
//		try {
//			Student s = ss.login(studentEmail, studentPwd);
//			System.out.println(s);
//		} catch (FindException e) {
//			e.printStackTrace();
//		}
		
//	}
	

}
