package com.my.control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.my.exception.AddException;
import com.my.service.StudentService;
import com.my.vo.Student;

public class SigninServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//servlet 테스트 할때는  브라우저쪽에  무조건  doget이기 때문에 dopost인 상황에서는 dopost 대신 service를 사용
	//다만 service는 보안상 문제가 취약할수 있다.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=UTF-8");	
		PrintWriter out =  response.getWriter();
		StudentService service = new StudentService();
		Student s = new Student();
//		HttpSession session = request.getSession();
//		String studentEmail =(String) session.getAttribute("pwd");
//		String studentName  = (String) session.getAttribute("name");
		s.setStudentName(request.getParameter("nickname"));
		s.setStudentEmail(request.getParameter("email"));
		s.setStudentPwd(request.getParameter("pwd"));
		s.setStudentPhone(request.getParameter("phonenumber"));
		try {
			service.add(s);
			out.print("{\"status\":1}");
		} catch (AddException e) {
			e.printStackTrace();
			out.print("{\"status\": -1, \"msg\": " + e.getMessage() + "}");
		}
	}
}