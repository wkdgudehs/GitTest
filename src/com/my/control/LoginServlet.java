package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.my.exception.FindException;
import com.my.service.StudentService;
import com.my.vo.Student;

/**
 * 로그인 페이지 서블릿
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 로그인 서블릿
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		//요청 데이터 전달 받기
		String studentEmail = request.getParameter("student_email");
		String studentPwd = request.getParameter("student_pwd");
		System.out.println("이메일: "+studentEmail);
		System.out.println("비밀번호: "+studentPwd);
		//비즈니스로직 시작 - 서비스객체에서 로그인 메소드 사용하기 위해 객체 생성
		StudentService service = new StudentService();
		try {
			//로그인 시도
			Student s = service.login(studentEmail, studentPwd);
			System.out.println("로그인 시도");
			if(s!=null) {
				//session 객체 얻기
				HttpSession session = request.getSession();
				//상태정보값 추가
				session.setAttribute("loginInfo", studentEmail);
				out.print("{\"status\":1}"); //로그인 성공
			}else {
				out.print("{\"status\":0}"); //로그인 실패
			}
		} catch (FindException e) {
			out.print("{\"status\":-1}"); //응답 에러 발생
		}
		
	}

}