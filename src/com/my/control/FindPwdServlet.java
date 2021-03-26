package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.my.exception.FindException;
import com.my.service.StudentService;
import com.my.vo.Student;

/**
 * 비밀번호 찾기 서블릿
 */
public class FindPwdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**비밀번호 찾기 메소드
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		//1. 객체 얻기
		String email = request.getParameter("email");
		StudentService service = new StudentService();
		try {
			Student s = service.findByEmail(email);
			out.print("{\"status\":1}");
			System.out.println("이메일찾기 성공");
		} catch (FindException e) {
			e.printStackTrace();
			out.print("{\"status\":0}");
			System.out.println("이메일찾기 실패");
		}
	}

}