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

public class EmailDupCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//doPost/service
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		String studentEmail = request.getParameter("student_email");
		StudentService service = new StudentService();
		try {
			Student s = service.findByEmail(studentEmail);
			if(s.getStudentId() != 0) {
				out.print("{\"status\":1}");
			}else {
				out.print("{\"status\":-1}");				
			}
		} catch (FindException e) {
			out.print("{\"status\":-1}");
			System.out.println("학생정보가 존재하지 않습니다");
		}
	}
}